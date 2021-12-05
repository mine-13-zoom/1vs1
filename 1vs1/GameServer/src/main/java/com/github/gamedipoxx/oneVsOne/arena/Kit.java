package com.github.gamedipoxx.oneVsOne.arena;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Kit {
	
	LEATHER(createLeather()),
	CROSSBOW(createCrossbow()),
	ENDER(createEnder()),
	IRON(createIron()),
	DIAMOND(createDiamond()),
	OP(createOp());
	
	private HashMap<Integer, ItemStack> inv;
	
	Kit(HashMap<Integer, ItemStack> inv){
		this.inv = inv;
	}

	public HashMap<Integer, ItemStack> getInv() {
		return inv;
	}

	public void setInv(HashMap<Integer, ItemStack> inv) {
		this.inv = inv;
	}
	
	public static Kit getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}
	
	private static HashMap<Integer, ItemStack> createLeather() {
		HashMap<Integer, ItemStack> inventory = new HashMap<>();
		inventory.put(0, new ItemStack(Material.STONE_SWORD));
		inventory.put(1, new ItemStack(Material.FISHING_ROD));
		inventory.put(2, new ItemStack(Material.BOW));
		inventory.put(7, new ItemStack(Material.ARROW, 10));
		inventory.put(8, new ItemStack(Material.COOKED_PORKCHOP, 10));
		inventory.put(36, new ItemStack(Material.LEATHER_BOOTS));
		inventory.put(37, new ItemStack(Material.LEATHER_LEGGINGS));
		inventory.put(38, new ItemStack(Material.LEATHER_CHESTPLATE));
		inventory.put(39, new ItemStack(Material.LEATHER_HELMET));
		return inventory;
	}
	
	private static HashMap<Integer, ItemStack> createCrossbow() {
		HashMap<Integer, ItemStack> inventory = new HashMap<>();
		ItemMeta meta = new ItemStack(Material.CROSSBOW).getItemMeta();
		meta.addEnchant(Enchantment.MULTISHOT, 3, true);
		meta.setUnbreakable(true);
		ItemStack crossbow = new ItemStack(Material.CROSSBOW);
		crossbow.setItemMeta(meta);
		inventory.put(0, new ItemStack(Material.WOODEN_SWORD));
		inventory.put(1, crossbow);
		inventory.put(2, crossbow);
		inventory.put(3, crossbow);
		inventory.put(8, new ItemStack(Material.COOKED_BEEF, 10));
		inventory.put(7, new ItemStack(Material.ARROW, 64));
		inventory.put(36, new ItemStack(Material.IRON_BOOTS));
		inventory.put(37, new ItemStack(Material.IRON_LEGGINGS));
		inventory.put(38, new ItemStack(Material.IRON_CHESTPLATE));
		inventory.put(39, new ItemStack(Material.IRON_HELMET));
		return inventory;
	}
	
	private static HashMap<Integer, ItemStack> createEnder() {
		HashMap<Integer, ItemStack> inventory = new HashMap<>();
		inventory.put(0, new ItemStack(Material.IRON_SWORD));
		inventory.put(1, new ItemStack(Material.ENDER_PEARL, 10));
		inventory.put(2, new ItemStack(Material.BOW));
		inventory.put(7, new ItemStack(Material.ARROW, 10));
		inventory.put(8, new ItemStack(Material.COOKED_PORKCHOP, 10));
		inventory.put(36, new ItemStack(Material.LEATHER_BOOTS));
		inventory.put(37, new ItemStack(Material.LEATHER_LEGGINGS));
		inventory.put(38, new ItemStack(Material.IRON_CHESTPLATE));
		inventory.put(39, new ItemStack(Material.LEATHER_HELMET));
		return inventory;
	}
	
	private static HashMap<Integer, ItemStack> createIron() {
		HashMap<Integer, ItemStack> inventory = new HashMap<>();
		inventory.put(0, new ItemStack(Material.IRON_SWORD));
		inventory.put(1, new ItemStack(Material.FISHING_ROD));
		inventory.put(2, new ItemStack(Material.BOW));
		inventory.put(7, new ItemStack(Material.ARROW, 10));
		inventory.put(8, new ItemStack(Material.COOKED_PORKCHOP, 10));
		inventory.put(36, new ItemStack(Material.IRON_BOOTS));
		inventory.put(37, new ItemStack(Material.IRON_LEGGINGS));
		inventory.put(38, new ItemStack(Material.IRON_CHESTPLATE));
		inventory.put(39, new ItemStack(Material.IRON_HELMET));
		return inventory;
	}
	
	private static HashMap<Integer, ItemStack> createDiamond() {
		HashMap<Integer, ItemStack> inventory = new HashMap<>();
		inventory.put(0, new ItemStack(Material.DIAMOND_SWORD));
		inventory.put(1, new ItemStack(Material.FISHING_ROD));
		inventory.put(2, new ItemStack(Material.BOW));
		inventory.put(7, new ItemStack(Material.ARROW, 10));
		inventory.put(8, new ItemStack(Material.COOKED_PORKCHOP, 10));
		inventory.put(36, new ItemStack(Material.DIAMOND_BOOTS));
		inventory.put(37, new ItemStack(Material.DIAMOND_LEGGINGS));
		inventory.put(38, new ItemStack(Material.DIAMOND_CHESTPLATE));
		inventory.put(39, new ItemStack(Material.DIAMOND_HELMET));
		return inventory;
	}
	
	private static HashMap<Integer, ItemStack> createOp() {
		HashMap<Integer, ItemStack> inventory = new HashMap<>();
		inventory.put(0, new ItemStack(Material.NETHERITE_SWORD));
		inventory.put(1, new ItemStack(Material.FISHING_ROD));
		inventory.put(2, new ItemStack(Material.BOW));
		inventory.put(7, new ItemStack(Material.ARROW, 64));
		inventory.put(8, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 5));
		inventory.put(36, new ItemStack(Material.NETHERITE_BOOTS));
		inventory.put(37, new ItemStack(Material.NETHERITE_LEGGINGS));
		inventory.put(38, new ItemStack(Material.NETHERITE_CHESTPLATE));
		inventory.put(39, new ItemStack(Material.NETHERITE_HELMET));
		return inventory;
	}
}
