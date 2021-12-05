package com.github.gamedipoxx.oneVsOne.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.gamedipoxx.oneVsOne.arena.Arena;

public class PlayerJoinArenaEvent extends Event{
	private static final HandlerList HANDLERS = new HandlerList();
	private Arena arena;
	private Player player;
	
	public PlayerJoinArenaEvent(Arena arena, Player player){
		this.player = player;
		this.arena = arena;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	public Arena getArena() {
		return arena;
	}
	public Player getPlayer() {
		return player;
	}

}
