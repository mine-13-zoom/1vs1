package com.github.gamedipoxx.oneVsOneLobby;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.github.gamedipoxx.oneVsOne.utils.MySQLManager;
import com.github.gamedipoxx.oneVsOne.utils.SimpleArenaDatabaseObject;
import com.github.gamedipoxx.oneVsOneLobby.sign.JoinGUI;

public class LobbySQLManager {
	private static ArrayList<SimpleArenaDatabaseObject> arenas = new ArrayList<>();
	
	public static void inti() {
		arenas = MySQLManager.readArenas();
	}
	
	public static void fetchFromDatabase() {
		Bukkit.getScheduler().runTaskAsynchronously(OneVsOneLobby.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				arenas = MySQLManager.readArenas();
				updateGui();
				
			}
		});
	}
	
	private static void updateGui() {
		Bukkit.getScheduler().runTask(OneVsOneLobby.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				JoinGUI.updateGui();
				
			}
		});
	}
	public static ArrayList<SimpleArenaDatabaseObject> getArenas() {
		return arenas;
	}

	public static void setArenas(ArrayList<SimpleArenaDatabaseObject> arenas) {
		LobbySQLManager.arenas = arenas;
	}
}
