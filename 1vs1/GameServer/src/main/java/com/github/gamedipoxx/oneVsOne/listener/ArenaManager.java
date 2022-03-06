package com.github.gamedipoxx.oneVsOne.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.github.gamedipoxx.oneVsOne.ArenaSettings;
import com.github.gamedipoxx.oneVsOne.Messages;
import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.arena.GameCountDown;
import com.github.gamedipoxx.oneVsOne.arena.ScheduledArenaDelete;
import com.github.gamedipoxx.oneVsOne.events.GameStateChangeEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerJoinArenaEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerLeaveArenaEvent;
import com.github.gamedipoxx.oneVsOne.utils.GameState;
import com.github.gamedipoxx.oneVsOne.utils.MySQLManager;

public class ArenaManager implements Listener{
	
	@EventHandler
	public void playerJoinArenaEvent(PlayerJoinArenaEvent event) {
		ArrayList<Player> players = new ArrayList<>(BlockBreakOnStartingListener.getPlayers());
		players.add(event.getPlayer());
		BlockBreakOnStartingListener.setPlayers(players);
		Arena arena = event.getArena();
		//first Player join
		if(arena.getPlayerCount() == 1 && arena.getGameState() == GameState.WAITING) {
			arena.setGameState(GameState.STARTING);
		}
		//second player join
		if(arena.getPlayerCount() == 2 && arena.getGameState() == GameState.STARTING) {
			arena.setGameState(GameState.INGAME);
		}
	}
	@EventHandler
	public void playerLeftArenaEvent(PlayerLeaveArenaEvent event) {
		Arena arena = event.getArena();
		//Dont wanna wait anymore
		if(arena.getPlayerCount() == 1 && arena.getGameState() == GameState.STARTING) {
			arena.setGameState(GameState.WAITING);
			return;
		}
		else if(arena.getPlayerCount() == 0 && arena.getGameState() == GameState.STARTING) {
			arena.setGameState(GameState.WAITING);
		}
		//RageQuit
		if(arena.getGameState() == GameState.INGAME) {
			arena.setGameState(GameState.ENDING);
		}
		
	}
	@EventHandler
	public void onGameStateChangeEvent(GameStateChangeEvent event) {
		
		MySQLManager.updateArena(event.getArena());	//Update Database
		
		Arena arena = event.getArena();
		GameState before = event.getBefore();
		GameState after = event.getAfter();
		
		if(before == GameState.WAITING && after == GameState.STARTING) {
			arena.broadcastMessage(Messages.PREFIX.getString() + Messages.WAITINGFORMOREPLAYER.getString());
		}
		if(before == GameState.STARTING && after == GameState.INGAME) {
			arena.broadcastMessage(Messages.PREFIX.getString() + Messages.STARTINGGAME.getString());
			MySQLManager.increateGamesPlayedByArena(arena);
			for(Player player : arena.getPlayers()) {
				player.getInventory().clear();
				player.setHealth(20);
				player.setFoodLevel(20);
				for(PotionEffect effect : player.getActivePotionEffects()) {
					player.removePotionEffect(effect.getType());
				}
			}
			List<Location> spawns = arena.getSpawns();
			for(Player forplayer : arena.getPlayers()) {
				Location spawn = spawns.get(0);
				forplayer.teleport(spawn);
				spawns.remove(0);
			}
			new GameCountDown(arena);
			giveInv(arena);
			
			
		}
		if(after == GameState.ENDING) {
			for(Player player : arena.getPlayers()) {
				player.getInventory().clear();
				player.setHealth(20);
				player.setFoodLevel(20);
				for(PotionEffect effect : player.getActivePotionEffects()) {
					player.removePotionEffect(effect.getType());
				}
			}
			new ScheduledArenaDelete(arena);
			
		}
		
		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if(event.getEntity().getType() != EntityType.PLAYER) {
			return;
		}
		Player player = (Player) event.getEntity();
		ArrayList<Arena> tempArenaList = new ArrayList<>(OneVsOne.getArena());
		for(Arena arena : tempArenaList) {
			for(Player arenaplayer : arena.getPlayers()) {
				if(arenaplayer == player && arena.getGameState() == GameState.INGAME) {
					event.setDroppedExp(0);
					arena.setGameState(GameState.ENDING);
					arena.broadcastMessage(Messages.PREFIX.getString() + player.getDisplayName() + " " + Messages.PLAYERDIED.getString());
					for(Player winPlayer : arena.getPlayers()) {
						if(winPlayer != player) {
							arena.broadcastMessage(Messages.PREFIX.getString() + winPlayer.getDisplayName() + " " + Messages.PLAYERWIN.getString());
							MySQLManager.increateWinsBy1(winPlayer);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		leavePlayer(event.getPlayer());
	}
	
	public static void leavePlayer(Player quitPlayer) {
		ArrayList<Arena> templist = new ArrayList<>(OneVsOne.getArena());
		for(Arena arena : templist) {
			for(Player player : arena.getPlayers()) {
				if(quitPlayer == player) {
					arena.broadcastMessage(Messages.PREFIX.getString() + quitPlayer.getDisplayName() + " " + Messages.PLAYERLEAVEARENA.getString());
					arena.setGameState(GameState.ENDING);
				}
			}
		}
	}
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(ArenaSettings.DROP_ITEMS.getValue()) {
			return;
		}
		event.setCancelled(true);
	}
	@EventHandler
	public void onToolDamageEvent(PlayerItemDamageEvent event) {
		if(ArenaSettings.ITEM_DAMAGE.getValue()) {
			return;
		}
		event.setCancelled(true);
	}
	@EventHandler
	public void onPlayerPickUpItem(EntityPickupItemEvent event) {
		if(ArenaSettings.ITEM_PICKUP.getValue()) {
			return;
		}
		if(event.getEntity() instanceof Player) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(ArenaSettings.BLOCK_BREAKING.getValue()) {
			return;
		}
		if(event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerBreakFarmland(PlayerInteractEvent event) {
		if(ArenaSettings.BLOCK_BREAKING.getValue()) {
			return;
		}
		if(event.getAction() == Action.PHYSICAL) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerFallDamage(EntityDamageEvent event) {
		if(ArenaSettings.FALL_DAMAGE.getValue()) {
			return;
		}
		if(event.getCause() == DamageCause.FALL) {
			event.setCancelled(true);
		}
	}
	
	private void giveInv(Arena arena) {
		for(Player player : arena.getPlayers()) {
			player.getInventory().setContents(arena.getArenaMap().getInventory().toArray(new ItemStack[0]));
			player.getInventory().setArmorContents(arena.getArenaMap().getArmor().toArray(new ItemStack[0]));
		}
	}
	
	public static void createMaxArenas() {
		int arenas = OneVsOne.getPlugin().getConfig().getInt("Arenas");
		for(int i = 1; i <= arenas; i++) {
			Arena.createAndRegisterArena();
		}
	}
}
