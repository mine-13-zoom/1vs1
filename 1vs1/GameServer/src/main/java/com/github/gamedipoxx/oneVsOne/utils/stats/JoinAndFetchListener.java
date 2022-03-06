package com.github.gamedipoxx.oneVsOne.utils.stats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.gamedipoxx.oneVsOne.utils.MySQLManager;

public class JoinAndFetchListener implements Listener{
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		MySQLManager.updateStats();
		MySQLManager.updateTopPlayers();
	}
}
