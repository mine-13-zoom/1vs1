package com.github.gamedipoxx.oneVsOneLobby.sign;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.gamedipoxx.oneVsOne.utils.GameState;
import com.github.gamedipoxx.oneVsOne.utils.SimpleArenaDatabaseObject;
import com.github.gamedipoxx.oneVsOneLobby.LobbyMessages;
import com.github.gamedipoxx.oneVsOneLobby.LobbySQLManager;
import com.github.gamedipoxx.oneVsOneLobby.OneVsOneLobby;
import com.github.gamedipoxx.oneVsOneLobby.PlayerConnector;

public class InventoryClickListener implements Listener {
	private ArrayList<Player> blacklist = new ArrayList<>();
	@EventHandler
	public void onPlayerClickEvent(InventoryClickEvent event) {
		if(!event.getInventory().equals(JoinGUI.getInventory())){
			return;
		}
		event.setCancelled(true);
		Player playerwhoclicked = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());

		// Handle close button (slot 49)
		if(event.getSlot() == 49) {
			playerwhoclicked.closeInventory();
			return;
		}

		// Handle random arena button (slot 53)
		if(event.getSlot() == 53) {
			LobbySQLManager.fetchFromDatabase();
			ArrayList<SimpleArenaDatabaseObject> joinableArenas = new ArrayList<>();

			// Collect all joinable arenas (WAITING or STARTING state)
			for(SimpleArenaDatabaseObject arena : LobbySQLManager.getArenas()) {
				if(arena.getGameState() == GameState.WAITING || arena.getGameState() == GameState.STARTING) {
					joinableArenas.add(arena);
				}
			}

			if(joinableArenas.isEmpty()) {
				playerwhoclicked.sendMessage(LobbyMessages.PREFIX.getString() + "§cNo arenas available to join!");
				return;
			}

			if(blacklist.contains(playerwhoclicked)) {
				playerwhoclicked.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.PLEASEWAIT.getString());
				return;
			}

			// Pick random arena
			Random random = new Random();
			SimpleArenaDatabaseObject randomArena = joinableArenas.get(random.nextInt(joinableArenas.size()));

			new PlayerConnector(playerwhoclicked, randomArena.getArenaName(), randomArena.getServer());
			blacklist.add(playerwhoclicked);
			Bukkit.getScheduler().runTaskLater(OneVsOneLobby.getPlugin(), new Runnable() {

				private final Player player = playerwhoclicked;

				@Override
				public void run() {
					if(blacklist.contains(player)){
						blacklist.remove(player);
					}
				}
			}, 20*5);
			return;
		}

		LobbySQLManager.fetchFromDatabase();
		SimpleArenaDatabaseObject sado = JoinGUI.arenaMapping.get(event.getSlot());
		if(sado == null) {
			return;
		}
		if(sado.getGameState() == GameState.ENDING) {
			return;
		}
		if(sado.getGameState() == GameState.UNDEFINED) {
			return;
		}
		if(sado.getGameState() == GameState.INGAME) {
			return;
		}
		if(blacklist.contains(playerwhoclicked)) {
			playerwhoclicked.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.PLEASEWAIT.getString());
			return;
		}
		new PlayerConnector(playerwhoclicked, sado.getArenaName(), sado.getServer());
		blacklist.add(playerwhoclicked);
		Bukkit.getScheduler().runTaskLater(OneVsOneLobby.getPlugin(), new Runnable() {

			private final Player player = playerwhoclicked;

			@Override
			public void run() {
				if(blacklist.contains(player)){
					blacklist.remove(player);
				}
			}
		}, 20*5);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(blacklist.contains(event.getPlayer())) {
			blacklist.remove(event.getPlayer());
		}
		LobbySQLManager.fetchFromDatabase();
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(blacklist.contains(event.getPlayer())) {
			blacklist.remove(event.getPlayer());
		}
		LobbySQLManager.fetchFromDatabase();
	}
}
