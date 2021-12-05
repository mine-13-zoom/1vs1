package com.github.gamedipoxx.oneVsOne.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.arena.GameState;

public class GameStateChangeEvent extends Event{
	private static final HandlerList HANDLERS = new HandlerList();
	private Arena arena;
	private GameState before;
	private GameState after;
	public GameStateChangeEvent(Arena arena, GameState before, GameState after){
		this.arena = arena;
		this.before = before;
		this.after = after;
	}
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	public Arena getArena() {
		return arena;
	}
	public GameState getBefore() {
		return before;
	}
	public void setBefore(GameState before) {
		this.before = before;
	}
	public GameState getAfter() {
		return after;
	}
	public void setAfter(GameState after) {
		this.after = after;
	}
	public void setArena(Arena arena) {
		this.arena = arena;
	}
}
