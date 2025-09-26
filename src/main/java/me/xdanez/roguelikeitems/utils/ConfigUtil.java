package me.xdanez.roguelikeitems.utils;

import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.Config;
import me.xdanez.roguelikeitems.enums.ConfigState;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.AmplifierChance;
import me.xdanez.roguelikeitems.models.ConfigData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

final public class ConfigUtil {
    final static Config[] ranges = new Config[]{
            Config.DURABILITY_AMPLIFIER_RANGE,
            Config.DAMAGE_AMPLIFIER_RANGE,
            Config.MAX_HEALTH_AMPLIFIER_RANGE
    };

    final static Config[] tags = new Config[]{
            Config.USE_DURABILITY_AMPLIFIER,
            Config.USE_DAMAGE_AMPLIFIER,
            Config.USE_MAX_HEALTH_AMPLIFIER,
            Config.MAX_HEALTH_TOOLS,
            Config.NATURAL_NUMBERS,
            Config.USE_LOOT_TABLES,
            Config.USE_MOB_DROPS,
            Config.USE_VILLAGER_TRADES,
            Config.USE_CRAFTING
    };

    final static Config[] amplifierChances = new Config[]{
            Config.AC_DURABILITY,
            Config.AC_DAMAGE,
            Config.AC_MAX_HEALTH
    };

    static List<ConfigState> states = new ArrayList<>();

    public static boolean useAmplifier(Config config) {
        ConfigData configData = ConfigData.getConfigData();
        AmplifierChance chance = configData.getAmplifierChance();
        int chanceValue = ThreadLocalRandom.current().nextInt(100 + 1);

        switch (config) {
            case USE_DURABILITY_AMPLIFIER:
            case DURABILITY_AMPLIFIER_RANGE:
                return configData.useDurabilityAmplifier() && chance.getDurability() >= chanceValue;
            case USE_MAX_HEALTH_AMPLIFIER:
            case MAX_HEALTH_AMPLIFIER_RANGE:
                return configData.useMaxHealthAmplifier() && chance.getMaxHealth() >= chanceValue;
            case USE_DAMAGE_AMPLIFIER:
            case DAMAGE_AMPLIFIER_RANGE:
                return configData.useDamageAmplifier() && chance.getDamage() >= chanceValue;
            default:
                throw new IllegalArgumentException("Given Config is no amplifier");
        }
    }

    public static List<ConfigState> validateConfig() {
        states.clear();
        validateRanges();
        validateTags();
        validateIgnoreList();
        validateAmplifierChances();
        return states;
    }

    private static void validateIgnoreList() {
        ArrayList<ItemStack> ignoreItemsList = new ArrayList<>();
        try {
            Object configVal = RogueLikeItems.getConfigVal(Config.IGNORE_ITEMS);
            if (!(configVal instanceof List)) {
                RogueLikeItems.logger().severe(Config.IGNORE_ITEMS.getVal() + " wrongfully declared");
                states.add(ConfigState.ERROR);
                setIgnoreList(ignoreItemsList);
                return;
            }

            List<?> ignoreItemsListConfig = (List<?>) configVal;

            if (ignoreItemsListConfig.isEmpty()) {
                setIgnoreList(ignoreItemsList);
                return;
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
                        states.add(ConfigState.WARNING);
                        RogueLikeItems.logger().warning(item + " is not modifiable");
                        continue;
                    }
                    ignoreItemsList.add(new ItemStack(material));
                } catch (IllegalArgumentException e) {
                    states.add(ConfigState.WARNING);
                    RogueLikeItems.logger().warning(item + " is not a valid item");
                }
            }
        } catch (IllegalArgumentException e) {
            states.add(ConfigState.WARNING);
            RogueLikeItems.logger().severe(Config.IGNORE_ITEMS.getVal() + " wrongfully declared");
            return;
        }
        setIgnoreList(ignoreItemsList);
    }

    private static void validateTags() {
        for (Config config : ConfigUtil.tags) {
            boolean tag = RogueLikeItems.defaultConfig().getBoolean(config.getVal());
            try {
                Object configVal = RogueLikeItems.getConfigVal(config);
                if (!configVal.toString().equalsIgnoreCase("true")
                        && !configVal.toString().equalsIgnoreCase("false")) {
                    RogueLikeItems.logger().severe(config.getVal() + " tag wrongfully declared. Using default value");
                    setTag(tag, config);
                    states.add(ConfigState.ERROR);
                    continue;
                }
                tag = Boolean.parseBoolean(configVal.toString().trim());
                setTag(tag, config);
            } catch (IllegalArgumentException e) {
                RogueLikeItems.logger().severe(config.getVal() + " tag wrongfully declared. Using default value");
                states.add(ConfigState.ERROR);
            }
        }
    }

    private static void validateRanges() {
        for (Config config : ConfigUtil.ranges) {
            List<Integer> defaultRange = RogueLikeItems.defaultConfig().getIntegerList(config.getVal());
            int from = defaultRange.get(0);
            int to = defaultRange.get(1);
            try {
                Object configVal = RogueLikeItems.getConfigVal(config);
                if (configVal instanceof List<?>) {
                    List<?> range = (List<?>) configVal;
                    if (range.isEmpty()) {
                        states.add(ConfigState.WARNING);
                        RogueLikeItems.logger().warning(config.getVal() + " is empty. Using default values.");
                        setRangeValues(from, to, config);
                        continue;
                    }
                    from = Integer.parseInt(range.get(0).toString());
                    to = Integer.parseInt(range.get(1).toString());
                } else {
                    int value = Integer.parseInt(configVal.toString());
                    from = value;
                    to = value;
                }
            } catch (IllegalArgumentException | ClassCastException e) {
                states.add(ConfigState.ERROR);
                RogueLikeItems.logger().severe(config.getVal() + " wrongfully declared. Using default values");
                setRangeValues(from, to, config);
                continue;
            }

            if (!config.equals(Config.MAX_HEALTH_AMPLIFIER_RANGE) && from <= -100) {
                RogueLikeItems.logger()
                        .warning("A value for " + config.getVal() + " is set to -100 or less. "
                                + "This can lead to " +
                                (config == Config.DURABILITY_AMPLIFIER_RANGE ? "items having no durability"
                                        : config == Config.DAMAGE_AMPLIFIER_RANGE ? "items doing no damage or even heal" : "problems"));
            }

            if (config.equals(Config.MAX_HEALTH_AMPLIFIER_RANGE) && from <= -20) {
                states.add(ConfigState.WARNING);
                RogueLikeItems.logger()
                        .warning("A value for " + config.getVal() + " is set to -20 or less. " +
                                "This can reduce the health to half a heart");
            }

            if (from > to) {
                states.add(ConfigState.WARNING);
                RogueLikeItems.logger().warning(config.getVal() + " not in correct order");
                setRangeValues(to, from, config);
            }
            setRangeValues(from, to, config);
        }
    }

    private static void validateAmplifierChances() {
        AmplifierChance amplifierChance = new AmplifierChance(100, 100, 100);

        for (Config acConfig : amplifierChances) {
            int defaultChance = RogueLikeItems.defaultConfig().getInt(acConfig.getVal());
            int chance;

            try {
                Object configVal = RogueLikeItems.getConfigVal(acConfig);
                chance = (int) configVal;
                if (chance < 0 || chance > 100) {
                    RogueLikeItems.logger().severe(acConfig + " must be a between 0 and 100");
                    states.add(ConfigState.ERROR);
                    amplifierChance.setAmplifierChance(acConfig, defaultChance);
                    return;
                }
                amplifierChance.setAmplifierChance(acConfig, chance);
            } catch (IllegalArgumentException | ClassCastException | NullPointerException e) {
                states.add(ConfigState.ERROR);
                RogueLikeItems.logger().severe(acConfig.getVal() + " must be a number");
                amplifierChance.setAmplifierChance(acConfig, defaultChance);
                return;
            }
        }
        setAmplifierChances(amplifierChance);
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

    private static void setAmplifierChances(AmplifierChance amplifierChance) {
        ConfigData configData = ConfigData.getConfigData();
        configData.setAmplifierChance(amplifierChance);
    }

    private static void setIgnoreList(List<ItemStack> items) {
        ConfigData configData = ConfigData.getConfigData();
        configData.setIgnoreItemList(items);
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
            case MAX_HEALTH_TOOLS:
                configData.setMaxHealthOnTools(tag);
                break;
        }
    }
}
