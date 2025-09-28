package me.xdanez.roguelikeitems.models;

import it.unimi.dsi.fastutil.Pair;
import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.Config;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConfigData {

    private static final ConfigData CONFIG_DATA = new ConfigData();

    private ConfigData() {
    }

    public static ConfigData getConfigData() {
        return CONFIG_DATA;
    }

    private Pair<Integer, Integer> durabilityRange;
    private Pair<Integer, Integer> damageAmplifierRange;
    private Pair<Integer, Integer> maxHealthAmplifierRange;
    private List<ItemStack> ignoreItemList;

    public Pair<Integer, Integer> getRange(Config config) {
        switch (config) {
            case DAMAGE_AMPLIFIER_RANGE:
                return getDamageAmplifierRange();
            case MAX_HEALTH_AMPLIFIER_RANGE:
                return getMaxHealthAmplifierRange();
            case DURABILITY_AMPLIFIER_RANGE:
                return getDurabilityRange();
            default:
                throw new IllegalArgumentException("Given config is no range");
        }
    }

    public int getAmplifierChance(Config config) {
        switch (config) {
            case AC_DAMAGE:
                return RogueLikeItems.config().getInt(Config.AC_DAMAGE.toString());
            case AC_MAX_HEALTH:
                return RogueLikeItems.config().getInt(Config.AC_MAX_HEALTH.toString());
            case AC_DURABILITY:
                return RogueLikeItems.config().getInt(Config.AC_DURABILITY.toString());
            default:
                throw new IllegalArgumentException("Given config is no amplifier chance");
        }
    }

    public Pair<Integer, Integer> getMaxHealthAmplifierRange() {
        return maxHealthAmplifierRange;
    }

    public void setMaxHealthAmplifierRange(int from, int to) {
        this.maxHealthAmplifierRange = Pair.of(from, to);
    }

    public boolean useMaxHealthOnTools() {
        return RogueLikeItems.config().getBoolean(Config.MAX_HEALTH_TOOLS.toString());
    }

    public boolean useMaxHealthAmplifier() {
        return RogueLikeItems.config().getBoolean(Config.USE_MAX_HEALTH_AMPLIFIER.toString());
    }

    public boolean useCrafting() {
        return RogueLikeItems.config().getBoolean(Config.USE_CRAFTING.toString());
    }

    public boolean useVillagerTrades() {
        return RogueLikeItems.config().getBoolean(Config.USE_VILLAGER_TRADES.toString());
    }

    public Pair<Integer, Integer> getDurabilityRange() {
        return durabilityRange;
    }

    public void setDurabilityRange(Integer from, Integer to) {
        this.durabilityRange = Pair.of(from, to);
    }

    public Pair<Integer, Integer> getDamageAmplifierRange() {
        return damageAmplifierRange;
    }

    public void setDamageAmplifierRange(Integer from, Integer to) {
        this.damageAmplifierRange = Pair.of(from, to);
    }

    public boolean useArmorDamageAmplifier() {
        return RogueLikeItems.config().getBoolean(Config.ARMOR_DAMAGE_AMPLIFIER.toString());
    }

    public boolean useNaturalNumbers() {
        return RogueLikeItems.config().getBoolean(Config.NATURAL_NUMBERS.toString());
    }

    public boolean useLootTables() {
        return RogueLikeItems.config().getBoolean(Config.USE_LOOT_TABLES.toString());
    }

    public boolean useMobDrops() {
        return RogueLikeItems.config().getBoolean(Config.USE_MOB_DROPS.toString());
    }

    public boolean useDurabilityAmplifier() {
        return RogueLikeItems.config().getBoolean(Config.USE_DURABILITY_AMPLIFIER.toString());
    }

    public boolean useDamageAmplifier() {
        return RogueLikeItems.config().getBoolean(Config.USE_DAMAGE_AMPLIFIER.toString());
    }

    public List<ItemStack> getIgnoreItemList() {
        return ignoreItemList;
    }

    public void setIgnoreItemList(List<ItemStack> ignoreItemList) {
        this.ignoreItemList = ignoreItemList;
    }
}
