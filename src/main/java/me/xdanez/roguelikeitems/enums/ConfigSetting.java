package me.xdanez.roguelikeitems.enums;

public enum ConfigSetting {
    USE_LOOT_TABLES("use-loot-tables"),
    USE_MOB_DROPS("use-mob-drops"),
    USE_VILLAGER_TRADES("use-villager-trades"),
    USE_CRAFTING("use-crafting"),
    IGNORE_ITEMS("ignore-items");

    private final String key;
    ConfigSetting(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
