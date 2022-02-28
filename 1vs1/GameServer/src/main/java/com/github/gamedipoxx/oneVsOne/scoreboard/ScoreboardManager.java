package com.github.gamedipoxx.oneVsOne.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.events.GameStateChangeEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerJoinArenaEvent;
import com.github.gamedipoxx.oneVsOne.events.PlayerLeaveArenaEvent;

import fr.mrmicky.fastboard.FastBoard;

public class ScoreboardManager implements Listener{
	
	@EventHandler
	public void onPlayerJoinArenaEvent(PlayerJoinArenaEvent event) {
		Arena arena = event.getArena();
		FastBoard board = new FastBoard(event.getPlayer());
		
		board.updateTitle(arena.getScoreboard().getTitle());
		arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_1_HP);
		arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_1_NAME);
		arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_2_HP);
		arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_2_NAME);
		arena.getScoreboard().update(UpdateablePlaceholders.GAMESTATE);
		try {
			board.updateLines(arena.getScoreboard().getScoreboardContent());
			arena.getScoreboard().getBoards().put(event.getPlayer(), board);
		}
		catch (Exception e) {
			if(OneVsOne.getPlugin().getConfig().getBoolean("debug")) {
				e.printStackTrace();
			}
		}
		
	}
	
	@EventHandler
	public void onPlayerLeftArena(PlayerLeaveArenaEvent event) {
		Arena arena = event.getArena();

		arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_1_HP);
		arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_1_NAME);
		arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_2_HP);
		arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_2_NAME);
		arena.getScoreboard().update(UpdateablePlaceholders.GAMESTATE);
		
		if(arena.getScoreboard().getBoards().get(event.getPlayer()) != null) {
			arena.getScoreboard().getBoards().get(event.getPlayer()).delete();
		}
		arena.getScoreboard().getBoards().remove(event.getPlayer());
	}
	
	@EventHandler
	public void onGameStateChange(GameStateChangeEvent event){
		event.getArena().getScoreboard().update(UpdateablePlaceholders.GAMESTATE);
	}
	
	@EventHandler 
	public void onPlayerDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity();
		for(Arena arena : OneVsOne.getArena()) {
			for(Player fplayer : arena.getPlayers()) {
				if(fplayer == player) {
					Bukkit.getScheduler().runTaskLater(OneVsOne.getPlugin(), () -> {
					arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_1_HP);
					arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_2_HP);
					}, 1);
				}
			}
		}
		
	}
	
	@EventHandler
	public void onPlayerHeal(EntityRegainHealthEvent event) {
		if(event.getEntity() instanceof Player == false) {
			return;
		}
		Player player = (Player) event.getEntity();
		for(Arena arena : OneVsOne.getArena()) {
			for(Player fplayer : arena.getPlayers()) {
				if(fplayer == player) {
					Bukkit.getScheduler().runTaskLater(OneVsOne.getPlugin(), () -> {
						arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_1_HP);
						arena.getScoreboard().update(UpdateablePlaceholders.PLAYER_2_HP);
						}, 1);
				}
			}
		}
	}
	
}
