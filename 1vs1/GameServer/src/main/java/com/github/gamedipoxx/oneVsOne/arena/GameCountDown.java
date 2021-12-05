package com.github.gamedipoxx.oneVsOne.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.gamedipoxx.oneVsOne.Messages;
import com.github.gamedipoxx.oneVsOne.OneVsOne;

public class GameCountDown implements Listener {
	private int taskId;
	private static List<Player> players = new ArrayList<Player>();
	
	public static List<Player> getPlayers() {
		return players;
	}

	public static void setPlayers(List<Player> players) {
		GameCountDown.players = players;
	}

	public GameCountDown(Arena arena) {
		for(Player player : arena.getPlayers()) {
			players.add(player);
		}
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(OneVsOne.getPlugin(), new Runnable() {
			
			int i;
			@Override
			public void run() {
				i++;
				if(i == 1) {
					showTitle("§25", "§7Mache dich bereit", arena);
				}
				if(i == 2) {
					showTitle("§24", "§7Mache dich bereit", arena);
				}
				if(i == 3) {
					showTitle("§23", "§7Mache dich bereit", arena);
				}
				if(i == 4) {
					showTitle("§22", "§7Mache dich bereit", arena);
				}
				if(i == 5) {
					showTitle("§21", "§7Mache dich bereit", arena);
				}
				if(i == 6) {
					showStartSubTitle(arena);
					for(Player forplayer : arena.getPlayers()) {
						players.remove(forplayer);
					}
					Bukkit.getScheduler().cancelTask(taskId);
						
				}
				
				
			}
		}, 10, 20);
	}
	
	private void showTitle(String titel, String subtitle, Arena arena) {
		for(Player player : arena.getPlayers()) {
			player.resetTitle();
			player.sendTitle(titel, subtitle, 0, 20, 0);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
		}
	}
	private void showStartSubTitle(Arena arena) {
		for(Player player : arena.getPlayers()) {
			player.sendTitle(Messages.STARTTITLE.getString(), Messages.STARTSUBTITLE.getString(), 10, 40, 10);
			player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.5F, 1F);
		}
	}
}
