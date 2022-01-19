package com.github.gamedipoxx.oneVsOne.utils;

public class SimpleArenaDatabaseObject {
	private String arenaName;
	private int playercount;
	private GameState gameState;
	private String kit;
	
	public SimpleArenaDatabaseObject(String arenaName, int playerCount, GameState gameState, String kit) {
		this.arenaName = arenaName;
		this.playercount = playerCount;
		this.gameState = gameState;
		this.kit = kit;
	}

	public String getArenaName() {
		return arenaName;
	}

	public int getPlayercount() {
		return playercount;
	}

	public GameState getGameState() {
		return gameState;
	}

	public String getKit() {
		return kit;
	}
	
}
