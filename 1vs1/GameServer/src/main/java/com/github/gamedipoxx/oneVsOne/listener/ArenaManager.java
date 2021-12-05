package com.github.gamedipoxx.oneVsOne.listener;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.github.gamedipoxx.oneVsOne.Messages;
import com.github.gamedipoxx.oneVsOne.MySQLManager;
import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.arena.GameCountDown;
import com.github.gamedipoxx.oneVsOne.arena.GameState;
import com.github.gamedipoxx.oneVsOne.arena.ScheduledArenaDelete;
import com.github.gamedipoxx.oneVsOne.events.GameStateChangeEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerJoinArenaEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerLeaveArenaEvent;

public class ArenaManager implements Listener{
	
	@EventHandler
	public void playerJoinArenaEvent(PlayerJoinArenaEvent event) {
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
		if(before == GameState.INGAME && after == GameState.ENDING) {
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
		Player player = (Player) event.getEntity();
		for(Arena arena : OneVsOne.getArena()) {
			for(Player arenaplayer : arena.getPlayers()) {
				if(arenaplayer == player && arena.getGameState() == GameState.INGAME) {
					event.setDeathMessage(" ");
					event.setKeepInventory(true);
					event.setDroppedExp(0);
					arena.setGameState(GameState.ENDING);
					arena.broadcastMessage(Messages.PREFIX.getString() + player.getDisplayName() + " " + Messages.PLAYERDIED.getString());
					for(Player winPlayer : arena.getPlayers()) {
						if(winPlayer != player) {
							arena.broadcastMessage(Messages.PREFIX.getString() + winPlayer.getDisplayName() + " " + Messages.PLAYERWIN.getString());
						}
					}
					player.spigot().respawn();
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player quitPlayer = event.getPlayer();
		event.setQuitMessage(" ");
		for(Arena arena : OneVsOne.getArena()) {
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
		event.setCancelled(true);
	}
	@EventHandler
	public void onToolDamageEvent(PlayerItemDamageEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onPlayerPickUpItem(EntityPickupItemEvent event) {
		if(event.getEntity() instanceof Player) {
			event.setCancelled(true);
		}
	}
	
	private void giveInv(Arena arena) {
		for(Player player : arena.getPlayers()) {
			for (Entry<Integer, ItemStack> entry : arena.getKit().getInv().entrySet()) {
			    Integer key = entry.getKey();
			    ItemStack value = entry.getValue();
			    player.getInventory().setItem(key, value);
			}
		}
	}
}



//cases:
//Kein Bock auf warten -> Starting -> Strting|Waiting
//RageQuit -> Ingame -> Ending
//Game zu ende -> Ending -> Ending
