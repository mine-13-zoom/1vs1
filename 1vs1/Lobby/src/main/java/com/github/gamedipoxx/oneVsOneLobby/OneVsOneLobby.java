package com.github.gamedipoxx.oneVsOneLobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.gamedipoxx.oneVsOne.utils.MessagesFile;
import com.github.gamedipoxx.oneVsOne.utils.MySQLManager;
import com.github.gamedipoxx.oneVsOne.utils.UpdateChecker;
import com.github.gamedipoxx.oneVsOne.utils.stats.GUIClickListener;
import com.github.gamedipoxx.oneVsOne.utils.stats.GlobalStatsGUI;
import com.github.gamedipoxx.oneVsOne.utils.stats.JoinAndFetchListener;
import com.github.gamedipoxx.oneVsOne.utils.stats.MainStatsGUI;
import com.github.gamedipoxx.oneVsOne.utils.stats.PrivateStatsGUI;
import com.github.gamedipoxx.oneVsOneLobby.commands.OneVsOneLobbyCommand;
import com.github.gamedipoxx.oneVsOneLobby.sign.InventoryClickListener;
import com.github.gamedipoxx.oneVsOneLobby.sign.JoinAndLeaveListener;
import com.github.gamedipoxx.oneVsOneLobby.sign.JoinGUI;
import com.github.gamedipoxx.oneVsOneLobby.sign.SignClick;

public class OneVsOneLobby extends JavaPlugin {

	private static OneVsOneLobby plugin;

	@Override
	public void onEnable() {

		plugin = this;
		
		//Setting up messages
		MessagesFile.setPlugin(this);
		MessagesFile.init();
		
		//setting up UpdateChecker
		UpdateChecker.setCurrentVersion(getDescription().getVersion());
		UpdateChecker.setPlugin(this);
		UpdateChecker.check();
		
		//init database
		MySQLManager.setConfig(getConfig());
		MySQLManager.setSetupFile(getResource("dbsetup.sql"));
		MySQLManager.setPlugin(this);
		
		//init GUIs
		JoinGUI.init();
		
		GlobalStatsGUI.setPlugin(plugin);
		GlobalStatsGUI.setGamesLost(LobbyMessages.GAMESLOST.getString());
		GlobalStatsGUI.setGamesPlayed(LobbyMessages.GAMESPLAYED.getString());
		GlobalStatsGUI.setGamesWon(LobbyMessages.GAMESWON.getString());
		
		PrivateStatsGUI.setGamesLost(LobbyMessages.GAMESLOST.getString());
		PrivateStatsGUI.setGamesPlayed(LobbyMessages.GAMESPLAYED.getString());
		PrivateStatsGUI.setGamesWon(LobbyMessages.GAMESWON.getString());
		
		MainStatsGUI.setGlobalStats(LobbyMessages.GLOBALSTATS.getString());
		MainStatsGUI.setPrivateStats(LobbyMessages.PRIVATESTATS.getString());
		 
		//saving config
		this.saveDefaultConfig();
		this.reloadConfig();
		
		//starting connection to database
		if (MySQLManager.init() == false) {
			Bukkit.getPluginManager().disablePlugin(this);
		}
		//register listener
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		getServer().getPluginManager().registerEvents(new SignClick(), this);
		getServer().getPluginManager().registerEvents(new JoinAndLeaveListener(), this);
		getServer().getPluginManager().registerEvents(new JoinAndFetchListener(), this);
		getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
		getServer().getPluginManager().registerEvents(new UpdateChecker(), this);
		//register Command
		this.getCommand("onevsonelobby").setExecutor(new OneVsOneLobbyCommand());
		//register Bungeecord channel
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		super.onDisable();
	}

	public static OneVsOneLobby getPlugin() {
		return plugin;
	}
}
