package me.xdanez.roguelikeitems.enums;

public enum ConfigSetting implements ConfigType {
    USE_LOOT_TABLES("use-loot-tables"),
    USE_MOB_DROPS("use-mob-drops"),
    USE_VILLAGER_TRADES("use-villager-trades"),
    USE_CRAFTING("use-crafting"),
    USE_BARTERING("use-bartering"),
    USE_VAULT("use-vault"),
    IGNORE_ITEMS("ignore-items"),
    INCLUDE_ITEMS("include-items"),
    BOW_MAINHAND_ATTACK("bow-mainhand-attack"),
    MODIFY_PROJECTILE_DAMAGE("modify-projectile-damage"),
    SHOW_ADJUSTED_VALUES("show-adjusted-values"),
    USE_LEGACY_DURABILITY_LORE("use-legacy-durability-lore");

    private final String key;

    ConfigSetting(String key) {
        this.key = key;
    }

    public String toString() {
        return key;
    }
}
