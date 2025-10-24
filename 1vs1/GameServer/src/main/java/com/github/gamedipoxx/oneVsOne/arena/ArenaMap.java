package com.github.gamedipoxx.oneVsOne.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.github.gamedipoxx.oneVsOne.OneVsOne;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.WorldManager;
import org.mvplugins.multiverse.core.world.LoadedMultiverseWorld;
import org.mvplugins.multiverse.core.world.MultiverseWorld;

import org.mvplugins.multiverse.core.world.options.CloneWorldOptions;
import org.mvplugins.multiverse.core.world.options.DeleteWorldOptions;
import org.mvplugins.multiverse.core.world.options.LoadWorldOptions;
import org.mvplugins.multiverse.external.vavr.control.Option;

public class ArenaMap {

	private String worldName;
	private String templateWorldName;
	private World arenaWorld;
	private String kitName;
	private List<ItemStack> inventory;
	private List<ItemStack> armor;
	private Location spawn1;
	private Location spawn2;
	private FileConfiguration arenaMapConfig;
	private String uuid;
	private WorldManager worldManager;
	private static List<String> maps;

	public ArenaMap(String templateWorldName, String uuid) {
		
		if(OneVsOne.getPlugin().getConfig().getBoolean("debug")) {
			OneVsOne.getPlugin().getLogger().info("Trying to load Map " + templateWorldName + " with uuid " + uuid);
		}

		this.uuid = uuid;
		this.templateWorldName = templateWorldName;

		// Config Stuff
		loadConfig();

		// create the world and set gamerules
		createWorld();
	}
	
	public void deleteMap() {
		OneVsOne.getMultiversecore().getWorldManager().getWorld(worldName)
			.peek(world -> {
				OneVsOne.getMultiversecore().getWorldManager().deleteWorld(DeleteWorldOptions.world(world))
					.onFailure(failure -> {
						OneVsOne.getPlugin().getLogger().warning("Failed to delete world " + worldName + ": " + failure.getFailureMessage());
					})
					.onSuccess(deletedWorld -> {
						OneVsOne.getPlugin().getLogger().info("Successfully deleted world " + worldName);
					});
			})
			.onEmpty(() -> {
				OneVsOne.getPlugin().getLogger().warning("World " + worldName + " not found for deletion");
			});
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
		
		kitName = arenaMapConfig.getString("Kit.Name");
		
		loadKit();

	}

	private void loadKit() {
		armor = new ArrayList<>();
		inventory = new ArrayList<>();
		for(Object obj : arenaMapConfig.getList("Kit.Contens")){
			ItemStack item = (ItemStack) obj;
			inventory.add(item);
		}
		for(Object obj : arenaMapConfig.getList("Kit.Armor")){
			ItemStack item = (ItemStack) obj;
			armor.add(item);
		}
	}

  	private void createWorld() {
   		worldName = uuid;
   		worldManager = OneVsOne.getMultiversecore().getWorldManager(); // set Multiverse world manager
   		// Ensure template world is loaded before cloning
		Option<LoadedMultiverseWorld> loadedOpt = worldManager.getLoadedWorld(templateWorldName);
		if (loadedOpt.isDefined()) {
   			CloneWorldOptions options = CloneWorldOptions.fromTo(loadedOpt.get(), worldName)
   				.saveBukkitWorld(true);
   			worldManager.cloneWorld(options)
   				.onSuccess(newWorld -> {
   					arenaWorld = newWorld.getBukkitWorld().getOrNull();
   					if (arenaWorld != null) {
   						arenaWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
   						arenaWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
   						arenaWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
   						arenaWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
   						arenaWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
   						arenaWorld.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
   						// Load data from config now that world is ready
   						loadDataFromConfig();
   					}
   				})
   				.onFailure(failure -> {
   					OneVsOne.getPlugin().getLogger().severe("Failed to clone world from " + templateWorldName + " to " + worldName + ": " + failure.getFailureMessage());
   					Bukkit.getPluginManager().disablePlugin(OneVsOne.getPlugin());
   				});
   		} else {
   			// Template world is not loaded, try to load it first
   			loadAndCloneTemplateWorld();
   		}
  	}

  	private void loadAndCloneTemplateWorld() {
		Option<MultiverseWorld> templateOpt = worldManager.getWorld(templateWorldName);
		if (templateOpt.isDefined()) {
  			// Load the template world first
  			LoadWorldOptions loadOptions = LoadWorldOptions.world(templateOpt.get());
  			worldManager.loadWorld(loadOptions)
  				.onSuccess(loadedTemplateWorld -> {
  					// Now clone the loaded world
  					CloneWorldOptions options = CloneWorldOptions.fromTo(loadedTemplateWorld, worldName)
  						.saveBukkitWorld(true);
  					worldManager.cloneWorld(options)
  						.onSuccess(newWorld -> {
  							arenaWorld = newWorld.getBukkitWorld().getOrNull();
  							if (arenaWorld != null) {
  								arenaWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
  								arenaWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
  								arenaWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
  								arenaWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
  								arenaWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
  								arenaWorld.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
  								// Load data from config now that world is ready
  								loadDataFromConfig();
  							}
  						})
  						.onFailure(failure -> {
  							OneVsOne.getPlugin().getLogger().severe("Failed to clone world from " + templateWorldName + " to " + worldName + ": " + failure.getFailureMessage());
  							Bukkit.getPluginManager().disablePlugin(OneVsOne.getPlugin());
  						});
  				})
  				.onFailure(failure -> {
  					OneVsOne.getPlugin().getLogger().severe("Failed to load template world " + templateWorldName + ": " + failure.getFailureMessage());
  					Bukkit.getPluginManager().disablePlugin(OneVsOne.getPlugin());
  				});
  		} else {
  			throw new IllegalStateException("Template world '" + templateWorldName + "' not found");
  		}
  	}

	private void loadConfig() {
		arenaMapConfig = new YamlConfiguration();
		try {
			File configFile = new File(OneVsOne.getPlugin().getDataFolder(), templateWorldName + ".yml");
			arenaMapConfig.load(configFile);

		} catch (Exception e) {
			OneVsOne.getPlugin().getLogger().warning("No Config for world '" + templateWorldName + "' found!");
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

	public List<ItemStack> getInventory() {
		return inventory;
	}
	public List<ItemStack> getArmor() {
		return armor;
	}

	public Location getSpawn1() {
		return spawn1;
	}

	public Location getSpawn2() {
		return spawn2;
	}

	public static List<String> getMaps() {
		return maps;
	}

	public static void setMaps(List<String> list) {
		ArenaMap.maps = list;
	}
	
	public World getArenaWorld() {
		return arenaWorld;
	}

}
