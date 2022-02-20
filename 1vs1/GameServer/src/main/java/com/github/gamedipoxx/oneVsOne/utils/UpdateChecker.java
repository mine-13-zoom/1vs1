package com.github.gamedipoxx.oneVsOne.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author EncryptDev Modified by Professor_Sam (Change code style)
 */
public class UpdateChecker implements Listener {

	private static String url = "https://api.spigotmc.org/legacy/update.php?resource=";
	private static String id = "99952";

	private static String currentVersion;
	private static JavaPlugin plugin;

	private static boolean isAvailable;

	public UpdateChecker() {
		
	}

	public static boolean isAvailable() {
		return isAvailable;
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPermission("OneVsOne.admin")) {
			return;
		}
		if (!isAvailable) {
			return;
		}
		Player player = event.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				player.sendMessage(" ");
				player.sendMessage("§7There is a new §lversion§r §7of §f§lOneVsOne§r §7available! Update now:");
				player.sendMessage("§6§nhttps://www.spigotmc.org/resources/1vs1-bungee-only.99952 ");
				player.sendMessage(" ");
				player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.2F, 1F);
			}
		}, 60L);

	}

	public static void check() {
		isAvailable = checkUpdate();
	}

	private static boolean checkUpdate() {
		try {
			HttpsURLConnection connection = (HttpsURLConnection) new URL(url + id).openConnection();
			connection.setRequestMethod("GET");
			String raw = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

			String remoteVersion;
			if (raw.contains("-")) {
				remoteVersion = raw.split("-")[0].trim();
			} else {
				remoteVersion = raw;
			}

			if (!currentVersion.equalsIgnoreCase(remoteVersion)) {
				plugin.getLogger().warning("§4Please update! Your version: " + currentVersion + " | Latest version: " + remoteVersion);
				return true;
			}

		} catch (IOException e) {
			return false;
		}
		return false;
	}
	
	public static void setCurrentVersion(String version) {
		currentVersion = version;
	}

	public static void setPlugin(JavaPlugin plugin) {
		UpdateChecker.plugin = plugin;
	}

}