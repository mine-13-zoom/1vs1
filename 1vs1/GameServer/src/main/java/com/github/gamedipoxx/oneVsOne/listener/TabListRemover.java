package com.github.gamedipoxx.oneVsOne.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.events.PlayerJoinArenaEvent;

public class TabListRemover implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		for (Player onlineplayer : Bukkit.getServer().getOnlinePlayers()) {
			onlineplayer.hidePlayer(OneVsOne.getPlugin(), player);
			player.hidePlayer(OneVsOne.getPlugin(), onlineplayer);
		}
	}

	@EventHandler
	public void onPlayerJoinArena(PlayerJoinArenaEvent event) {
		Player joinPlayer = event.getPlayer();
		for(Player arenaplayer : event.getArena().getPlayers()) {
			arenaplayer.showPlayer(OneVsOne.getPlugin(), joinPlayer);
			joinPlayer.showPlayer(OneVsOne.getPlugin(), arenaplayer);
		}
	}

}
