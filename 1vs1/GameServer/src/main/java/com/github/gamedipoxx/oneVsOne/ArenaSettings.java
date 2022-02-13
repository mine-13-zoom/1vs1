package com.github.gamedipoxx.oneVsOne;

public enum ArenaSettings {
	
    FALL_DAMAGE(OneVsOne.getPlugin().getConfig().getBoolean("Arenasettings.FallDamage")),
    BLOCK_BREAKING(OneVsOne.getPlugin().getConfig().getBoolean("Arenasettings.FallDamage")),
    BLOCK_PLACING(OneVsOne.getPlugin().getConfig().getBoolean("Arenasettings.FallDamage")),
    DROP_ITEMS(OneVsOne.getPlugin().getConfig().getBoolean("Arenasettings.FallDamage")),
    ITEM_DAMAGE(OneVsOne.getPlugin().getConfig().getBoolean("Arenasettings.ItemDamage")),
    ITEM_PICKUP(OneVsOne.getPlugin().getConfig().getBoolean("Arenasettings.ItemPickup"));
	
	private Boolean value;
	
    ArenaSettings(boolean value) {
        this.value = value;
    }
    
    public Boolean getValue() {
		return value;
	}
}
