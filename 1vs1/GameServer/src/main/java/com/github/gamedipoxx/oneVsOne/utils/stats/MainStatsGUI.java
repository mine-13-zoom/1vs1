package com.github.gamedipoxx.oneVsOne.utils.stats;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainStatsGUI {
	private static Inventory inv;
	private static String globalStats;
	private static String privateStats;

	private static void fillInv() {
		inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§e§lStats");
		inv.setItem(1, getPrivateItem());
		inv.setItem(3, getGlobalItem());
	}
	
	public static ItemStack getGlobalItem() {
		ItemStack item = new ItemStack(Material.SPYGLASS, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(globalStats);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getPrivateItem() {
		ItemStack item = new ItemStack(Material.CLOCK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(privateStats);
		item.setItemMeta(meta);
		return item;
	}
	
	public static Inventory getInv() {
		if(inv == null) {
			fillInv();
		}
		return inv;
	}

	public static void setGlobalStats(String globalStats) {
		MainStatsGUI.globalStats = globalStats;
	}

	public static void setPrivateStats(String privateStats) {
		MainStatsGUI.privateStats = privateStats;
	}

}
