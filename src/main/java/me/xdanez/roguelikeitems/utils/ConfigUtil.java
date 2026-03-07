package me.xdanez.roguelikeitems.utils;

import it.unimi.dsi.fastutil.Pair;
import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.*;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.models.CustomAttributeModifier;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.yaml.snakeyaml.constructor.ConstructorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

final public class ConfigUtil {
    private static final ConfigData configData = ConfigData.getConfigData();

    static int amtWarnings = 0;
    static int amtErrors = 0;

    private static final ConfigSetting[] settingKeys = ConfigSetting.values();

    public static boolean useAmplifier(CustomAttributeModifier cam, Material material) {
        if (ConfigData.getConfigData().getIgnoreItemsList().contains(material)) return false;
        if (cam.ignoreItemsList().contains(material)) return false;
        if (ItemType.isWeaponOrTool(material) && !cam.useToolsAndWeapons()) return false;
        if (ItemType.isArmorOrShield(material) && !cam.useArmorAndShield()) return false;

        return ThreadLocalRandom.current().nextInt(100 + 1) <= cam.chance();
    }

    public static Pair<Integer, Integer> validateConfig() {
        try {
            RogueLikeItems.plugin().reloadConfig();
        } catch (ConstructorException e) {
            RogueLikeItems.logger().severe("Unable to load config!\n" + e.getMessage());
            return null;
        }

        amtWarnings = 0;
        amtErrors = 0;
        validateModifiers();
        validateSettings();
        return Pair.of(amtWarnings, amtErrors);
    }

    private static void validateModifiers() {
        ConfigData configData = ConfigData.getConfigData();
        Set<String> keys = RogueLikeItems.config().getKeys(false);

        for (ConfigSetting k : settingKeys) {
            keys.remove(k.toString());
        }

        HashMap<Attribute, CustomAttributeModifier> customAttributeModifierMap = new HashMap<>();

        if (keys.isEmpty()) {
            RogueLikeItems.logger().info("Found 0 amplifiers");
            configData.setCustomAttributeModifier(customAttributeModifierMap);
            return;
        }
        RogueLikeItems.logger().info("Found " + keys.size() + " amplifiers");
        int amtLoaded = 0;
        for (String k : keys) {
            String key = "minecraft:" + k.toLowerCase().replace("-", "_").replace(" ", "_");

            final Attribute attribute = Registry.ATTRIBUTE.get(NamespacedKey.fromString(key));
            if (!k.equalsIgnoreCase("durability") && attribute == null) {
                RogueLikeItems.logger().warning("Could not find Attribute " + k);
                amtWarnings++;
                continue;
            }

            ConfigurationSection configurationSection = RogueLikeItems.config().getConfigurationSection(k);
            Set<String> configurationSectionKeys = configurationSection.getKeys(false);

            if (configurationSectionKeys.contains(ConfigModifier.ACTIVE.toString())) {
                Object yActive = configurationSection.get(ConfigModifier.ACTIVE.toString());
                if (!validateTag(k, yActive, ConfigModifier.ACTIVE)) continue;
            }

            if (customAttributeModifierMap.get(attribute) != null) {
                RogueLikeItems.logger().warning("Modifier for attribute " + k + " already exists!");
                amtWarnings++;
                continue;
            }

            boolean inPercent = true;
            if (configurationSectionKeys.contains(ConfigModifier.IN_PERCENT.toString())) {
                Object yInPercent = configurationSection.get(ConfigModifier.IN_PERCENT.toString());
                inPercent = validateTag(k, yInPercent, ConfigModifier.IN_PERCENT);
            }

            int chance = 100;
            if (configurationSectionKeys.contains(ConfigModifier.CHANCE.toString())) {
                try {
                    Object yChance = configurationSection.get(ConfigModifier.CHANCE.toString());
                    if (yChance != null) {
                        chance = (int) yChance;

                        if (chance > 100) {
                            chance = 100;
                            RogueLikeItems.logger().warning(ConfigModifier.CHANCE + " for " + k + " is greater than 100");
                            amtWarnings++;
                        }
                        if (chance <= 0) {
                            chance = 0;
                            RogueLikeItems.logger().warning(ConfigModifier.CHANCE + " for " + k + " is less or equal to 0.");
                            amtWarnings++;
                        }
                    }
                } catch (ClassCastException e) {
                    RogueLikeItems.logger().warning(ConfigModifier.CHANCE + " for " + k + " wrongfully declared");
                    amtWarnings++;
                }
            }

            List<Material> ignoreItems = new ArrayList<>(List.of());
            if (configurationSectionKeys.contains(ConfigModifier.IGNORE_ITEMS.toString())) {
                ignoreItems = validateItemList(k, ConfigModifier.IGNORE_ITEMS, configurationSection.get(ConfigModifier.IGNORE_ITEMS.toString()));
            }

            boolean toolsAndWeapons = true;
            if (configurationSectionKeys.contains(ConfigModifier.TOOLS_AND_WEAPONS.toString())) {
                Object yToolsAndWeapons = configurationSection.get(ConfigModifier.TOOLS_AND_WEAPONS.toString());
                toolsAndWeapons = validateTag(k, yToolsAndWeapons, ConfigModifier.TOOLS_AND_WEAPONS);
            }

            boolean armorAndShield = true;
            if (configurationSectionKeys.contains(ConfigModifier.ARMOR_AND_SHIELD.toString())) {
                Object yArmorAndShield = configurationSection.get(ConfigModifier.ARMOR_AND_SHIELD.toString());
                armorAndShield = validateTag(k, yArmorAndShield, ConfigModifier.ARMOR_AND_SHIELD);
            }

            if (!configurationSectionKeys.contains(ConfigModifier.RANGE.toString())) {
                RogueLikeItems.logger().severe(k + " does not have a required range!");
                amtErrors++;
                continue;
            }

            List<Float> range = new ArrayList<>(List.of());
            Object yRange = configurationSection.get(ConfigModifier.RANGE.toString());
            if (yRange == null) {
                RogueLikeItems.logger().severe(k + " does not have required range!");
                amtErrors++;
                continue;
            }

            try {
                ConfigurationSection cRange = (ConfigurationSection) yRange;
                Object from = cRange.get(ConfigModifier.FROM.toString());
                Object to = cRange.get(ConfigModifier.TO.toString());

                if (from != null)
                    range.add(Float.parseFloat(from.toString()));
                else {
                    RogueLikeItems.logger().warning("Missing from-value for range " + k);
                    amtWarnings++;
                }
                if (to != null)
                    range.add(Float.parseFloat(to.toString()));
                else {
                    RogueLikeItems.logger().warning("Missing to-value for range " + k);
                    amtWarnings++;
                }
            } catch (ClassCastException e) {
                // old format or single value is used
            } catch (NumberFormatException e) {
                RogueLikeItems.logger().severe("Range for " + k + " wrongfully declared!");
                amtErrors++;
                continue;
            }

            if (range.isEmpty()) {
                try {
                    if (yRange instanceof List<?>) {
                        RogueLikeItems.logger().warning("Range for " + k + " is using old format. Consider using new format in future.");
                        List<?> tempRange = (List<?>) yRange;
                        if (tempRange.isEmpty()) {
                            RogueLikeItems.logger().severe("Range for " + k + " is required, but empty!");
                            amtErrors++;
                            continue;
                        }
                        range.add(Float.parseFloat(tempRange.getFirst().toString()));
                        range.add(Float.parseFloat(tempRange.getLast().toString()));
                    } else {
                        range.add(Float.parseFloat(yRange.toString()));
                    }
                } catch (IllegalArgumentException e) {
                    RogueLikeItems.logger().severe("Range for " + k + " wrongfully declared!");
                    amtErrors++;
                    return;
                }
            }

            if (range.getFirst() > range.getLast()) {
                range = range.reversed();
                RogueLikeItems.logger().warning("Range for " + k + " is reversed.");
            }

            boolean useOnlyNaturalNumbers;
            boolean isInt = range.getFirst() % 1 == 0 && range.getLast() % 1 == 0;
            if (configurationSectionKeys.contains(ConfigModifier.USE_ONLY_NATURAL_NUMBERS.toString())) {
                Object yUseOnlyNaturalNumbers = configurationSection.get(ConfigModifier.USE_ONLY_NATURAL_NUMBERS.toString());
                useOnlyNaturalNumbers = validateTag(k, yUseOnlyNaturalNumbers, ConfigModifier.USE_ONLY_NATURAL_NUMBERS);
                if (useOnlyNaturalNumbers && !isInt) {
                    useOnlyNaturalNumbers = false;
                    RogueLikeItems.logger().warning(ConfigModifier.USE_ONLY_NATURAL_NUMBERS + " for " + k + " will be ignored");
                    amtWarnings++;
                }
            } else {
                useOnlyNaturalNumbers = isInt;
            }

            customAttributeModifierMap.put(attribute, new CustomAttributeModifier(
                    inPercent,
                    range,
                    chance,
                    ignoreItems,
                    toolsAndWeapons,
                    armorAndShield,
                    useOnlyNaturalNumbers
            ));

            amtLoaded++;
        }
        RogueLikeItems.logger().info("Loaded " + amtLoaded + " amplifiers");
        configData.setCustomAttributeModifier(customAttributeModifierMap);
    }

    private static List<Material> validateItemList(String k, ConfigType config, Object yItemList) {
        List<Material> itemList = new ArrayList<>(List.of());
        String errorMessage = config + (k != null ? (" in " + k) : " ") + "wrongfully declared!";
        try {
            if (yItemList != null) {
                List<?> list = (List<?>) yItemList;
                for (Object m : list) {
                    try {
                        Material material = Material.valueOf(m.toString()
                                .toUpperCase()
                                .replaceAll("-", "_")
                                .replaceAll(" ", "_"));

                        itemList.add(material);
                    } catch (IllegalArgumentException e) {
                        RogueLikeItems.logger().warning(errorMessage);
                        amtWarnings++;
                    }
                }
            }
        } catch (ClassCastException e) {
            RogueLikeItems.logger().warning(errorMessage);
            amtWarnings++;
        }
        return itemList;
    }

    private static boolean validateTag(String key, Object tag, ConfigType config) {
        try {
            return (Boolean) tag;
        } catch (ClassCastException e) {
            RogueLikeItems.logger().warning(config + (key != null ? " for " + key : "") + " wrongfully declared");
            amtWarnings++;
            return true;
        }
    }

    private static void validateSettings() {
        for (ConfigSetting k : settingKeys) {
            if (k.equals(ConfigSetting.IGNORE_ITEMS)) {
                configData.setIgnoreItemsList(validateItemList(null, ConfigSetting.IGNORE_ITEMS, RogueLikeItems.config().get(k.toString())));
                continue;
            }
            if (k.equals(ConfigSetting.INCLUDE_ITEMS)) {
                configData.setIncludeItemsList(validateItemList(null, ConfigSetting.INCLUDE_ITEMS, RogueLikeItems.config().get(k.toString())));
                continue;
            }
            validateTag(null, RogueLikeItems.config().get(k.toString()), k);
        }
    }
}
