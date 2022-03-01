package com.github.gamedipoxx.oneVsOne.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;

public class PlaceHolders {
	//Instance variables
	private Arena arena;
	
	//placeholders
	private String player1name;
	private String player2name;
	private String player1hp;
	private String player2hp;
	private String arenaname;
	private String arenaKit;
	private String gameState;
	private String version;
	private String server;
	
	public PlaceHolders(Arena arena) {
		this.arena = arena;
		version = OneVsOne.getPlugin().getDescription().getVersion();
		server = OneVsOne.getServername();
	}
	
	public void update(UpdateablePlaceholders placeholder) {
		List<Player> playerlist = new ArrayList<>(arena.getPlayers());
		switch (placeholder) {
		case ARENAKIT:
			arenaKit =  arena.getArenaMap().getKitName();
		case ARENANAME:
			arenaname = arena.getArenaName();
		case GAMESTATE:
			gameState = arena.getGameState().name();
		case PLAYER_1_HP:	
			if(playerlist.size() >= 1) {
				player1hp = "" + (int) playerlist.get(0).getHealth();
			}
			else {
				player1hp = "x";
			}
			break;
		case PLAYER_1_NAME:
			if(playerlist.size() >= 1) {
				player1name = "" + playerlist.get(0).getDisplayName();
			}
			else {
				player1name = "x";
			}
			break;
		case PLAYER_2_HP:
			if(playerlist.size() >= 2) {
				player2hp = "" + (int) playerlist.get(1).getHealth();
			}
			else {
				player2hp = "x";
			}
			break;
		case PLAYER_2_NAME:
			if(playerlist.size() >= 2) {
				player2name = "" + playerlist.get(1).getDisplayName();
			}
			else {
				player2name = "x";
			}
			break;
		default:
			break;
		};
	}

	public String getPlayer1name() {
		return player1name;
	}

	public String getPlayer2name() {
		return player2name;
	}

	public String getPlayer1hp() {
		return player1hp;
	}

	public String getPlayer2hp() {
		return player2hp;
	}

	public String getArenaname() {
		return arenaname;
	}

	public String getArenaKit() {
		return arenaKit;
	}

	public String getGameState() {
		return gameState;
	}

	public String getVersion() {
		return version;
	}

	public String getServer() {
		return server;
	}
	
}

/* PlaceHolders
 * player1 name
 * player2 name
 * arena name
 * arena kit
 * player1 hp
 * player2 hp
 * gamestate
 * version
 * server
 */

