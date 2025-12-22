package me.xdanez.roguelikeitems.models;

import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.ConfigSetting;
import org.bukkit.Material;

import java.util.List;

public class ConfigData {

    private static final ConfigData CONFIG_DATA = new ConfigData();

    private ConfigData() {
    }

    public static ConfigData getConfigData() {
        return CONFIG_DATA;
    }

    private List<Material> ignoreItemList = List.of();
    private List<CustomAttributeModifier> customAttributeModifiers = List.of();

    public List<CustomAttributeModifier> getCustomAttributeModifiers() {
        return customAttributeModifiers;
    }

    public void setCustomAttributeModifier(List<CustomAttributeModifier> customAttributeModifiers) {
        this.customAttributeModifiers = customAttributeModifiers;
    }

    public CustomAttributeModifier getDurabilityModifier() {
        return customAttributeModifiers.stream()
                .filter(c -> c.attribute() == null).findFirst().orElse(null);
    }

    public boolean useCrafting() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.USE_CRAFTING.toString());
    }

    public boolean useVillagerTrades() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.USE_VILLAGER_TRADES.toString());
    }

    public boolean useLootTables() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.USE_LOOT_TABLES.toString());
    }

    public boolean useMobDrops() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.USE_MOB_DROPS.toString());
    }

    public boolean useBartering() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.USE_BARTERING.toString());
    }

    public boolean useBowMainHandAttack() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.BOW_MAINHAND_ATTACK.toString());
    }

    public boolean showAdjustedValues() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.SHOW_ADJUSTED_VALUES.toString());
    }

    public List<Material> getIgnoreItemList() {
        return ignoreItemList;
    }

    public void setIgnoreItemList(List<Material> ignoreItemList) {
        this.ignoreItemList = ignoreItemList;
    }
}
