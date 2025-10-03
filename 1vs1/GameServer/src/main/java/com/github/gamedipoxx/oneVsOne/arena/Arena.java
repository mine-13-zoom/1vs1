package com.github.gamedipoxx.oneVsOne.arena;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.github.gamedipoxx.oneVsOne.BungeeCordManager;
import com.github.gamedipoxx.oneVsOne.Messages;
import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.events.GameStateChangeEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerJoinArenaEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerLeaveArenaEvent;
import com.github.gamedipoxx.oneVsOne.scoreboard.Scoreboard;
import com.github.gamedipoxx.oneVsOne.utils.GameState;
import com.github.gamedipoxx.oneVsOne.utils.MySQLManager;

public class Arena {
	private static HashMap<String, Integer> arenaCounts = new HashMap<>();
	private String arenaName;
	private ArenaMap arenamap;
	private int playercount;
	private GameState gameState;
	private Collection<Player> players = new ArrayList<Player>();
	private Scoreboard scoreboard;
	
	public Arena(String mapName) {
		
		int count = arenaCounts.getOrDefault(mapName, 0);
		count++;
		arenaCounts.put(mapName, count);
		this.arenaName = mapName + "-" + count;
		
		//Map
		if(ArenaMap.getMaps().size() == 0) {
			OneVsOne.getPlugin().getLogger().warning("No Maps in config.yml");
			Bukkit.getPluginManager().disablePlugin(OneVsOne.getPlugin());
			return;
		}
		arenamap = new ArenaMap(mapName, arenaName);
		
		//playercound & Gamestate init
		playercount = 0;
		gameState = GameState.WAITING;
		
		//Scoreboard
		if(OneVsOne.getPlugin().getConfig().getBoolean("scoreboard")) {
			scoreboard = new Scoreboard(this);
		}
	}
	
	public Arena() {
		this(ArenaMap.getMaps().get(new Random().nextInt(ArenaMap.getMaps().size())));
	}
	
	
	public void joinPlayer(Player player) { //adds a player to the arena and fires event
		if(playercount == 0) {
			player.teleport(arenamap.getSpawn1());
		}
		if(playercount == 1) {
			player.teleport(arenamap.getSpawn2());
		}
		
		players.add(player);
		playercount = players.size();
		
		PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(this, player);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
	}
	
	public void removePlayer(Player player) {
		player.getInventory().clear();
		if(player.isOnline()) {
			player.teleport(new Location(Bukkit.getWorld(OneVsOne.getPlugin().getConfig().getString("Lobby.World")), OneVsOne.getPlugin().getConfig().getInt("Lobby.X"), OneVsOne.getPlugin().getConfig().getInt("Lobby.Y"), OneVsOne.getPlugin().getConfig().getInt("Lobby.Z"), OneVsOne.getPlugin().getConfig().getLong("Lobby.Pitch"), OneVsOne.getPlugin().getConfig().getLong("Lobby.Yaw")));
		}
		players.remove(player);
		playercount = players.size();
		
		BungeeCordManager.connectPlayerToLobby(player);
		
		PlayerLeaveArenaEvent event = new PlayerLeaveArenaEvent(this, player);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
	}
	
	public void broadcastMessage(String message) {
		for(Player player : players) {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
			player.sendMessage(message);
			
		}
	}
	
	public static void deleteAndUnregisterArena(Arena arena) {	 //teleports players to Lobby and destroy the arena and unregister it and fire a Event	
		ArrayList<Player> list = new ArrayList<>(arena.getPlayers());
		for(Player player : list) {
			arena.removePlayer(player);
			player.sendMessage(Messages.PREFIX.getString() + Messages.TELEPORTTOLOBBY.getString());
			Bukkit.getServer().getPluginManager().callEvent(new PlayerLeaveArenaEvent(arena, player));
		}
		arena.getArenaMap().deleteMap();
		
		MySQLManager.deleteArena(arena.getArenaName()); //Purge Arena from Database
		
		ArrayList<Arena> arenalist = (ArrayList<Arena>) OneVsOne.getArena();
		arenalist.remove(arena);
		OneVsOne.setArena(arenalist);
		
	}
	
	public static void deleteAndUnregisterArenaForOnDisable(Arena arena) { //Just for OneVsOne.onDisable()
		ArrayList<Player> players = new ArrayList<>(arena.getPlayers());
		for(Player player : players) {
			arena.removePlayer(player);
			player.sendMessage(Messages.PREFIX.getString() + Messages.TELEPORTTOLOBBY.getString());
			Bukkit.getServer().getPluginManager().callEvent(new PlayerLeaveArenaEvent(arena, player));
		}
		
		arena.getArenaMap().deleteMap();
		
		ArrayList<Arena> arenalist = (ArrayList<Arena>) OneVsOne.getArena();
		arenalist.remove(arena);
		OneVsOne.setArena(arenalist);
		
	}
	
	public static Arena createAndRegisterArena(String mapName) { //create a arena (Name based on the amout of arenas) and register it in the OneVsOne Class
		Arena arena = new Arena(mapName);
		Collection<Arena> arenaCollection = OneVsOne.getArena();
		arenaCollection.add(arena);
		OneVsOne.setArena(new ArrayList<>(arenaCollection));
		MySQLManager.addArena(arena); //Add Arena to Database
		
		return arena;
	}
	
	public static Arena createAndRegisterArena() { //create a arena (Name based on the amout of arenas) and register it in the OneVsOne Class
		Arena arena = new Arena();
		Collection<Arena> arenaCollection = OneVsOne.getArena();
		arenaCollection.add(arena);
		OneVsOne.setArena(new ArrayList<>(arenaCollection));
		MySQLManager.addArena(arena); //Add Arena to Database
		
		return arena;
	}
	
	
	public static Arena getArena(@NotNull String arenaname) {
		for(Arena forlooparena : OneVsOne.getArena()) {
			if(forlooparena.getArenaName().equalsIgnoreCase(arenaname)) {
				return forlooparena;
			}
			else {
				continue;
			}
		}
		return null;
	}
	
	public List<Location> getSpawns(){
		List<Location> list = new ArrayList<>();
		list.add(arenamap.getSpawn1());
		list.add(arenamap.getSpawn2());
		return list;
	}
	
	public int getPlayerCount() {
		return playercount;
	}
	
	
	
	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		GameStateChangeEvent event = new GameStateChangeEvent(this, this.gameState, gameState);
		Bukkit.getPluginManager().callEvent(event);
		this.gameState = gameState;
	}
	
	public String getArenaName() {
		return arenaName;
	}

	public Collection<Player> getPlayers() {
		return players;
	}
	
	public ArenaMap getArenaMap() {
		return arenamap;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}	
}