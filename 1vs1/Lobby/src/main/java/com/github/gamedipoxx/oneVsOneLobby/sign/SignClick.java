package com.github.gamedipoxx.oneVsOneLobby.sign;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.gamedipoxx.oneVsOneLobby.LobbyMessages;
import com.github.gamedipoxx.oneVsOneLobby.LobbySQLManager;

public class SignClick implements Listener {
	@EventHandler
	public void onPlayerClickSign(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getClickedBlock() == null) {
			return;
		}
		if (event.getClickedBlock().getState() == null) {
			return;
		}
		if (event.getClickedBlock().getState() instanceof Sign) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase(LobbyMessages.JOINSIGN_LINE1.getString())) {
					sign.setLine(1, LobbyMessages.JOINSIGN_LINE2.getString());
					sign.setLine(2, LobbyMessages.JOINSIGN_LINE3.getString());
					sign.setLine(3, LobbyMessages.JOINSIGN_LINE4.getString());
					sign.update();
					JoinGUI.openForPlayer(player);
					LobbySQLManager.fetchFromDatabase();
				}
			}
		}
	}
}
