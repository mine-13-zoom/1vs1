package com.github.gamedipoxx.oneVsOne.arena;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

public class ArenaMap {

	private String worldName;
	private String templateWorldName;
	private World arenaWorld;
	private String kitName;
	private HashMap<Integer, ItemStack> inventory;
	private Location spawn1;
	private Location spawn2;
	private FileConfiguration arenaMapConfig;
	private String uuid;
	private MVWorldManager worldmanager;

	public ArenaMap(String templateWorldName, String uuid) {

		this.uuid = uuid;

		// Config Stuff
		loadConfig();

		// create the world and set gamerules
		createWorld();

		// Load Stuff from Config
		loadDataFromConfig();
	}

	private void loadDataFromConfig() {
		double spawn1x = arenaMapConfig.getDouble("Spawn1.X");
		double spawn1y = arenaMapConfig.getDouble("Spawn1.Y");
		double spawn1z = arenaMapConfig.getDouble("Spawn1.Z");
		long spawn1pitch = arenaMapConfig.getLong("Spawn1.Pitch");
		long spawn1yaw = arenaMapConfig.getLong("Spawn1.Yaw");
		spawn1 = new Location(arenaWorld, spawn1x, spawn1y, spawn1z, spawn1yaw, spawn1pitch);

		double spawn2x = arenaMapConfig.getDouble("Spawn2.X");
		double spawn2y = arenaMapConfig.getDouble("Spawn2.Y");
		double spawn2z = arenaMapConfig.getDouble("Spawn2.Z");
		long spawn2pitch = arenaMapConfig.getLong("Spawn2.Pitch");
		long spawn2yaw = arenaMapConfig.getLong("Spawn2.Yaw");
		spawn2 = new Location(arenaWorld, spawn2x, spawn2y, spawn2z, spawn2yaw, spawn2pitch);

		
	}

	private void createWorld() {
		worldName = uuid;
		worldmanager = OneVsOne.getMultiversecore().getMVWorldManager(); // set Multiverse wolrdmanager
		worldmanager.cloneWorld(templateWorldName, worldName); // clone world
		arenaWorld = Bukkit.getWorld(worldName);
		arenaWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		arenaWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		arenaWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		arenaWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		arenaWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
		arenaWorld.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
	}

	private void loadConfig() {
		arenaMapConfig = new YamlConfiguration();
		try {
			File configFile = new File(OneVsOne.getPlugin().getDataFolder(), templateWorldName + ".yml");
			arenaMapConfig.load(configFile);

		} catch (Exception e) {
			OneVsOne.getPlugin().getLogger().warning("No Config for world '" + worldName + "' found!");
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(OneVsOne.getPlugin());
		}
	}

	public String getWorldName() {
		return worldName;
	}

	public String getKitName() {
		return kitName;
	}

	public HashMap<Integer, ItemStack> getInventory() {
		return inventory;
	}

	public Location getSpawn1() {
		return spawn1;
	}

	public Location getSpawn2() {
		return spawn2;
	}

}
