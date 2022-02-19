package com.github.gamedipoxx.oneVsOne.utils.stats;

import java.util.HashMap;
import java.util.UUID;

public class StatsObject {
	private static HashMap<UUID, StatsObject> statsCach;
	private final UUID uuid;
	private final int gamesPlayed;
	private final int gamesWon;
	private final String playername;

	public StatsObject(final UUID uuid, final int gamesPlayed, final int gamesWon, final String playerName) {
		this.uuid = uuid;
		this.gamesPlayed = gamesPlayed;
		this.gamesWon = gamesWon;
		this.playername = playerName;
		
	}
	

	public static HashMap<UUID, StatsObject> getStatsCach() {
		return statsCach;
	}
	
	public static void setStatsCach(HashMap<UUID, StatsObject> statsCach) {
		StatsObject.statsCach = statsCach;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public int getGamesWon() {
		return gamesWon;
	}


	public String getPlayername() {
		return playername;
	}
}
