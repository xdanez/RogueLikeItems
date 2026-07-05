package me.xdanez.roguelikeitems.models;

import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.ConfigSetting;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigData {

    private static final ConfigData CONFIG_DATA = new ConfigData();

    private ConfigData() {
    }

    public static ConfigData getConfigData() {
        return CONFIG_DATA;
    }

    private List<Material> ignoreItemsList;
    private List<Material> includeItemsList;
    private Map<Attribute, CustomAttributeModifier> customAttributeModifiers;

    @Nullable
    public CustomAttributeModifier getCustomAttributeModifier(Attribute attribute) {
        return customAttributeModifiers.getOrDefault(attribute, null);
    }

    public Map<Attribute, CustomAttributeModifier> getCustomAttributeModifierCopy() {
        return new HashMap<>(customAttributeModifiers);
    }

    public void setCustomAttributeModifier(Map<Attribute, CustomAttributeModifier> customAttributeModifiers) {
        this.customAttributeModifiers = customAttributeModifiers;
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

    public boolean useVault() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.USE_VAULT.toString());
    }

    public boolean modifyItemframeContents() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.MODIFY_ITEMFRAME_CONTENTS.toString());
    }

    public boolean useBowMainHandAttack() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.BOW_MAINHAND_ATTACK.toString());
    }

    public boolean modifyProjectileDamage() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.MODIFY_PROJECTILE_DAMAGE.toString());
    }

    public boolean showAdjustedValues() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.SHOW_ADJUSTED_VALUES.toString());
    }

    public boolean useLegacyDurabilityLore() {
        return RogueLikeItems.config().getBoolean(ConfigSetting.USE_LEGACY_DURABILITY_LORE.toString());
    }

    public List<Material> getIgnoreItemsList() {
        return ignoreItemsList;
    }

    public void setIgnoreItemsList(List<Material> materials) {
        ignoreItemsList = materials;
    }

    public List<Material> getIncludeItemsList() {
        return includeItemsList;
    }

    public void setIncludeItemsList(List<Material> materials) {
        includeItemsList = materials;
    }
}
