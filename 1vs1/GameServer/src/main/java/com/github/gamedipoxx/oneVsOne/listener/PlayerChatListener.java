package com.github.gamedipoxx.oneVsOne.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;

public class PlayerChatListener implements Listener {
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		
		event.getRecipients().clear();
		
		for(Arena arena : OneVsOne.getArena()) {
			if(arena.getPlayers().contains(event.getPlayer())) {
				event.getRecipients().addAll(arena.getPlayers());
			}
		}
		
	}

}
