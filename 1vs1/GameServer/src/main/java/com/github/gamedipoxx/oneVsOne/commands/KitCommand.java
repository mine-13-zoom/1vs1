package com.github.gamedipoxx.oneVsOne.commands;

import com.github.gamedipoxx.oneVsOne.Messages;
import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.utils.KitManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if (command.getName().equalsIgnoreCase("kitsave")) {
            Arena arena = null;
            for (Arena a : OneVsOne.getArena()) {
                if (!a.getPlayers().contains(player)) continue;
                arena = a;
                break;
            }
            if (arena == null) {
                player.sendMessage("§cYou must be in an arena to save your kit.");
                return true;
            }
            String kitName = arena.getArenaMap().getKitName();
            KitManager.savePlayerKit(player, kitName);
            player.sendMessage("§aYour kit for '" + kitName + "' has been saved.");
            player.getInventory().clear();
            ItemStack customizeKitItem = new ItemStack(Material.CHEST);
            ItemMeta meta = customizeKitItem.getItemMeta();
            meta.setDisplayName("§aCustomize Kit");
            customizeKitItem.setItemMeta(meta);
            player.getInventory().setItem(0, customizeKitItem);
            ItemStack leaveItem = new ItemStack(Material.RED_BED);
            ItemMeta leaveMeta = leaveItem.getItemMeta();
            leaveMeta.setDisplayName("§cLeave");
            leaveItem.setItemMeta(leaveMeta);
            player.getInventory().setItem(8, leaveItem);
        }
        return false;
    }
}