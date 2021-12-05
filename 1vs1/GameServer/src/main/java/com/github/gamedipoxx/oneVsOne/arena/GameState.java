package com.github.gamedipoxx.oneVsOne.arena;

public enum GameState {

	WAITING, // 0 Player, Arena wait for any player
	STARTING, // 1 Player, Arena wait for an other Player
	INGAME, // Both players are playing
	ENDING, // Both players are done with fighting and will be teleported to the Lobby
	UNDEFINED // The Arena doesnt know its state

}
