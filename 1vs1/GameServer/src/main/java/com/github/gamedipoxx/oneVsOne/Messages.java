package com.github.gamedipoxx.oneVsOne;

public enum Messages {
    PREFIX("§7[§f§lGamedipoxx§r§7] §r"), 
    TELEPORTTOLOBBY("§7Du wurdest zur Lobby teleportiert"), 
    NOARENAFOUND("§cEs konnte keine Arena gefunden werden"), 
    COMMANDS("§cCommands: /onevsone [create, join, delete, list, delete, leave]"),
    NOARENAAVAIBLE("§cKeine Arena verfügbar!"),
    CONFIGRELOADED("§2Die Konfigurationen wurden neu geladen!"),
	WAITINGFORMOREPLAYER("§cWarte auf weitere Spieler"),
	STARTINGGAME("§2Das Spiel startet jetzt"),
	PLAYERWIN("§2hat gewonnen"),
	PLAYERDIED("§cist gestorben!"),
	STARTTITLE("§4Kämpft"),
	STARTSUBTITLE("§7um Leben und Tot"),
	PLAYERLEAVEARENA("§chat das Spiel verlassen!")
	;
 
    private String string;
 
    Messages(String string) {
        this.string = string;
    }
 
    public String getString() {
        return string;
    }
}


