package me.xdanez.roguelikeitems.enums;

public enum Config {
    DURABILITY_AMPLIFIER_RANGE("durability-amplifier-range"),
    DAMAGE_AMPLIFIER_RANGE("damage-amplifier-range"),
    IGNORE_ITEMS("ignore-items"),
    USE_DURABILITY_AMPLIFIER("use-durability-amplifier"),
    USE_DAMAGE_AMPLIFIER("use-damage-amplifier"),
    ARMOR_DAMAGE_AMPLIFIER("armor-damage-amplifier"),
    ONLY_NATURAL_NUMBERS("only-natural-numbers"),
    USE_LOOT_TABLES("use-loot-tables"),
    USE_MOB_DROPS("use-mob-drops"),
    USE_VILLAGER_TRADES("use-villager-trades"),
    USE_CRAFTING("use-crafting"),
    MAX_HEALTH_AMPLIFIER_RANGE("max-health-amplifier-range"),
    USE_MAX_HEALTH_AMPLIFIER("use-max-health-amplifier"),
    MAX_HEALTH_TOOLS("max-health-tools"),
    AMPLIFIER_CHANCE("amplifier-chance"),
    AC_DURABILITY(AMPLIFIER_CHANCE + ".durability"),
    AC_DAMAGE(AMPLIFIER_CHANCE + ".damage"),
    AC_MAX_HEALTH(AMPLIFIER_CHANCE + ".max-health");

    private final String val;
    Config(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
