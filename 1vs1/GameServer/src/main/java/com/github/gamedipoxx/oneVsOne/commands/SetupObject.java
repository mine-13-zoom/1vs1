package com.github.gamedipoxx.oneVsOne.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.PlayerInventory;

import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.ArenaMap;

public class SetupObject {
	private String kitname;
	private PlayerInventory inv;
	private Location spawn1;
	private Location spawn2;
	private YamlConfiguration mapConfig;

	public SetupObject() {

	}

	public void save() {
		
		mapConfig = new YamlConfiguration();
		
		mapConfig.set("Spawn1.X", spawn1.getX());
		mapConfig.set("Spawn1.Y", spawn1.getY());
		mapConfig.set("Spawn1.Z", spawn1.getZ());
		mapConfig.set("Spawn1.Pitch", spawn1.getPitch());
		mapConfig.set("Spawn1.Yaw", spawn1.getYaw());
		
		mapConfig.set("Spawn2.X", spawn2.getX());
		mapConfig.set("Spawn2.Y", spawn2.getY());
		mapConfig.set("Spawn2.Z", spawn2.getZ());
		mapConfig.set("Spawn2.Pitch", spawn2.getPitch());
		mapConfig.set("Spawn2.Yaw", spawn2.getYaw());
		
		mapConfig.set("Kit.Name", kitname);
		
		mapConfig.set("Kit.Armor", inv.getArmorContents());
		mapConfig.set("Kit.Contents", inv.getContents());
		
		try {
			mapConfig.save(new File(OneVsOne.getPlugin().getDataFolder(), spawn1.getWorld().getName() + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<String> mapList = ArenaMap.getMaps();
		if(mapList == null) {
			mapList = new ArrayList<>();
		}
		mapList.add(spawn1.getWorld().getName());
		ArenaMap.setMaps(mapList);
		OneVsOne.getPlugin().getConfig().set("Maps", mapList);
		OneVsOne.getPlugin().saveConfig();
		OneVsOne.getPlugin().reloadConfig();
		
	}

	public String getKitname() {
		return kitname;
	}

	public void setKitname(String kitname) {
		this.kitname = kitname;
	}

	public PlayerInventory getInv() {
		return inv;
	}

	public void setInv(PlayerInventory inv) {
		this.inv = inv;
	}

	public Location getSpawn1() {
		return spawn1;
	}

	public void setSpawn1(Location spawn1) {
		this.spawn1 = spawn1;
	}

	public Location getSpawn2() {
		return spawn2;
	}

	public void setSpawn2(Location spawn2) {
		this.spawn2 = spawn2;
	}
}
