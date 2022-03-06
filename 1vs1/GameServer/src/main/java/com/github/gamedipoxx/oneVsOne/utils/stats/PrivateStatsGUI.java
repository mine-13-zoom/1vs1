package com.github.gamedipoxx.oneVsOne.utils.stats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PrivateStatsGUI {
	private static String gamesPlayed;
	private static String gamesWon;
	private static String gamesLost;

	private Inventory gui;
	private StatsObject stats;
	
	public PrivateStatsGUI(OfflinePlayer player) {
		if(player == null) {
			gui = Bukkit.createInventory(null, InventoryType.HOPPER, "§4§lERROR");
			ItemStack erroritem = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
			ItemMeta meta = erroritem.getItemMeta();
			meta.setDisplayName("§4§lNo data found!");
			erroritem.setItemMeta(meta);
			gui.setItem(2, erroritem);
			return;
		}
		stats = StatsObject.getStatsCach().get(player.getUniqueId());
		gui = Bukkit.createInventory(null, InventoryType.HOPPER, "§e" + stats.getPlayername());
		fillInv();
		
		List<Inventory> invs = new ArrayList<>(GUIClickListener.getInvs());
		invs.add(gui);
		GUIClickListener.setInvs(invs);
	}
	
	public void openForPlayer(Player player) {
		player.openInventory(gui);
	}
	
	private void fillInv() {
		gui.setItem(0, getGamesPlayedItem());
		gui.setItem(2, getGamesWonItem());
		gui.setItem(4, getGamesLostItem());
				
	}
	
	private ItemStack getGamesPlayedItem() {
		ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(PrivateStatsGUI.gamesPlayed);
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("§7" + stats.getGamesPlayed());
		lore.add(" ");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getGamesWonItem() {
		ItemStack item = new ItemStack(Material.GOLDEN_SWORD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(PrivateStatsGUI.gamesWon);
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("§7" + stats.getGamesWon());
		lore.add(" ");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack getGamesLostItem() {
		ItemStack item = new ItemStack(Material.WOODEN_SWORD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(PrivateStatsGUI.gamesLost);
		List<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add("§7" + (stats.getGamesPlayed() - stats.getGamesWon()));
		lore.add(" ");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}
	
	public static void setGamesPlayed(String gamesPlayed) {
		PrivateStatsGUI.gamesPlayed = gamesPlayed;
	}

	public static void setGamesWon(String gamesWon) {
		PrivateStatsGUI.gamesWon = gamesWon;
	}

	public static void setGamesLost(String gamesLost) {
		PrivateStatsGUI.gamesLost = gamesLost;
	}
}
