package com.github.gamedipoxx.oneVsOne.listener;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class OnTntPlaceListener implements Listener{
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPlaceTntEvent(BlockPlaceEvent event) {
		if(event.getBlock().getType() != Material.TNT) {
			return;
		}
		event.setCancelled(true);
		event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
		World world = event.getBlock().getLocation().getWorld();
		world.spawnEntity(event.getBlock().getLocation(), EntityType.PRIMED_TNT);
	}
	
	@EventHandler
	private void onBlockBreakEvent(EntityExplodeEvent event) {
		if(event.getEntityType() != EntityType.PRIMED_TNT) {
			return;
		}
		event.setYield(0F);
	}
}
