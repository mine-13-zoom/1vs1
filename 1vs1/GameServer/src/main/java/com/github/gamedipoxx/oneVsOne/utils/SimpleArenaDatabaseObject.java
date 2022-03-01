package com.github.gamedipoxx.oneVsOne.utils;

public class SimpleArenaDatabaseObject {
	private String arenaName;
	private int playercount;
	private GameState gameState;
	private String kit;
	private String server;
	
	public SimpleArenaDatabaseObject(String arenaName, int playerCount, GameState gameState, String kit, String server) {
		this.arenaName = arenaName;
		this.playercount = playerCount;
		this.gameState = gameState;
		this.kit = kit;
		this.server = server;
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

	public String getServer() {
		return server;
	}
	
}
