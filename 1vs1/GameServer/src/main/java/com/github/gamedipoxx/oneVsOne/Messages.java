package com.github.gamedipoxx.oneVsOne;

import com.github.gamedipoxx.oneVsOne.utils.MessagesFile;

import net.md_5.bungee.api.ChatColor;

public enum Messages {
	
    PREFIX(MessagesFile.getMessageConfig().getString("Prefix")), 
    TELEPORTTOLOBBY(MessagesFile.getMessageConfig().getString("TeleportToLobby")), 
    NOARENAFOUND(MessagesFile.getMessageConfig().getString("NoArenaFound")), 
    COMMANDS(MessagesFile.getMessageConfig().getString("Commands")),
    NOARENAAVAIBLE(MessagesFile.getMessageConfig().getString("NoArenaAvaible")),
    CONFIGRELOADED(MessagesFile.getMessageConfig().getString("ConfigReloaded")),
	WAITINGFORMOREPLAYER(MessagesFile.getMessageConfig().getString("WaitingForMorePlayer")),
	STARTINGGAME(MessagesFile.getMessageConfig().getString("StartingGame")),
	PLAYERWIN(MessagesFile.getMessageConfig().getString("PlayerWin")),
	PLAYERDIED(MessagesFile.getMessageConfig().getString("PlayerDied")),
	STARTTITLE(MessagesFile.getMessageConfig().getString("StartTitle")),
	STARTSUBTITLE(MessagesFile.getMessageConfig().getString("StartSubTitle")),
	PLAYERLEAVEARENA(MessagesFile.getMessageConfig().getString("PlayerLeaveArena")),
	LOADDATATITLE(MessagesFile.getMessageConfig().getString("LoadDataTitle")),
	NOPERMISSION(MessagesFile.getMessageConfig().getString("NoPermission")),
	SETUPLIST(MessagesFile.getMessageConfig().getString("SetupList")),
	SETUPSPECIFYNAME(MessagesFile.getMessageConfig().getString("SetupSpecifyName")),
	OK(MessagesFile.getMessageConfig().getString("Ok")),
	PLEASEENABLESETUPMODE(MessagesFile.getMessageConfig().getString("PleaseEnableSetupMode")),
	SETUPFORGET(MessagesFile.getMessageConfig().getString("SetupForget")),
	SETUPKITNAME(MessagesFile.getMessageConfig().getString("SetupKitname")),
	SETUPSPAWN1(MessagesFile.getMessageConfig().getString("SetupSpawn1")),
	SETUPSPAWN2(MessagesFile.getMessageConfig().getString("SetupSpawn2")),
	SETUPKIT(MessagesFile.getMessageConfig().getString("SetupKit")),
	RELOADERROR(MessagesFile.getMessageConfig().getString("ReloadError")),
	GETREADY(MessagesFile.getMessageConfig().getString("GetReady")),
	LEAVEITEM(MessagesFile.getMessageConfig().getString("LeaveItem")),
	BRANDING("§f§l1vs1 §7by Professor_Sam. Discord: ProfSam#3975 GitHub: ProfessorSam")
	;
 
    private String string;
    
 
    Messages(String string) {
        this.string = string;
    }
 
    public String getString() {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}


