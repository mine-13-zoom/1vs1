package com.github.gamedipoxx.oneVsOne.listener;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.gamedipoxx.oneVsOne.Messages;
import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.events.PlayerJoinArenaEvent;
import com.github.gamedipoxx.oneVsOne.utils.GameState;

public class LeaveItem implements Listener {
	
	@EventHandler
	public void onPlayerDropEvent(PlayerDropItemEvent event) {
		if(event.getItemDrop().getItemStack().equals(getItem())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDragItem(InventoryClickEvent event) {
		if(!event.getCurrentItem().equals(getItem())) {
			return;
		}
		event.getCursor().setType(Material.AIR);
		event.setCancelled(true);
	}

	@EventHandler
	public void onClickEvent(PlayerInteractEvent event) {
		
		if(!event.getPlayer().getInventory().getItemInMainHand().equals(getItem())) {
			return;
		}
		
		Player player = event.getPlayer();
		player.sendMessage(Messages.PREFIX.getString() + Messages.TELEPORTTOLOBBY.getString());
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		ArrayList<Arena> list = new ArrayList<>(OneVsOne.getArena());
		for (Arena arena : list) {
			ArrayList<Player> players = new ArrayList<>(arena.getPlayers());
			for (Player fplayer : players) {
				if (fplayer == player) {
					arena.removePlayer(player);
				}
			}
		}

	}

	@EventHandler
	public void onPlayerJoinArena(PlayerJoinArenaEvent event) {
		if(event.getArena().getGameState() != GameState.STARTING) {
			return;
		}
		
		event.getPlayer().getInventory().setItem(8, getItem());
	}
	
	private static ItemStack getItem() {
		ItemStack item = new ItemStack(Material.MAGMA_CREAM, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Messages.LEAVEITEM.getString());
		item.setItemMeta(meta);
		return item;
	}

}
