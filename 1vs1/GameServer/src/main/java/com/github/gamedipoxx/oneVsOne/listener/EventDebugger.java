package com.github.gamedipoxx.oneVsOne.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.gamedipoxx.oneVsOne.events.GameStateChangeEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerJoinArenaEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerLeaveArenaEvent;

public class EventDebugger implements Listener{
	@EventHandler
	public void onEventArenaJoin(PlayerJoinArenaEvent event) {
		String debugmsg = "§cDebug: " + event.getArena().getArenaName() + " | " + event.getPlayer().getDisplayName();
		Bukkit.broadcastMessage(debugmsg);
	}
	@EventHandler
	public void onEventArenaLeave(PlayerLeaveArenaEvent event) {
		String debugmsg = "§cDebug: " + event.getArena().getArenaName() + " | " + event.getPlayer().getDisplayName();
		Bukkit.broadcastMessage(debugmsg);
	}
	@EventHandler
	public void onEventGameStateChange(GameStateChangeEvent event) {
		String debugmsg = "§cDebug: " + event.getArena().getArenaName() + " | " + event.getBefore() + " | " + event.getAfter();
		Bukkit.broadcastMessage(debugmsg);
	}
}
