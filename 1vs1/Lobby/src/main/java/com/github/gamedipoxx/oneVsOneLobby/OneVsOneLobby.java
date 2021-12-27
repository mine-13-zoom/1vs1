package com.github.gamedipoxx.oneVsOneLobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.gamedipoxx.oneVsOne.utils.MySQLManager;
import com.github.gamedipoxx.oneVsOneLobby.commands.OneVsOneLobbyCommand;
import com.github.gamedipoxx.oneVsOneLobby.sign.InventoryClickListener;
import com.github.gamedipoxx.oneVsOneLobby.sign.JoinGUI;

public class OneVsOneLobby extends JavaPlugin {
	
	private static OneVsOneLobby plugin;

	@Override
	public void onEnable() {
		
		plugin = this;
		MySQLManager.setConfig(getConfig());
		MySQLManager.setSetupFile(getResource("dbsetup.sql"));
		MySQLManager.setPlugin(this);
		JoinGUI.init();
		this.saveDefaultConfig();
		this.reloadConfig();
		if(MySQLManager.init() == false) {
			Bukkit.getPluginManager().disablePlugin(this);
		}
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		this.getCommand("onevsonelobby").setExecutor(new OneVsOneLobbyCommand());
			
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}
	
	public static OneVsOneLobby getPlugin() {
		return plugin;
	}
}
