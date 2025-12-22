package me.xdanez.roguelikeitems.enums;

public enum ConfigSetting implements ConfigType {
    USE_LOOT_TABLES("use-loot-tables"),
    USE_MOB_DROPS("use-mob-drops"),
    USE_VILLAGER_TRADES("use-villager-trades"),
    USE_CRAFTING("use-crafting"),
    USE_BARTERING("use-bartering"),
    IGNORE_ITEMS("ignore-items"),
    BOW_MAINHAND_ATTACK("bow-mainhand-attack"),
    SHOW_ADJUSTED_VALUES("show-adjusted-values");

    private final String key;

    ConfigSetting(String key) {
        this.key = key;
    }

    public String toString() {
        return key;
    }
}
