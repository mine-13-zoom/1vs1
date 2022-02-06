package com.github.gamedipoxx.oneVsOne;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.arena.ArenaMap;
import com.github.gamedipoxx.oneVsOne.commands.OneVsOneCommand;
import com.github.gamedipoxx.oneVsOne.commands.OneVsOneLeaveCommand;
import com.github.gamedipoxx.oneVsOne.commands.OneVsOneSetupCommand;
import com.github.gamedipoxx.oneVsOne.listener.ArenaManager;
import com.github.gamedipoxx.oneVsOne.listener.PlayerChatListener;
import com.github.gamedipoxx.oneVsOne.listener.PlayerJoinListener;
import com.github.gamedipoxx.oneVsOne.listener.PlayerMoveEventCancel;
import com.github.gamedipoxx.oneVsOne.listener.TabListRemover;
import com.github.gamedipoxx.oneVsOne.utils.MessagesFile;
import com.github.gamedipoxx.oneVsOne.utils.MySQLManager;
import com.onarandombox.MultiverseCore.MultiverseCore;

public class OneVsOne extends JavaPlugin{
	private static ArrayList<Arena> arena = new ArrayList<Arena>();
	private static OneVsOne plugin;
	private static MultiverseCore multiversecore;
	@Override
	public void onEnable() {
		//Config stuff
		saveDefaultConfig();
		saveConfig();
		MessagesFile.setPlugin(this);
		MessagesFile.init();
		
		//init plugins and Apis
		plugin = this;
		multiversecore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		
		//register Commands and Bungeecord
		this.getCommand("OneVsOne").setExecutor(new OneVsOneCommand());
		this.getCommand("OneVsOneSetup").setExecutor(new OneVsOneSetupCommand());
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		//check for setupmode and cancel the rest of onEnable()
		if(getConfig().getBoolean("setupmode") == true) {
			OneVsOneSetupCommand.resetSetupObject();
			return;
		}
		
		if(getConfig().getBoolean("leaveCommand")) {
			this.getCommand("leave").setExecutor(new OneVsOneLeaveCommand());
			ArrayList<String> list = new ArrayList<>();
			list.add("l");
			this.getCommand("leave").setAliases((List<String>)list);
		}
		
		//init Database
		MySQLManager.setConfig(getConfig());
		MySQLManager.setPlugin(this);
		MySQLManager.setSetupFile(getResource("dbsetup.sql"));
		if (!MySQLManager.init()) {
			getServer().getPluginManager().disablePlugin(this);
		}
		//register all Events
		getServer().getPluginManager().registerEvents(new ArenaManager(), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveEventCancel(), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
		getServer().getPluginManager().registerEvents(new TabListRemover(), this);
		//getServer().getPluginManager().registerEvents(new EventDebugger(), this); //USE THIS JUST FOR DEBUG PURPOSE!
		
		//Create a Kit list
		ArenaMap.setMaps(getConfig().getStringList("Maps"));
		
		//clear Database
		MySQLManager.purgeDatabase();
		
		//create all Arenas as definded in the config.yml
		ArenaManager.createMaxArenas();
		
	}
	
	@Override
	public void onDisable() {
		if(getConfig().getBoolean("setupmode")) {
			return;
		}
		
		MySQLManager.purgeDatabase();
		@SuppressWarnings("unchecked")
		ArrayList<Arena> templist = (ArrayList<Arena>) arena.clone();
		for(Arena arena : templist) {
			Arena.deleteAndUnregisterArenaForOnDisable(arena);
		}
	}
	public static Collection<Arena> getArena() {
		return arena;
	}
	
	public static void setArena(ArrayList<Arena> arena) {
		OneVsOne.arena = arena;
	}
	public static OneVsOne getPlugin() {
		return plugin;
	}
	
	public static MultiverseCore getMultiversecore() {
		return multiversecore;
	}
}
