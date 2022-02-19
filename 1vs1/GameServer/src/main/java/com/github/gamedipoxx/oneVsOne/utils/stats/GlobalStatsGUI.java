package com.github.gamedipoxx.oneVsOne.utils.stats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalStatsGUI {
	private static Inventory gui = Bukkit.createInventory(null, 54, "§eStats");
	private static JavaPlugin plugin;
	private static List<StatsObject> topPlayers;
	private static String gamesPlayed;
	private static String gamesWon;
	private static String gamesLost;
	
	public static void updateGui() {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				gui.clear();
				if(topPlayers == null || topPlayers.size() == 0) {
					gui.setItem(31, errorItem());
					return;
				}
				ArrayList<StatsObject> top = new ArrayList<>(topPlayers);
				for(int i = 0; i < top.size(); i ++) {
					if(i == 54) {
						break;
					}
					gui.setItem(i, getPlayerHead(top.get(i)));
				}	
			}
		});
	}

	public static void setPlugin(JavaPlugin plugin) {
		GlobalStatsGUI.plugin = plugin;
	}
	
	private static ItemStack getPlayerHead(StatsObject stats) {
		if(stats == null) {
			return errorItem();
		}
		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setDisplayName(stats.getPlayername());
		List<String> lore = new ArrayList<>();
		int gamesPlayedint = stats.getGamesPlayed();
		int gamesWonint = stats.getGamesWon();
		int gamesLostint = gamesPlayedint - gamesWonint;
		lore.add(" ");
		lore.add(gamesPlayed);
		lore.add("§7" + gamesPlayedint);
		lore.add(" ");
		lore.add(gamesWon);
		lore.add("§7" + gamesWonint);
		lore.add(" ");
		lore.add(gamesLost);
		lore.add("§7" + gamesLostint);
		lore.add(" ");
		meta.setLore(lore);
		meta.setUnbreakable(true);
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(stats.getUuid()));
		head.setItemMeta(meta);
		return head;
	}
	
	

	public static void setGamesPlayed(String gamesPlayed) {
		GlobalStatsGUI.gamesPlayed = gamesPlayed;
	}

	public static void setGamesWon(String gamesWon) {
		GlobalStatsGUI.gamesWon = gamesWon;
	}

	public static void setGamesLost(String gamesLost) {
		GlobalStatsGUI.gamesLost = gamesLost;
	}

	public static Inventory getGui() {
		return gui;
	}
	
	public static void setTopPlayers(List<StatsObject> list){
		topPlayers = list;
	}
	
	private static ItemStack errorItem() {
		ItemStack error = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		ItemMeta meta = error.getItemMeta();
		meta.setDisplayName("§cNo Data");
		error.setItemMeta(meta);
		return error;
	}
}
