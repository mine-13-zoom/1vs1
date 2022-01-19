package com.github.gamedipoxx.oneVsOne.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.gamedipoxx.oneVsOne.Messages;
import com.github.gamedipoxx.oneVsOne.OneVsOne;

public class OneVsOneSetupCommand implements CommandExecutor{
	
	private static SetupObject setupobj;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(sender instanceof Player == false) {
			return false;
		}
		Player player = (Player) sender;
		if(!player.hasPermission("*")) {
			player.sendMessage(Messages.NOPERMISSION.getString());
			return false;
		}
		
		if(!OneVsOne.getPlugin().getConfig().getBoolean("setupmode")) {
			player.sendMessage(Messages.PLEASEENABLESETUPMODE.getString());
			return false;
		}
		
		if(args.length == 0) {
			player.sendMessage(Messages.PREFIX.getString() + Messages.SETUPLIST.getString());
			return false;
		}
		
		switch (args[0]) {
		case "spawn1": {
			getSetupObject().setSpawn1(player.getLocation());
			player.sendMessage(Messages.PREFIX.getString() + Messages.OK.getString());
			break;
		}
		case "spawn2": {
			getSetupObject().setSpawn2(player.getLocation());
			player.sendMessage(Messages.PREFIX.getString() + Messages.OK.getString());
			break;
		}
		case "inv": {
			getSetupObject().setInv(player.getInventory());
			player.sendMessage(Messages.PREFIX.getString() + Messages.OK.getString());
			break;
		}
		case "kitname": {
			if(args.length == 1) {
				player.sendMessage(Messages.PREFIX.getString() + Messages.SETUPSPECIFYNAME.getString());
				break;
			}
			if(args.length == 2) {
				getSetupObject().setKitname(args[1]);
				player.sendMessage(Messages.PREFIX.getString() + Messages.OK.getString());
				break;
			}
		}
		case "save": {
			getSetupObject().save();
			resetSetupObject();
			player.sendMessage(Messages.PREFIX.getString() + Messages.OK.getString());
			break;
		}
		default:
			player.sendMessage(Messages.PREFIX.getString() + Messages.SETUPLIST.getString());
			break;
		}
		
		
		return false;
	}
	
	public static SetupObject getSetupObject() {
		return setupobj;
	}
	
	public static void resetSetupObject() {
		setupobj = new SetupObject();
	}

}
