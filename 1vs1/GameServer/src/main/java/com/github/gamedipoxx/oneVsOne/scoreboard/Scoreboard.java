package com.github.gamedipoxx.oneVsOne.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.utils.MessagesFile;

import fr.mrmicky.fastboard.FastBoard;

public class Scoreboard{
	
	private PlaceHolders placeHolders;
	private final String title = ChatColor.translateAlternateColorCodes('&', MessagesFile.getMessageConfig().getString("ScoreboardTitle"));
	private HashMap<Player, FastBoard> boards = new HashMap<>();
	private Arena arena;
	
	public Scoreboard(Arena arena) {
		this.placeHolders = new PlaceHolders(arena);
		for(UpdateablePlaceholders placeholder : UpdateablePlaceholders.values()) {
			placeHolders.update(placeholder);
		}
		this.arena = arena;
	}
	
	public void update(UpdateablePlaceholders placeholder) {
		placeHolders.update(placeholder);
		for(Player player : arena.getPlayers()) {
			if(boards.get(player) == null) {
				break;
			}
			if(boards.get(player).isDeleted()) {
				break;
			}
			arena.getScoreboard().getBoards().get(player).updateLines(arena.getScoreboard().getScoreboardContent());
		}
	}
	
	public List<String> getScoreboardContent() {
		List<?> objectList = MessagesFile.getMessageConfig().getList("ScoreboardContent");
		List<String> list = new ArrayList<>();
		for(Object object : objectList) {
			String stringfromobject = (String) object;
			
			stringfromobject = ChatColor.translateAlternateColorCodes('&', stringfromobject);
			stringfromobject = stringfromobject.replace("%player1name%", placeHolders.getPlayer1name());
			stringfromobject = stringfromobject.replace("%player1hp%", placeHolders.getPlayer1hp());
			stringfromobject = stringfromobject.replace("%player2name%", placeHolders.getPlayer2name());
			stringfromobject = stringfromobject.replace("%player2hp%", placeHolders.getPlayer2hp());
			stringfromobject = stringfromobject.replace("%arenaname%", placeHolders.getArenaname());
			stringfromobject = stringfromobject.replace("%arenamap%", placeHolders.getArenaKit());
			stringfromobject = stringfromobject.replace("%gamestate%", placeHolders.getGameState());
			stringfromobject = stringfromobject.replace("%version%", placeHolders.getVersion());
			stringfromobject = stringfromobject.replace("%server%", placeHolders.getServer());
			
			list.add(stringfromobject);
		}
		
		return list;
	}

	public String getTitle() {
		return title;
	}

	public HashMap<Player, FastBoard> getBoards() {
		return boards;
	}

	public void setBoards(HashMap<Player, FastBoard> boards) {
		this.boards = boards;
	}
}
