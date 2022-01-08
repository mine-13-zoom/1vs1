package com.github.gamedipoxx.oneVsOneLobby.sign;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.gamedipoxx.oneVsOneLobby.LobbySQLManager;

public class JoinAndLeaveListener implements Listener {
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		LobbySQLManager.fetchFromDatabase();
	}
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		LobbySQLManager.fetchFromDatabase();
	}
}
