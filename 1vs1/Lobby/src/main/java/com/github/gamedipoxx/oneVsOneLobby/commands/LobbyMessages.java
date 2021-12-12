package com.github.gamedipoxx.oneVsOneLobby.commands;

public enum LobbyMessages {
    PREFIX("§7[§f§lGamedipoxx§r§7] §r"), 
    WRONGARGS("§cFalsche Argumente! Nutze /onevsonelobby [join, list]"),
    NOPERMISSION("§cDu hast dazu keine Rechte!"),
    WRONGJOINARG("§cNutze /onevsonelobby join arenaname"),
    CONNECTING("§2Du wirst verbunden"),
    WRONGLISTARGS("§c /onevsonelobby list!")
	;
 
    private String string;
 
    LobbyMessages(String string) {
        this.string = string;
    }
 
    public String getString() {
        return string;
    }
}
