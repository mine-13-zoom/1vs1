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

import java.util.HashSet;
import java.util.UUID;

public class KitCommand implements CommandExecutor {
    private static final HashSet<UUID> playersInEditMode = new HashSet<>();

    public static void setEditMode(Player player, boolean editMode) {
        if (editMode) {
            playersInEditMode.add(player.getUniqueId());
        } else {
            playersInEditMode.remove(player.getUniqueId());
        }
    }

    public static boolean isInEditMode(Player player) {
        return playersInEditMode.contains(player.getUniqueId());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        Arena arena = null;
        for (Arena a : OneVsOne.getArena()) {
            if (a.getPlayers().contains(player)) {
                arena = a;
                break;
            }
        }

        if (command.getName().equalsIgnoreCase("kitsave")) {
            if (!isInEditMode(player)) {
                player.sendMessage("§cYou must be in edit mode to save your kit. Click the Customize Kit item first.");
                return true;
            }

            if (arena == null) {
                player.sendMessage("§cYou must be in an arena to save your kit.");
                return true;
            }

            String kitName = arena.getArenaMap().getKitName();
            KitManager.savePlayerKit(player, kitName);
            player.sendMessage("§aYour kit for '" + kitName + "' has been saved.");

            setEditMode(player, false);
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
            return true;
        }

        if (command.getName().equalsIgnoreCase("resetkit")) {
            if (!isInEditMode(player)) {
                player.sendMessage("§cYou must be in edit mode to reset your kit. Click the Customize Kit item first.");
                return true;
            }

            if (arena == null) {
                player.sendMessage("§cYou must be in an arena to reset your kit.");
                return true;
            }

            player.getInventory().clear();
            player.getInventory().setContents(arena.getArenaMap().getInventory().toArray(new ItemStack[0]));
            player.getInventory().setArmorContents(arena.getArenaMap().getArmor().toArray(new ItemStack[0]));
            player.sendMessage("§aYour kit has been reset to default.");
            return true;
        }

        return false;
    }
}