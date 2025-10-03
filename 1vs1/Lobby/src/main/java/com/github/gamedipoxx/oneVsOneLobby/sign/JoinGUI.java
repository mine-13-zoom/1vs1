package com.github.gamedipoxx.oneVsOneLobby.sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.gamedipoxx.oneVsOne.utils.SimpleArenaDatabaseObject;
import com.github.gamedipoxx.oneVsOneLobby.LobbyMessages;
import com.github.gamedipoxx.oneVsOneLobby.LobbySQLManager;
import com.github.gamedipoxx.oneVsOneLobby.OneVsOneLobby;

public class JoinGUI {
	private static JoinGUI joingui;
	private Inventory inventory;
	public static HashMap<Integer, SimpleArenaDatabaseObject> arenaMapping = new HashMap<>();

	private JoinGUI() {
		this.inventory = Bukkit.createInventory(null, 54, LobbyMessages.GUITITLE.getString());
	}
	
	public static void init() {
		int delay = OneVsOneLobby.getPlugin().getConfig().getInt("updatedelay");
		joingui = new JoinGUI();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(OneVsOneLobby.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				LobbySQLManager.fetchFromDatabase();
			}
		}, 20L, delay*20L);
	}
	
	public static void updateGui() {
		arenaMapping.clear();
		joingui.inventory.clear();
		ArrayList<SimpleArenaDatabaseObject> arenas = LobbySQLManager.getArenas();
		int i = 0;
		for(SimpleArenaDatabaseObject sado : arenas) {
			switch (sado.getGameState()) {
			case ENDING: {
				joingui.addItemToInv(sado, Material.RED_TERRACOTTA, i);
				break;
			}
			case INGAME: {
				joingui.addItemToInv(sado, Material.ORANGE_TERRACOTTA, i);
				break;
			}
			case STARTING: {
				joingui.addItemToInv(sado, Material.YELLOW_TERRACOTTA, i);
				break;
			}
			case WAITING: {
				joingui.addItemToInv(sado, Material.LIME_TERRACOTTA, i);
				break;
			}
			case UNDEFINED: {
				joingui.addItemToInv(sado, Material.GRAY_TERRACOTTA, i);
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + sado.getGameState());
			}
			arenaMapping.put(i, sado);
			i++;
		}
		for(HumanEntity humanEntity : joingui.inventory.getViewers()) {
			Bukkit.getPlayer(humanEntity.getUniqueId()).updateInventory();
		}
	}
	
	private void addItemToInv(SimpleArenaDatabaseObject sado, Material material, int index) {
		ItemStack itemstack = new ItemStack(Material.TERRACOTTA, 1);
		itemstack.setType(material);
		ItemMeta itemmeta = itemstack.getItemMeta();
		itemmeta.setDisplayName("§2");
		List<String> list = new ArrayList<String>();
		list.add(LobbyMessages.ARENA.getString());
		list.add("§7" + sado.getArenaName());
		list.add(" ");
		list.add(LobbyMessages.STATUS.getString());
		list.add("§7" + sado.getGameState().name());
		list.add(" ");
		list.add(LobbyMessages.KIT.getString());
		list.add("§7" + sado.getKit());
		list.add(" ");
		list.add(LobbyMessages.PLAYER.getString());
		list.add("§7" + sado.getPlayercount() + "§7/2");
		itemmeta.setLore(list);
		itemstack.setItemMeta(itemmeta);
		joingui.inventory.setItem(index, itemstack);
	}
	
	public static void openForPlayer(Player player) {
		player.openInventory(joingui.inventory);
	}
	
	public static JoinGUI getJoingui() {
		return joingui;
	}

	public static Inventory getInventory() {
		return joingui.inventory;
	}
}
