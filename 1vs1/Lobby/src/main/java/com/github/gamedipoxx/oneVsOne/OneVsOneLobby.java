package com.github.gamedipoxx.oneVsOne;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.gamedipoxx.oneVsOne.utils.MySQLManager;

public class OneVsOneLobby extends JavaPlugin {
	
	private static OneVsOneLobby plugin;

	@Override
	public void onEnable() {
		Date startDate = new Date();
		
		plugin = this;
		this.saveDefaultConfig();
		this.reloadConfig();
		MySQLManager.init();
		
		Date endDate = new Date();
		long difference = endDate.getTime() - startDate.getTime();
		getLogger().info("§cTime to start 1vs1: " + TimeUnit.MILLISECONDS.toSeconds(difference));
				
	}
	
	public static OneVsOneLobby getPlugin() {
		return plugin;
	}
}
