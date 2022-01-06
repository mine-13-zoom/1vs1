package com.github.gamedipoxx.oneVsOneLobby;

public enum LobbyMessages {
    PREFIX("§7[§f§lGamedipoxx§r§7] §r"), 
    WRONGARGS("§cFalsche Argumente! Nutze /onevsonelobby [join, list]"),
    NOPERMISSION("§cDu hast dazu keine Rechte!"),
    WRONGJOINARG("§cNutze /onevsonelobby join arenaname"),
    CONNECTING("§2Du wirst verbunden"),
    WRONGLISTARGS("§cNutze /onevsonelobby list!"),
    FETCHING("§2Lade Daten"),
    JOINSIGN_LINE1("§e§l1vs1"),
    JOINSIGN_LINE2("§dKlicke"),
    JOINSIGN_LINE3("§dzum"),
    JOINSIGN_LINE4("§dbeitreten"),
	WRONGSIGNCREATE("§cDieser Block ist kein Schild!"),
	SIGNCREATESUCESS("Schild erfolgreich erstellt");
 
    private String string;
 
    LobbyMessages(String string) {
        this.string = string;
    }
 
    public String getString() {
        return string;
    }
}
