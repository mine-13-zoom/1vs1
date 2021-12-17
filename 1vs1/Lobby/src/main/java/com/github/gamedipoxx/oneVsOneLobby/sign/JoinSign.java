package com.github.gamedipoxx.oneVsOneLobby.sign;

import java.util.Optional;

import com.github.gamedipoxx.oneVsOneLobby.LobbyMessages;
import com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location;

public class JoinSign {
	private Location location;
	private String line1;
	private String line4;
	private String arena;
	private int players;
	
	public JoinSign(Location location, String arenaName, int players, Optional<String> line1, Optional<String> line4){
		this.location = location;
		this.arena = arenaName;
		this.players = players;
		LobbyMessages = 
	}
}
