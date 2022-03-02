package com.github.gamedipoxx.oneVsOne.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.gamedipoxx.oneVsOne.events.PlayerLeaveArenaEvent;

public class BlockBreakOnStartingListener implements Listener{
	private static List<Player> players = new ArrayList<>();
	
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event){
		if(event.getPlayer() == null) {
			return;
		}
		if(!players.contains(event.getPlayer())) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event){
		if(event.getPlayer() == null) {
			return;
		}
		if(!players.contains(event.getPlayer())) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPlaceBlock(PlayerInteractEvent event){
		if(event.getPlayer() == null) {
			return;
		}
		if(!players.contains(event.getPlayer())) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){
		if(event.getEntity()== null) {
			return;
		}
		if(event.getEntity() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity();
		if(!players.contains(player)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerLeaveArenaEvent event) {
		players.remove(event.getPlayer());
	}
	
	public static List<Player> getPlayers() {
		return players;
	}

	public static void setPlayers(List<Player> players) {
		BlockBreakOnStartingListener.players = players;
	}

}
