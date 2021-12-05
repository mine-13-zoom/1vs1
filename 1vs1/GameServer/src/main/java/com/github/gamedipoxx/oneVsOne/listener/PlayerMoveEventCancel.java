package com.github.gamedipoxx.oneVsOne.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.gamedipoxx.oneVsOne.arena.GameCountDown;

public class PlayerMoveEventCancel implements Listener {
	@EventHandler
	public void onEvent(PlayerMoveEvent event) {
		List<Player> players = GameCountDown.getPlayers();
		if(players == null) {
			return;
		}
		for (Player player : players) {
			if (player == event.getPlayer()) {
				Location from = event.getFrom();
				Location to = event.getTo();
				
				if(from.getX() != to.getX()) {
					event.setCancelled(true);
				}
				if(from.getZ() != to.getZ()) {
					event.setCancelled(true);
				}
			}
		}

	}
	@EventHandler
	public void onPlayerShootArrow(EntityShootBowEvent event) {
		if(event.getEntity() instanceof Player == false) {
			return;
		}
		List<Player> players = GameCountDown.getPlayers();
		if(players == null) {
			return;
		}
		for (Player player : players) {
			if (player == (Player) event.getEntity()) {
				event.setCancelled(true);
			}
		}
	}
}
