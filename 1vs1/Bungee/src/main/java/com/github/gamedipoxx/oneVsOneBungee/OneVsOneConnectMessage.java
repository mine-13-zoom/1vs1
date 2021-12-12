package com.github.gamedipoxx.oneVsOneBungee;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OneVsOneConnectMessage implements Listener {
	
	@EventHandler
	public void onMessage(PluginMessageEvent event) {
		if (event.getTag() != "BungeeCord") {
			return;
		}
		if (event.getSender() instanceof Server == false) {
			return;
		}
		DataInput input = new DataInputStream(new ByteArrayInputStream(event.getData()));
		String subchannel;
		String player;
		String arena;
		try {
			subchannel = input.readUTF();
			player = input.readUTF();
			arena = input.readUTF();
		} catch (IOException e) {
			OneVsOneBungee.getPlugin().getLogger().warning("Could not read message");
			return;
		}
		
		if(subchannel.equalsIgnoreCase("OneVsOneConnect") == false) {
			return;
		}

		if (ProxyServer.getInstance().getPlayer(player) == null) {
			return;
		}
		
		ProxiedPlayer pplayer = ProxyServer.getInstance().getPlayer(player);
		HashMap<ProxiedPlayer, String> map = OneVsOneBungee.getPlayerArenaMap();
		map.put(pplayer, arena);
		OneVsOneBungee.setPlayerArenaMap(map);
		ServerInfo server = ProxyServer.getInstance().getServerInfo(OneVsOneBungee.getConfiguration().getString("GameServer"));
		pplayer.connect(server);
	}

}
