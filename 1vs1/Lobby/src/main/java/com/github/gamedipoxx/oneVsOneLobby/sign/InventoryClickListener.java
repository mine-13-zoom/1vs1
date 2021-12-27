package com.github.gamedipoxx.oneVsOneLobby.sign;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.gamedipoxx.oneVsOne.utils.GameState;
import com.github.gamedipoxx.oneVsOne.utils.SimpleArenaDatabaseObject;
import com.github.gamedipoxx.oneVsOneLobby.LobbySQLManager;
import com.github.gamedipoxx.oneVsOneLobby.PlayerConnector;

public class InventoryClickListener implements Listener {
	@EventHandler
	public void onPlayerClickEvent(InventoryClickEvent event) {
		if(!event.getInventory().equals(JoinGUI.getInventory())){
			return;
		}
		event.setCancelled(true);
		LobbySQLManager.fetchFromDatabase();
		SimpleArenaDatabaseObject sado = JoinGUI.arenaMapping.get(event.getSlot());
		if(sado == null) {
			return;
		}
		if(sado.getGameState() == GameState.ENDING) {
			return;
		}
		if(sado.getGameState() == GameState.UNDEFINED) {
			return;
		}
		if(sado.getGameState() == GameState.INGAME) {
			return;
		}
		new PlayerConnector(Bukkit.getPlayer(event.getWhoClicked().getUniqueId()), sado.getArenaName());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		LobbySQLManager.fetchFromDatabase();
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		LobbySQLManager.fetchFromDatabase();
	}
}
