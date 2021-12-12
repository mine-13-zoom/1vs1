package com.github.gamedipoxx.oneVsOneBungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class OneVsOneBungee extends Plugin {
	private static Configuration configuration;
	private static OneVsOneBungee plugin;
	private static HashMap<ProxiedPlayer, String> playerArenaMap = new HashMap<>();
	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfg();
		loadConfig();
		
		getProxy().registerChannel("OneVsOneConnect");
		getProxy().registerChannel("OneVsOneGet");
		
		getProxy().getPluginManager().registerListener(this, new OneVsOneConnectMessage());
		getProxy().getPluginManager().registerListener(this, new OneVsOneGetMessage());
		
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	private void saveDefaultConfg() {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		File file = new File(getDataFolder(), "config.yml");

		if (!file.exists()) {
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void loadConfig() {
		try {
			configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			getProxy().getPluginManager().unregisterListeners(this);
			getProxy().getPluginManager().unregisterCommands(this);
			onDisable();
			e.printStackTrace();
		}
	}
	
	public static OneVsOneBungee getPlugin() {
		return plugin;
	}

	public static HashMap<ProxiedPlayer, String> getPlayerArenaMap() {
		return playerArenaMap;
	}

	public static void setPlayerArenaMap(HashMap<ProxiedPlayer, String> playerArenaMap) {
		OneVsOneBungee.playerArenaMap = playerArenaMap;
	}

}
