package com.github.gamedipoxx.oneVsOne.arena;

import org.bukkit.Bukkit;

import com.github.gamedipoxx.oneVsOne.OneVsOne;

public class ScheduledArenaDelete {
	public ScheduledArenaDelete(Arena arena) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(OneVsOne.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				Arena.deleteAndUnregisterArena(arena);
				
			}
		}, 20*5);
	}
}
