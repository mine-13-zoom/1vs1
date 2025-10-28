package com.github.gamedipoxx.oneVsOneLobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import com.github.gamedipoxx.oneVsOne.utils.SimpleArenaDatabaseObject;
import com.github.gamedipoxx.oneVsOne.utils.stats.MainStatsGUI;
import com.github.gamedipoxx.oneVsOne.utils.stats.PrivateStatsGUI;
import com.github.gamedipoxx.oneVsOne.utils.stats.StatsObject;
import com.github.gamedipoxx.oneVsOneLobby.LobbyMessages;
import com.github.gamedipoxx.oneVsOneLobby.LobbySQLManager;
import com.github.gamedipoxx.oneVsOneLobby.sign.JoinGUI;

public class OneVsOneLobbyCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player == false) {
			return false;
		}
		Player player = (Player) sender;

		if (args.length == 0) {
			if (!player.hasPermission("OneVsOne.admin")) {
				player.sendMessage(LobbyMessages.BRANDING.getString());
				return false;
			}
			player.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.WRONGARGS.getString());
			return false;
		}
		if (args.length >= 1) {
			switch (args[0]) {
			case "list": {
				if (!player.hasPermission("OneVsOne.admin")) {
					player.sendMessage(LobbyMessages.BRANDING.getString());
					return false;
				}
				if(args.length != 1) {
					break;
				}
				for(SimpleArenaDatabaseObject sado : LobbySQLManager.getArenas()) {
					player.sendMessage(LobbyMessages.PREFIX.getString() + sado.getArenaName() + " | " + sado.getPlayercount());
				}
				break;
			}
			case "gui": {
				if (!player.hasPermission("OneVsOne.admin")) {
					player.sendMessage(LobbyMessages.BRANDING.getString());
					return false;
				}
				if(args.length != 1) {
					break;
				}
				JoinGUI.openForPlayer(player);
				LobbySQLManager.fetchFromDatabase();
				break;
				
			}
			case "stats": {
				if (!player.hasPermission("OneVsOne.stats.me")) {
					player.sendMessage(LobbyMessages.NOPERMISSION.getString());
					return false;
				}
				if(args.length == 1) {
					player.openInventory(MainStatsGUI.getInv());
					break;
				}
				if(args.length == 2) {
					if(player.hasPermission("OneVsOne.stats.other")) {
						StatsObject stats = StatsObject.getStatsCachByName().get(args[1]);
						if(stats != null) {
							OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(stats.getUuid());
							PrivateStatsGUI gui = new PrivateStatsGUI(offlinePlayer);
							gui.openForPlayer(player);
						}
						else {
							PrivateStatsGUI gui = new PrivateStatsGUI(null);
							gui.openForPlayer(player);
						}
					}
					else {
						player.sendMessage(LobbyMessages.PREFIX.getString() +  LobbyMessages.NOPERMISSION.getString());
					}
				}
				break;
				
			}
			case "addsign": {
				if(args.length != 1) {
					break;
				}
				RayTraceResult result = player.rayTraceBlocks(6.0);
				if (result == null || result.getHitBlock() == null) {
					player.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.WRONGSIGNCREATE.getString());
					break;
				}
				Block block = result.getHitBlock();
				if(block.getState() == null) {
					player.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.WRONGSIGNCREATE.getString());
					break;
				}
				if(block.getState() instanceof Sign == false) {
					player.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.WRONGSIGNCREATE.getString());
					break;
				}
				Sign sign = (Sign) block.getState();
				sign.getSide(Side.FRONT).setLine(0, LobbyMessages.JOINSIGN_LINE1.getString());
				sign.getSide(Side.FRONT).setLine(1, LobbyMessages.JOINSIGN_LINE2.getString());
				sign.getSide(Side.FRONT).setLine(2, LobbyMessages.JOINSIGN_LINE3.getString());
				sign.getSide(Side.FRONT).setLine(3, LobbyMessages.JOINSIGN_LINE4.getString());
				sign.update();
				player.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.SIGNCREATESUCESS.getString());
				break;
				
			}
			default:
				player.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.WRONGARGS.getString());
			}
		}
		return false;
	}
}
