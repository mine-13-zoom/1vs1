package com.github.gamedipoxx.oneVsOneLobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.gamedipoxx.oneVsOneLobby.PlayerConnector;

public class OneVsOneLobbyCommand implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player == false) {
			return false;
		}
		Player player = (Player) sender;
		if(!player.hasPermission("*")) {
			return false;
		}
		
		if(args.length == 0) {
			player.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.WRONGARGS.getString());
			return false;
		}
		if(args.length >= 1) {
			switch (args[0]) {
			case "join": {
				if(args.length == 1) {
					player.sendMessage(LobbyMessages.WRONGJOINARG.getString());
					break;
				}
				if(args.length == 2) {
					new PlayerConnector(player, args[1]);
					player.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.CONNECTING.getString());
				}
				else {
					player.sendMessage(LobbyMessages.WRONGJOINARG.getString());
				}
				break;
			}
			default:
				player.sendMessage(LobbyMessages.PREFIX.getString() + LobbyMessages.WRONGARGS.getString());
			}
		}
		return false;
	}
}
