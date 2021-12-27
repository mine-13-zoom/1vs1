package com.github.gamedipoxx.oneVsOneLobby;

public enum LobbyMessages {
    PREFIX("§7[§f§lGamedipoxx§r§7] §r"), 
    WRONGARGS("§cFalsche Argumente! Nutze /onevsonelobby [join, list]"),
    NOPERMISSION("§cDu hast dazu keine Rechte!"),
    WRONGJOINARG("§cNutze /onevsonelobby join arenaname"),
    CONNECTING("§2Du wirst verbunden"),
    WRONGLISTARGS("§cNutze /onevsonelobby list!"),
    FETCHING("§2Lade Daten"),
    JOINSIGN_LINE1("§7--§2§l1vs1§r§7--"),
    JOINSIGN_LINE4("§7---------")
	;
 
    private String string;
 
    LobbyMessages(String string) {
        this.string = string;
    }
 
    public String getString() {
        return string;
    }
}
