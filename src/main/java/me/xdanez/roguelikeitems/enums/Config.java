package me.xdanez.roguelikeitems.enums;

public enum Config implements ConfigType {
    IGNORE_ITEMS("ignore-items"),
    ACTIVE("active"),
    IN_PERCENT("in-percent"),
    RANGE("range"),
    CHANCE("chance"),
    TOOLS_AND_WEAPONS("tools-and-weapons"),
    ARMOR_AND_SHIELD("armor-and-shield"),
    USE_ONLY_NATURAL_NUMBERS("use-only-natural-numbers");

    private final String key;

    Config(String key) {
        this.key = key;
    }

    public String toString() {
        return key;
    }
}
