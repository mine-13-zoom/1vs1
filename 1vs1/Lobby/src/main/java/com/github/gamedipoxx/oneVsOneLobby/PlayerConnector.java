package com.github.gamedipoxx.oneVsOneLobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.gamedipoxx.oneVsOne.utils.MySQLManager;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PlayerConnector {

	public PlayerConnector(Player player, String arena, String server) {

		MySQLManager.addPlayerToTeleport(player.getName(), arena, server);

		Bukkit.getScheduler().scheduleSyncDelayedTask(OneVsOneLobby.getPlugin(), () -> {

			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(server);

			player.sendPluginMessage(OneVsOneLobby.getPlugin(), "BungeeCord", out.toByteArray());
			
		}, 20);

	}

}
