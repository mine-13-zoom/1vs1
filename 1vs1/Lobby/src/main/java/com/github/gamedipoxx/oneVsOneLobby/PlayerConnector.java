package com.github.gamedipoxx.oneVsOneLobby;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PlayerConnector{
	
	public PlayerConnector(Player player, String arena) {
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		  out.writeUTF("OneVsOneConnect");
		  out.writeUTF(player.getName());
		  out.writeUTF(arena);

		  player.sendPluginMessage(OneVsOneLobby.getPlugin(), "BungeeCord", out.toByteArray());
		  
	}
	
}
