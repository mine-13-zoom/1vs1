package com.github.gamedipoxx.oneVsOneBungee;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OneVsOneGetMessage implements Listener{

	@EventHandler
	public void onPluginMessage(PluginMessageEvent event) {
		if (event.getTag() != "BungeeCord") {
			return;
		}
		if (event.getSender() instanceof Server == false) {
			return;
		}
		DataInput input = new DataInputStream(new ByteArrayInputStream(event.getData()));
		String subchannel;
		String player;
		try {
			subchannel = input.readUTF();
			player = input.readUTF();;
		} catch (IOException e) {
			OneVsOneBungee.getPlugin().getLogger().warning("Cann't read Message");
			return;
		}
		if(subchannel.equalsIgnoreCase("OneVsOneGet") == false) {
			return;
		}

		if (ProxyServer.getInstance().getPlayer(player) == null) {
			return;
		}
		
		ProxiedPlayer pplayer = ProxyServer.getInstance().getPlayer(player);
		HashMap<ProxiedPlayer, String> map = OneVsOneBungee.getPlayerArenaMap();
		String arena = map.get(pplayer);
		map.remove(pplayer);
		OneVsOneBungee.setPlayerArenaMap(map);
		
		if(event.getSender() instanceof Server) {
			Server server = (Server) event.getSender();
			  ByteArrayDataOutput out = ByteStreams.newDataOutput();
			  out.writeUTF("OneVsOneGet");
			  out.writeUTF(player);
			  if(arena == null) {
				  out.writeUTF("null");
			  }
			  else {
				  out.writeUTF(arena);
			  }
			  
			server.sendData("BungeeCord", out.toByteArray());
		}

	}
	
	
}
