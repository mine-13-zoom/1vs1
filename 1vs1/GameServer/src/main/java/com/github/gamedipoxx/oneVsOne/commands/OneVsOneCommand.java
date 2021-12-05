package com.github.gamedipoxx.oneVsOne.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.gamedipoxx.oneVsOne.Messages;
import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;

public class OneVsOneCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player == false) {
			return false;
		}
		Player player = (Player) sender;
		if (player.hasPermission("OneVsOne.admin")) {
			if (args.length >= 1) {
				switch (args[0]) {
				case ("create"):
					String message = "§cArena: " + Arena.createAndRegisterArena().getArenaName();
					player.sendMessage(message);
					OneVsOne.getPlugin().getLogger().info(message);
					break;
				case ("join"):
					if (args.length == 2) {
						if(OneVsOne.getArena().size() == 0) {
							player.sendMessage(Messages.PREFIX.getString() + Messages.NOARENAAVAIBLE.getString());
							break;
						}
						for (Arena arena : OneVsOne.getArena()) {
							if(arena.getArenaName().equalsIgnoreCase(args[1])) {
								arena.joinPlayer(player);
								break;
							}
						}
						player.sendMessage(Messages.PREFIX.getString() + Messages.NOARENAFOUND.getString());
					}
					break;
				case("delete"):
					if(args.length != 2) {
						break;
					}
					if(Arena.getArena(args[1]) == null) {
						player.sendMessage(Messages.PREFIX.getString() + Messages.NOARENAFOUND.getString());
					}
					else {
						Arena.deleteAndUnregisterArena(Arena.getArena(args[1]));
					}
					break;
				case("list"):
					if(args.length != 1) {
						break;
					}
					if(OneVsOne.getArena().size() == 0) {
						player.sendMessage(Messages.PREFIX.getString() + Messages.NOARENAFOUND.getString());
					}
					else {
						for(Arena arena : OneVsOne.getArena()) {
							player.sendMessage(Messages.PREFIX.getString() + arena.getArenaName());
						}
					}
					break;
				case("reload"):
					if(args.length != 1) {
						break;
					}
					OneVsOne.getPlugin().saveConfig();
					OneVsOne.getPlugin().reloadConfig();
					player.sendMessage(Messages.PREFIX.getString() + Messages.CONFIGRELOADED.getString());
					
					break;
				case("leave"):
					for(Arena arena : OneVsOne.getArena()) {
						for(Player forPlayer : arena.getPlayers()) {
							if(forPlayer == player) {
								arena.removePlayer(player);
								break;
							}
						}
					}
					break;
				default:
					break;
				}
			} else {
				player.sendMessage(Messages.PREFIX.getString() + Messages.COMMANDS.getString());
			}
		}
		return false;
		
	}
}
