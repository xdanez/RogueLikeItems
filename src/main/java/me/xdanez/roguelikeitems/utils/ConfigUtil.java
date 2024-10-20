package me.xdanez.roguelikeitems.utils;

import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.Config;
import me.xdanez.roguelikeitems.enums.ConfigState;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.ConfigData;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

final public class ConfigUtil {
    public static ConfigState validateConfig() {
        Triple<ConfigState, Integer, Integer> validDurabilityRange = validRange(Config.DURABILITY_AMPLIFIER_RANGE);
        setRangeValues(validDurabilityRange.getMiddle(), validDurabilityRange.getRight(), Config.DURABILITY_AMPLIFIER_RANGE);

        Triple<ConfigState, Integer, Integer> validDamageAmplifierRange = validRange(Config.DAMAGE_AMPLIFIER_RANGE);
        setRangeValues(validDamageAmplifierRange.getMiddle(), validDamageAmplifierRange.getRight(), Config.DAMAGE_AMPLIFIER_RANGE);

        Triple<ConfigState, Integer, Integer> validMaxHealthAmplifierRange = validRange(Config.MAX_HEALTH_AMPLIFIER_RANGE);
        setRangeValues(validMaxHealthAmplifierRange.getMiddle(), validMaxHealthAmplifierRange.getRight(), Config.MAX_HEALTH_AMPLIFIER_RANGE);

        Pair<ConfigState, Boolean> validArmorDamageAmplifierTag = validateTag(Config.ARMOR_DAMAGE_AMPLIFIER);
        setTag(validArmorDamageAmplifierTag.getRight(), Config.ARMOR_DAMAGE_AMPLIFIER);

        Pair<ConfigState, Boolean> validNaturalNumbersTag = validateTag(Config.NATURAL_NUMBERS);
        setTag(validNaturalNumbersTag.getRight(), Config.NATURAL_NUMBERS);

        Pair<ConfigState, Boolean> validUseLootTables = validateTag(Config.USE_LOOT_TABLES);
        setTag(validUseLootTables.getRight(), Config.USE_LOOT_TABLES);

        Pair<ConfigState, Boolean> validUseMobDrops = validateTag(Config.USE_MOB_DROPS);
        setTag(validUseMobDrops.getRight(), Config.USE_MOB_DROPS);

        Pair<ConfigState, Boolean> validUseDurabilityAmplifier = validateTag(Config.USE_DURABILITY_AMPLIFIER);
        setTag(validUseDurabilityAmplifier.getRight(), Config.USE_DURABILITY_AMPLIFIER);

        Pair<ConfigState, Boolean> validUseDamageAmplifier = validateTag(Config.USE_DAMAGE_AMPLIFIER);
        setTag(validUseDamageAmplifier.getRight(), Config.USE_DAMAGE_AMPLIFIER);

        Pair<ConfigState, Boolean> validUseVillagerTrades = validateTag(Config.USE_VILLAGER_TRADES);
        setTag(validUseVillagerTrades.getRight(), Config.USE_VILLAGER_TRADES);

        Pair<ConfigState, Boolean> validUseCrafting = validateTag(Config.USE_CRAFTING);
        setTag(validUseCrafting.getRight(), Config.USE_CRAFTING);

        Pair<ConfigState, List<ItemStack>> validIgnoreList = validIgnoreList();
        ConfigData.getConfigData().setIgnoreItemList(validIgnoreList.getRight());

        Pair<ConfigState, Boolean> validUseMaxHealthAmplifier = validateTag(Config.USE_MAX_HEALTH_AMPLIFIER);
        setTag(validUseMaxHealthAmplifier.getRight(), Config.USE_MAX_HEALTH_AMPLIFIER);

        Pair<ConfigState, Boolean> validMaxHealthOnTool = validateTag(Config.MAX_HEALTH_ON_TOOLS);
        setTag(validMaxHealthOnTool.getRight(), Config.MAX_HEALTH_ON_TOOLS);

        if (validDurabilityRange.getLeft().equals(ConfigState.ERROR)
                || validDamageAmplifierRange.getLeft().equals(ConfigState.ERROR)
                || validArmorDamageAmplifierTag.getLeft().equals(ConfigState.ERROR)
                || validNaturalNumbersTag.getLeft().equals(ConfigState.ERROR)
                || validUseLootTables.getLeft().equals(ConfigState.ERROR)
                || validUseMobDrops.getLeft().equals(ConfigState.ERROR)
                || validUseDamageAmplifier.getLeft().equals(ConfigState.ERROR)
                || validUseDurabilityAmplifier.getLeft().equals(ConfigState.ERROR)
                || validUseVillagerTrades.getLeft().equals(ConfigState.ERROR)
                || validIgnoreList.getLeft().equals(ConfigState.ERROR)
                || validUseCrafting.getLeft().equals(ConfigState.ERROR)
                || validMaxHealthAmplifierRange.getLeft().equals(ConfigState.ERROR)
                || validUseMaxHealthAmplifier.getLeft().equals(ConfigState.ERROR)
                || validMaxHealthOnTool.getLeft().equals(ConfigState.ERROR)) {
            return ConfigState.ERROR;
        }

        if (validDurabilityRange.getLeft().equals(ConfigState.WARNING)
                || validDamageAmplifierRange.getLeft().equals(ConfigState.WARNING)
                || validDurabilityRange.getLeft().equals(ConfigState.WARNING)
                || validNaturalNumbersTag.getLeft().equals(ConfigState.WARNING)
                || validUseLootTables.getLeft().equals(ConfigState.WARNING)
                || validUseMobDrops.getLeft().equals(ConfigState.WARNING)
                || validUseDamageAmplifier.getLeft().equals(ConfigState.WARNING)
                || validUseDurabilityAmplifier.getLeft().equals(ConfigState.WARNING)
                || validUseVillagerTrades.getLeft().equals(ConfigState.WARNING)
                || validIgnoreList.getLeft().equals(ConfigState.WARNING)
                || validUseCrafting.getLeft().equals(ConfigState.WARNING)
                || validMaxHealthAmplifierRange.getLeft().equals(ConfigState.WARNING)
                || validUseMaxHealthAmplifier.getLeft().equals(ConfigState.WARNING)
                || validMaxHealthOnTool.getLeft().equals(ConfigState.WARNING)) {
            return ConfigState.WARNING;
        }

        return ConfigState.SUCCESS;
    }

    private static Pair<ConfigState, List<ItemStack>> validIgnoreList() {
        ArrayList<ItemStack> ignoreItemsList = new ArrayList<>();
        ConfigState state = ConfigState.SUCCESS;
        try {
            Object configVal = RogueLikeItems.getConfigVal(Config.IGNORE_ITEMS);
            if (!(configVal instanceof List)) {
                RogueLikeItems.logger().severe(Config.IGNORE_ITEMS.getVal() + " wrongfully declared");
                return Pair.of(ConfigState.ERROR, List.of());
            }

            List<?> ignoreItemsListConfig = (List<?>) configVal;

            if (ignoreItemsListConfig.isEmpty()) {
                return Pair.of(state, ignoreItemsList);
            }
            for (Object item : ignoreItemsListConfig) {
                String materialString =
                        item.toString()
                                .toUpperCase()
                                .replaceAll(" ", "_")
                                .replaceAll("-", "_");
                try {
                    Material material = Material.valueOf(materialString);
                    if (!ItemType.isModifiable(material)) {
                        RogueLikeItems.logger().warning(item + " is not modifiable");
                        state = ConfigState.WARNING;
                        continue;
                    }
                    ignoreItemsList.add(new ItemStack(material));
                } catch (IllegalArgumentException e) {
                    RogueLikeItems.logger().warning(item + " is not a valid item");
                    state = ConfigState.WARNING;
                }
            }
        } catch (IllegalArgumentException e) {
            RogueLikeItems.logger().severe(Config.IGNORE_ITEMS.getVal() + " wrongfully declared");
            return Pair.of(ConfigState.ERROR, ignoreItemsList);
        }
        return Pair.of(state, ignoreItemsList);
    }

    private static Pair<ConfigState, Boolean> validateTag(Config config) {
        boolean tag = RogueLikeItems.defaultConfig().getBoolean(config.getVal());
        try {
            Object configVal = RogueLikeItems.getConfigVal(config);
            if (!configVal.toString().equalsIgnoreCase("true")
                    && !configVal.toString().equalsIgnoreCase("false")) {
                RogueLikeItems.logger().severe(config.getVal() + " tag wrongfully declared. Using default value");
                return Pair.of(ConfigState.ERROR, tag);
            }
            tag = Boolean.parseBoolean(configVal.toString().trim());
        } catch (IllegalArgumentException e) {
            RogueLikeItems.logger().severe(config.getVal() + " tag wrongfully declared. Using default value");
            return Pair.of(ConfigState.ERROR, tag);
        }

        return Pair.of(ConfigState.SUCCESS, tag);
    }

    private static Triple<ConfigState, Integer, Integer> validRange(Config config) {
        List<Integer> defaultRange = RogueLikeItems.defaultConfig().getIntegerList(config.getVal());
        ConfigState state = ConfigState.SUCCESS;
        int from = defaultRange.get(0);
        int to = defaultRange.get(1);
        try {
            Object configVal = RogueLikeItems.getConfigVal(config);
            if (configVal instanceof List<?>) {
                List<?> range = (List<?>) configVal;
                if (range.isEmpty()) {
                    state = ConfigState.WARNING;
                    RogueLikeItems.logger().warning(config.getVal() + " is empty. Using default values.");
                    return Triple.of(state, from, to);
                }
                from = Integer.parseInt(range.get(0).toString());
                to = Integer.parseInt(range.get(1).toString());
            } else {
                int value = Integer.parseInt(configVal.toString());
                from = value;
                to = value;
            }
        } catch (IllegalArgumentException | ClassCastException e) {
            state = ConfigState.ERROR;
            RogueLikeItems.logger().severe(config.getVal() + " wrongfully declared. Using default values");

            return Triple.of(state, from, to);
        }

        if (!config.equals(Config.MAX_HEALTH_AMPLIFIER_RANGE) && from <= -100) {
            state = ConfigState.WARNING;
            RogueLikeItems.logger()
                    .warning("A value for " + config.getVal() + " is set to -100 or less. "
                            + "This can lead to " +
                            (config == Config.DURABILITY_AMPLIFIER_RANGE ? "items having no durability"
                                    : config == Config.DAMAGE_AMPLIFIER_RANGE ? "items doing no damage or even heal" : "problems"));
        }

        if (config.equals(Config.MAX_HEALTH_AMPLIFIER_RANGE) && from <= -20) {
            state = ConfigState.WARNING;
            RogueLikeItems.logger()
                    .warning("A value for " + config.getVal() + " is set to -20 or less. " +
                            "This can reduce the health to half a heart");
        }

        if (from > to) {
            RogueLikeItems.logger().warning(config.getVal() + " not in correct order");
            return Triple.of(ConfigState.WARNING, to, from);
        }

        return Triple.of(state, from, to);
    }

    private static void setRangeValues(int from, int to, Config config) {
        ConfigData configData = ConfigData.getConfigData();
        switch (config) {
            case DURABILITY_AMPLIFIER_RANGE: {
                configData.setDurabilityRange(from, to);
                break;
            }
            case DAMAGE_AMPLIFIER_RANGE: {
                configData.setDamageAmplifierRange(from, to);
                break;
            }
            case MAX_HEALTH_AMPLIFIER_RANGE:
                configData.setMaxHealthAmplifierRange(from, to);
                break;
        }
    }

    private static void setTag(boolean tag, Config config) {
        ConfigData configData = ConfigData.getConfigData();
        switch (config) {
            case USE_DURABILITY_AMPLIFIER:
                configData.setUseDurabilityAmplifier(tag);
                break;
            case USE_DAMAGE_AMPLIFIER:
                configData.setUseDamageAmplifier(tag);
                break;
            case ARMOR_DAMAGE_AMPLIFIER:
                configData.setUseArmorDamageAmplifier(tag);
                break;
            case NATURAL_NUMBERS:
                configData.setUseNaturalNumbers(tag);
                break;
            case USE_LOOT_TABLES:
                configData.setUseLootTables(tag);
                break;
            case USE_MOB_DROPS:
                configData.setUseMobDrops(tag);
                break;
            case USE_VILLAGER_TRADES:
                configData.setUseVillagerTrades(tag);
                break;
            case USE_CRAFTING:
                configData.setUseCrafting(tag);
                break;
            case USE_MAX_HEALTH_AMPLIFIER:
                configData.setUseMaxHealthAmplifier(tag);
                break;
            case MAX_HEALTH_ON_TOOLS:
                configData.setMaxHealthOnTools(tag);
                break;
        }
    }
}
