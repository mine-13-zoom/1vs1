package com.github.gamedipoxx.oneVsOne.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.gamedipoxx.oneVsOne.BungeeCordManager;
import com.github.gamedipoxx.oneVsOne.Messages;
import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.utils.MySQLManager;

public class PlayerJoinListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();
		player.teleport(new Location(Bukkit.getWorld(OneVsOne.getPlugin().getConfig().getString("Lobby.World")), OneVsOne.getPlugin().getConfig().getInt("Lobby.X"), OneVsOne.getPlugin().getConfig().getInt("Lobby.Y"), OneVsOne.getPlugin().getConfig().getInt("Lobby.Z"), OneVsOne.getPlugin().getConfig().getLong("Lobby.Pitch"), OneVsOne.getPlugin().getConfig().getLong("Lobby.Yaw")));
		player.sendTitle(Messages.LOADDATATITLE.getString(), null, 1, 20, 20);
		player.setFireTicks(0);
		Bukkit.getScheduler().runTaskAsynchronously(OneVsOne.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				String arenaname = MySQLManager.readPlayerTeleport(player);
				
				if(arenaname == null) {
					runSync(player, null);
					return;
				}
				MySQLManager.deletePlayerFromTeleport(player.getName());
				for(Arena arena : OneVsOne.getArena()) {
					if(arena.getArenaName().equalsIgnoreCase(arenaname)) {
						runSync(player, arena);
						return;
					}
				}
				
			}
		});
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(OneVsOne.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if(player.getWorld().getName() != OneVsOne.getPlugin().getConfig().getString("Lobby.World")) {
					return;
				}
				if(player.hasPermission("*")) {
					player.sendMessage("§cError while loading data! Please check the console or database!");
					return;
				}
				else {
					BungeeCordManager.connectPlayerToLobby(player);
					return;
				}
				
			}
		}, 6*20);
	}
	
	private void runSync(Player player, Arena arena) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(OneVsOne.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if(arena != null) {
					arena.joinPlayer(player);
				}
				else {
					player.sendMessage("§cError while loading data! Please check the console or database!");
				}
				
			}
		});
	}
}
