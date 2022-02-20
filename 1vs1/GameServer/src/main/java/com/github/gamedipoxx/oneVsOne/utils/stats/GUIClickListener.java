package com.github.gamedipoxx.oneVsOne.utils.stats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class GUIClickListener implements Listener{
	
	private static List<Inventory> invs = new ArrayList<>();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void denyClick(InventoryClickEvent event) {
		if(invs.contains(event.getInventory())) {
			event.setCancelled(true);
		}
		if(event.getInventory().equals(GlobalStatsGUI.getGui())) {
			event.setCancelled(true);
		}
		if(event.getInventory().equals(MainStatsGUI.getInv())) {
			if(event.getCurrentItem() == null) {
				return;
			}
			if(event.getCurrentItem().equals(MainStatsGUI.getGlobalItem())) {
				event.getWhoClicked().openInventory(GlobalStatsGUI.getGui());
				event.setCancelled(true);
				return;
			}
			if(event.getCurrentItem().equals(MainStatsGUI.getPrivateItem())) {
				PrivateStatsGUI gui = new PrivateStatsGUI(Bukkit.getOfflinePlayer(event.getWhoClicked().getUniqueId()));
				gui.openForPlayer(Bukkit.getPlayer(event.getWhoClicked().getUniqueId()));
				event.setCancelled(true);
				return;
			}
		}
	}
	@EventHandler
	public void removeInvFromList(InventoryCloseEvent event) {
		if(!invs.contains(event.getInventory())) {
			return;
		}
		if(event.getInventory().getViewers().size() != 0) {
			return;
		}
		invs.remove(event.getInventory());
	}

	public static List<Inventory> getInvs() {
		return invs;
	}

	public static void setInvs(List<Inventory> invs) {
		GUIClickListener.invs = invs;
	}

}
