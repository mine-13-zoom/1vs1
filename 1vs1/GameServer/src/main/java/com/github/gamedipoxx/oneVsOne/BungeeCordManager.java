package com.github.gamedipoxx.oneVsOne;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeCordManager {
	public static void connectPlayerToLobby(Player player) {
		
		if(!OneVsOne.getPlugin().isEnabled()) {
			if(player.isOnline()) {
				player.kickPlayer("§cServer is stopping");
			}
			return;
		}
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(OneVsOne.getPlugin().getConfig().getString("LobbyServer"));

		player.sendPluginMessage(OneVsOne.getPlugin(), "BungeeCord", out.toByteArray());
	}
}
