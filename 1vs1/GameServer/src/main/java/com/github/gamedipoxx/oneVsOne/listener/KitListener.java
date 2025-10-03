package com.github.gamedipoxx.oneVsOne.listener;

import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.events.PlayerJoinArenaEvent;
import com.github.gamedipoxx.oneVsOne.utils.KitManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class KitListener implements Listener {

    @EventHandler
    public void onPlayerJoinArena(PlayerJoinArenaEvent event) {
        if (event.getArena().getPlayerCount() == 1) {
            Player player = event.getPlayer();
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
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.CHEST && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals("§aCustomize Kit")) {
            Arena arena = null;
            for (Arena a : OneVsOne.getArena()) {
                if (a.getPlayers().contains(player)) {
                    arena = a;
                    break;
                }
            }
            if (arena == null) {
                return;
            }

            player.getInventory().clear();
            String kitName = arena.getArenaMap().getKitName();
            Inventory kit = KitManager.getPlayerKit(player, kitName);
            if (kit != null) {
                player.getInventory().setContents(kit.getContents());
            } else {
                player.getInventory().setContents(arena.getArenaMap().getInventory().toArray(new ItemStack[0]));
                player.getInventory().setArmorContents(arena.getArenaMap().getArmor().toArray(new ItemStack[0]));
            }
            player.sendMessage("§aYou can now customize your kit. Type /kitsave to save it.");
        }
    }
}