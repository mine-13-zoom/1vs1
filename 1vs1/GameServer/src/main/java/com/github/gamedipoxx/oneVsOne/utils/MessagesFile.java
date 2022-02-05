package com.github.gamedipoxx.oneVsOne.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MessagesFile {
	private static File messageConfigFile;
	private static FileConfiguration messageConfig;
	private static JavaPlugin plugin;

	public static void init() {
		if (plugin == null) {
			return;
		}
		createCustomConfig();
	}

	public static FileConfiguration getMessageConfig() {
		return messageConfig;
	}

	private static void createCustomConfig() {
		messageConfigFile = new File(plugin.getDataFolder(), "messages.yml");
		if (!messageConfigFile.exists()) {
			messageConfigFile.getParentFile().mkdirs();
			plugin.saveResource("messages.yml", false);
		}

		messageConfig = new YamlConfiguration();
		try {
			messageConfig.load(messageConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static void setPlugin(JavaPlugin plugin) {
		MessagesFile.plugin = plugin;
	}
	
	public static void reload() throws FileNotFoundException, IOException, InvalidConfigurationException {
		messageConfig = YamlConfiguration.loadConfiguration(messageConfigFile);
	}
}
