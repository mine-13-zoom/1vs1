package com.github.gamedipoxx.oneVsOne.arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.gamedipoxx.oneVsOne.BungeeCordManager;
import com.github.gamedipoxx.oneVsOne.OneVsOne;

public class ScheduledArenaDelete {
	public ScheduledArenaDelete(Arena arena) {
		String mapName = arena.getArenaMap().getTemplateWorldName();
		String kitName = arena.getArenaMap().getKitName();
		
		if(OneVsOne.getPlugin().getConfig().getBoolean("debug")) {
			OneVsOne.getPlugin().getLogger().info("ScheduledArenaDelete: Creating new arena with mapName=" + mapName + ", kitName=" + kitName);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(OneVsOne.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				for(Player player : arena.getPlayers()) {
					BungeeCordManager.connectPlayerToLobby(player);
				}
				Arena.deleteAndUnregisterArena(arena);
				
			}
		}, 20*5);
		
		Arena.createAndRegisterArena(mapName);
	}
}
