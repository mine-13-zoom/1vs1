package com.github.gamedipoxx.oneVsOne;

import org.bukkit.plugin.java.JavaPlugin;

public class OneVsOneLobby extends JavaPlugin {
	
	private final OneVsOneLobby plugin = this;

	@Override
	public void onEnable() {
		
		this.saveDefaultConfig();
		this.reloadConfig();
		MySQLManager.init();
		
				
	}
	
	public OneVsOneLobby getPlugin() {
		return plugin;
	}
}
