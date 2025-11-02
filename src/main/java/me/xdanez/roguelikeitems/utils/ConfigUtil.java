package me.xdanez.roguelikeitems.utils;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Pair;
import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.Config;
import me.xdanez.roguelikeitems.enums.ConfigSetting;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.models.CustomAttributeModifier;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final public class ConfigUtil {
    static int amtWarnings = 0;
    static int amtErrors = 0;

    static List<String> settingKeys = Stream.of(ConfigSetting.values())
            .map(ConfigSetting::toString)
            .collect(Collectors.toList());

    public static boolean useAmplifier(CustomAttributeModifier cam, Material material) {
        if (ConfigData.getConfigData().getIgnoreItemList().contains(material)) return false;
        if (cam.ignoreItemsList().contains(material)) return false;
        if (ItemType.isWeaponOrTool(material) && !cam.useToolsAndWeapons()) return false;
        if (ItemType.isArmorOrShield(material) && !cam.useArmorAndShield()) return false;

        return ThreadLocalRandom.current().nextInt(100 + 1) <= cam.chance();
    }

    public static Pair<Integer, Integer> validateConfig() {
        amtWarnings = 0;
        amtErrors = 0;
        validateModifiers();
        validateSettings();
        return Pair.of(amtWarnings, amtErrors);
    }

    private static void validateModifiers() {
        ConfigData configData = ConfigData.getConfigData();
        Set<String> keys = RogueLikeItems.config().getKeys(false);
        List<Key> attributeList = Stream.of(Lists.newArrayList(Registry.ATTRIBUTE).toArray(new Attribute[0]))
                .map(Attribute::key)
                .collect(Collectors.toList());

        keys.removeIf(settingKeys::contains);

        List<CustomAttributeModifier> customAttributeModifierList = new ArrayList<>(List.of());

        if (keys.isEmpty()) {
            RogueLikeItems.logger().warning("No amplifier found. Items will not be affected in any way");
            configData.setCustomAttributeModifier(customAttributeModifierList);
            return;
        }

        for (String k : keys) {
            String key = "minecraft:" + k.replace("-", "_").replace(" ", "_");
            if (!attributeList.contains(Key.key(key)) && !k.equalsIgnoreCase("durability")) {
                RogueLikeItems.logger().warning("Could not find Attribute " + k);
                amtWarnings++;
                continue;
            }

            Attribute attribute;
            if (k.equalsIgnoreCase("durability")) attribute = null;
            else attribute = Registry.ATTRIBUTE.get(NamespacedKey.fromString(key));

            ConfigurationSection configurationSection = RogueLikeItems.config().getConfigurationSection(k);
            Set<String> configurationSectionKeys = configurationSection.getKeys(false);

            if (configurationSectionKeys.contains(Config.ACTIVE.toString())) {
                Object yActive = configurationSection.get(Config.ACTIVE.toString());
                if (!validateTag(k, yActive, Config.ACTIVE)) continue;
            }

            if (customAttributeModifierList.stream().anyMatch(c -> c.attribute() == attribute)) {
                RogueLikeItems.logger().warning("Modifier for attribute " + k + " already exists!");
                amtWarnings++;
                continue;
            }

            boolean inPercent = true;
            if (configurationSectionKeys.contains(Config.IN_PERCENT.toString())) {
                Object yInPercent = configurationSection.get(Config.IN_PERCENT.toString());
                inPercent = validateTag(k, yInPercent, Config.IN_PERCENT);
            }

            int chance = 100;
            if (configurationSectionKeys.contains(Config.CHANCE.toString())) {
                try {
                    Object yChance = configurationSection.get(Config.CHANCE.toString());
                    if (yChance != null) {
                        chance = (int) yChance;

                        if (chance > 100) {
                            chance = 100;
                            RogueLikeItems.logger().warning(Config.CHANCE + " for " + k + " is greater than 100");
                            amtWarnings++;
                        }
                        if (chance <= 0) {
                            chance = 0;
                            RogueLikeItems.logger().warning(Config.CHANCE + " for " + k + " is less or equal to 0.");
                            amtWarnings++;
                        }
                    }
                } catch (ClassCastException e) {
                    RogueLikeItems.logger().warning(Config.CHANCE + " for " + k + " wrongfully declared");
                    amtWarnings++;
                }
            }

            List<Material> ignoreItems = new ArrayList<>(List.of());
            if (configurationSectionKeys.contains(Config.IGNORE_ITEMS.toString())) {
                ignoreItems = validateIgnoreList(k, configurationSection.get(Config.IGNORE_ITEMS.toString()));
            }

            boolean toolsAndWeapons = true;
            if (configurationSectionKeys.contains(Config.TOOLS_AND_WEAPONS.toString())) {
                Object yToolsAndWeapons = configurationSection.get(Config.TOOLS_AND_WEAPONS.toString());
                toolsAndWeapons = validateTag(k, yToolsAndWeapons, Config.TOOLS_AND_WEAPONS);
            }

            boolean armorAndShield = true;
            if (configurationSectionKeys.contains(Config.ARMOR_AND_SHIELD.toString())) {
                Object yArmorAndShield = configurationSection.get(Config.ARMOR_AND_SHIELD.toString());
                armorAndShield = validateTag(k, yArmorAndShield, Config.ARMOR_AND_SHIELD);
            }

            if (!configurationSectionKeys.contains(Config.RANGE.toString())) {
                RogueLikeItems.logger().severe(k + " does not have a required range!");
                amtErrors++;
                continue;
            }

            List<Float> range = new ArrayList<>(List.of());
            try {
                Object yRange = configurationSection.get(Config.RANGE.toString());
                if (yRange == null) {
                    RogueLikeItems.logger().severe("Range for " + k + " does not have required range!");
                    amtErrors++;
                    continue;
                }
                if (yRange instanceof List<?>) {
                    List<?> tempRange = (List<?>) yRange;
                    if (tempRange.isEmpty()) {
                        RogueLikeItems.logger().severe("Range for " + k + " is required, but empty!");
                        amtErrors++;
                        continue;
                    }
                    range.add(Float.parseFloat(tempRange.getFirst().toString()));
                    range.add(Float.parseFloat(tempRange.getLast().toString()));

                    if (range.getFirst() > range.getLast()) {
                        range = range.reversed();
                        RogueLikeItems.logger().warning("Range for " + k + " is reversed.");
                        amtWarnings++;
                    }

                } else {
                    range.add(Float.parseFloat(yRange.toString()));
                }
            } catch (IllegalArgumentException e) {
                RogueLikeItems.logger().severe("Range for " + k + " wrongfully declared!");
                amtErrors++;
                return;
            }

            boolean useOnlyNaturalNumbers;
            boolean isInt = range.getFirst() % 1 == 0 && range.getLast() % 1 == 0;
            if (configurationSectionKeys.contains(Config.USE_ONLY_NATURAL_NUMBERS.toString())) {
                Object yUseOnlyNaturalNumbers = configurationSection.get(Config.USE_ONLY_NATURAL_NUMBERS.toString());
                useOnlyNaturalNumbers = validateTag(k, yUseOnlyNaturalNumbers, Config.USE_ONLY_NATURAL_NUMBERS);
                if (useOnlyNaturalNumbers && !isInt) {
                    useOnlyNaturalNumbers = false;
                    RogueLikeItems.logger().warning(Config.USE_ONLY_NATURAL_NUMBERS + " for " + k + " will be ignored");
                    amtWarnings++;
                }
            } else {
                useOnlyNaturalNumbers = isInt;
            }

            customAttributeModifierList.add(new CustomAttributeModifier(
                    attribute,
                    inPercent,
                    range,
                    chance,
                    ignoreItems,
                    toolsAndWeapons,
                    armorAndShield,
                    useOnlyNaturalNumbers)
            );
        }
        configData.setCustomAttributeModifier(customAttributeModifierList);
    }

    private static List<Material> validateIgnoreList(String k, Object yIgnoreLIst) {
        List<Material> ignoreItems = new ArrayList<>(List.of());
        try {
            Object yIgnoreItems = yIgnoreLIst == null ? RogueLikeItems.getConfigVal(Config.IGNORE_ITEMS) : yIgnoreLIst;
            if (yIgnoreItems != null) {
                List<?> list = (List<?>) yIgnoreItems;
                for (Object m : list) {
                    try {
                        Material material = Material.valueOf(m.toString()
                                .toUpperCase()
                                .replace("-", "_")
                                .replace(" ", "_"));

                        if (!ItemType.isModifiable(material)) {
                            RogueLikeItems.logger().warning(m
                                    + " in " + Config.IGNORE_ITEMS
                                    + (k != null ? (" in " + k) : "") + " is not modifiable!"
                            );
                            amtWarnings++;
                            continue;
                        }

                        ignoreItems.add(material);
                    } catch (IllegalArgumentException e) {
                        RogueLikeItems.logger().warning(m
                                + " in " + Config.IGNORE_ITEMS
                                + (k != null ? (" in " + k) : "") + " could not be found!"
                        );
                        amtWarnings++;
                    }
                }
            }
        } catch (ClassCastException e) {
            RogueLikeItems.logger().warning(Config.IGNORE_ITEMS + " "
                    + (k != null ? (" in " + k) : "") + "wrongfully declared!");
            amtWarnings++;
        }
        return ignoreItems;
    }

    private static boolean validateTag(String key, Object tag, Config config) {
        try {
            return (Boolean) tag;
        } catch (ClassCastException e) {
            RogueLikeItems.logger().warning(config + " for " + key + " wrongfully declared");
            amtWarnings++;
            return true;
        }
    }

    private static void validateTag(Object tag, String config) {
        try {
            boolean _t = (Boolean) tag;
        } catch (ClassCastException e) {
            RogueLikeItems.logger().warning(config + " wrongfully declared!");
            amtWarnings++;
        }
    }

    private static void validateSettings() {
        for (String k : settingKeys) {
            if (!k.equalsIgnoreCase(Config.IGNORE_ITEMS.toString())) {
                validateTag(RogueLikeItems.config().get(k), k);
                continue;
            }
            setIgnoreList(validateIgnoreList(null, null));
        }
    }

    private static void setIgnoreList(List<Material> items) {
        ConfigData configData = ConfigData.getConfigData();
        configData.setIgnoreItemList(items);
    }

}
