package com.github.gamedipoxx.oneVsOne.listener;

import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.commands.KitCommand;
import com.github.gamedipoxx.oneVsOne.events.PlayerJoinArenaEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerLeaveArenaEvent;
import com.github.gamedipoxx.oneVsOne.utils.KitManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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
    public void onPlayerLeaveArena(PlayerLeaveArenaEvent event) {
        KitCommand.setEditMode(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();

        // Check if player is in waiting lobby
        Arena playerArena = null;
        for (Arena arena : OneVsOne.getArena()) {
            if (arena.getPlayers().contains(player) && arena.getPlayerCount() == 1) {
                playerArena = arena;
                break;
            }
        }

        if (playerArena != null) {
            // Prevent dropping lobby items
            if (isLobbyItem(item)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null) {
            return;
        }

        // Check if player is in waiting lobby
        Arena playerArena = null;
        for (Arena arena : OneVsOne.getArena()) {
            if (arena.getPlayers().contains(player) && arena.getPlayerCount() == 1) {
                playerArena = arena;
                break;
            }
        }

        if (playerArena != null) {
            // Prevent moving lobby items
            if (isLobbyItem(item)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isLobbyItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        String displayName = item.getItemMeta().getDisplayName();
        return (item.getType() == Material.CHEST && displayName.equals("§aCustomize Kit")) ||
               (item.getType() == Material.RED_BED && displayName.equals("§cLeave"));
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

            KitCommand.setEditMode(player, true);
            player.getInventory().clear();
            String kitName = arena.getArenaMap().getKitName();
            Inventory kit = KitManager.getPlayerKit(player, kitName);
            if (kit != null) {
                player.getInventory().setContents(kit.getContents());
            } else {
                // Give default kit from arena config
                player.getInventory().setContents(arena.getArenaMap().getInventory().toArray(new ItemStack[0]));
                player.getInventory().setArmorContents(arena.getArenaMap().getArmor().toArray(new ItemStack[0]));
            }
            player.sendMessage("§aYou can now customize your kit. Type /kitsave to save it or /resetkit to reset it.");
        }
    }
}