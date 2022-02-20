package com.github.gamedipoxx.oneVsOneLobby;

import com.github.gamedipoxx.oneVsOne.utils.MessagesFile;

import net.md_5.bungee.api.ChatColor;

public enum LobbyMessages {
	
    PREFIX(MessagesFile.getMessageConfig().getString("Prefix")), 
    WRONGARGS(MessagesFile.getMessageConfig().getString("WrongArgs")),
    NOPERMISSION(MessagesFile.getMessageConfig().getString("NoPermission")),
    WRONGJOINARG(MessagesFile.getMessageConfig().getString("WrongJoinArgs")),
    CONNECTING(MessagesFile.getMessageConfig().getString("Connecting")),
    WRONGLISTARGS(MessagesFile.getMessageConfig().getString("WrongListArgs")),
    FETCHING(MessagesFile.getMessageConfig().getString("Fetching")),
    JOINSIGN_LINE1(MessagesFile.getMessageConfig().getString("JoinSignLine1")),
    JOINSIGN_LINE2(MessagesFile.getMessageConfig().getString("JoinSignLine2")),
    JOINSIGN_LINE3(MessagesFile.getMessageConfig().getString("JoinSignLine3")),
    JOINSIGN_LINE4(MessagesFile.getMessageConfig().getString("JoinSignLine4")),
	WRONGSIGNCREATE(MessagesFile.getMessageConfig().getString("WrongSignCreate")),
	SIGNCREATESUCESS(MessagesFile.getMessageConfig().getString("SignCreateSucess")),
	PLEASEWAIT(MessagesFile.getMessageConfig().getString("PleaseWait")),
	ARENA(MessagesFile.getMessageConfig().getString("Arena")),
	PLAYER(MessagesFile.getMessageConfig().getString("Player")),
	STATUS(MessagesFile.getMessageConfig().getString("Status")),
	KIT(MessagesFile.getMessageConfig().getString("Kit")),
	GUITITLE(MessagesFile.getMessageConfig().getString("GuiTitle")),
	GAMESWON(MessagesFile.getMessageConfig().getString("GamesWon")),
	GAMESLOST(MessagesFile.getMessageConfig().getString("GamesLost")),
	GAMESPLAYED(MessagesFile.getMessageConfig().getString("GamesPlayed")),
	PRIVATESTATS(MessagesFile.getMessageConfig().getString("PrivateStats")),
	GLOBALSTATS(MessagesFile.getMessageConfig().getString("GlobalStats")),
	BRANDING("§f§l1vs1 §7 by ProfessorSam. Discord: ProfSam#3975 " + " Github: ProfessorSam");
 
    private String string;
 
    LobbyMessages(String string) {
    	this.string = string;
    }
    
    public String getString() {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}

