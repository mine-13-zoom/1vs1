package com.github.gamedipoxx.oneVsOne.commands;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.gamedipoxx.oneVsOne.Messages;
import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;

public class OneVsOneLeaveCommand implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player player) {
			player.sendMessage(Messages.PREFIX.getString() + Messages.TELEPORTTOLOBBY.getString());
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			ArrayList<Arena> list = new ArrayList<>(OneVsOne.getArena());
			for(Arena arena : list) {
				for(Player fplayer : arena.getPlayers()) {
					if(fplayer == player) {
						arena.removePlayer(player);
						return false;
					}
				}
			}
		}
		return false;
	}

}
