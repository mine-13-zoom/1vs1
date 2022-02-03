package com.github.gamedipoxx.oneVsOne.arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.gamedipoxx.oneVsOne.BungeeCordManager;
import com.github.gamedipoxx.oneVsOne.OneVsOne;

public class ScheduledArenaDelete {
	public ScheduledArenaDelete(Arena arena) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(OneVsOne.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				for(Player player : arena.getPlayers()) {
					BungeeCordManager.connectPlayerToLobby(player);
				}
				Arena.deleteAndUnregisterArena(arena);
				
			}
		}, 20*5);
		
		Arena.createAndRegisterArena();
	}
}
