package com.github.gamedipoxx.oneVsOneLobby.sign;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.gamedipoxx.oneVsOne.utils.GameState;
import com.github.gamedipoxx.oneVsOne.utils.SimpleArenaDatabaseObject;
import com.github.gamedipoxx.oneVsOneLobby.LobbyMessages;
import com.github.gamedipoxx.oneVsOneLobby.LobbySQLManager;
import com.github.gamedipoxx.oneVsOneLobby.OneVsOneLobby;
import com.github.gamedipoxx.oneVsOneLobby.PlayerConnector;

public class InventoryClickListener implements Listener {
	private ArrayList<Player> blacklist = new ArrayList<>();
	@EventHandler
	public void onPlayerClickEvent(InventoryClickEvent event) {
		if(!event.getInventory().equals(JoinGUI.getInventory())){
			return;
		}
		event.setCancelled(true);
		LobbySQLManager.fetchFromDatabase();
		SimpleArenaDatabaseObject sado = JoinGUI.arenaMapping.get(event.getSlot());
		Player playerwhoclicked = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
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
		if(blacklist.contains(playerwhoclicked)) {
			playerwhoclicked.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.PLEASEWAIT.getString());
			return;
		}
		new PlayerConnector(playerwhoclicked, sado.getArenaName());
		blacklist.add(playerwhoclicked);
		Bukkit.getScheduler().runTaskLater(OneVsOneLobby.getPlugin(), new Runnable() {
			
			private final Player player = playerwhoclicked;
			
			@Override
			public void run() {
				if(blacklist.contains(player)){
					blacklist.remove(player);
				}
			}
		}, 20*5);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(blacklist.contains(event.getPlayer())) {
			blacklist.remove(event.getPlayer());
		}
		LobbySQLManager.fetchFromDatabase();
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(blacklist.contains(event.getPlayer())) {
			blacklist.remove(event.getPlayer());
		}
		LobbySQLManager.fetchFromDatabase();
	}
}
