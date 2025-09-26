package me.xdanez.roguelikeitems.utils;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import it.unimi.dsi.fastutil.Pair;
import me.xdanez.roguelikeitems.enums.Config;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.amplifiers.AttributeModifiersAmplifierUtil;
import me.xdanez.roguelikeitems.utils.amplifiers.DurabilityAmplifierUtil;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

final public class AmplifierUtil {

    public static void setAmplifiers(ItemStack item) {
        if (item == null) return;
        Material material = item.getType();
        if (material.equals(Material.AIR)) return;
        if (!ItemType.isModifiable(material)) return;

        if (ConfigData.getConfigData().getIgnoreItemList().contains(item)) return;

        if (ConfigUtil.useAmplifier(Config.USE_DURABILITY_AMPLIFIER))
            DurabilityAmplifierUtil.setDurabilityData(item);

        AttributeModifiersAmplifierUtil.setAmplifiers(item);
    }


    public static double getRandomAmplifierValue(Config config) {
        ConfigData configData = ConfigData.getConfigData();
        boolean naturalNumbers = configData.useNaturalNumbers();
        int rangeM = naturalNumbers || config.equals(Config.MAX_HEALTH_AMPLIFIER_RANGE) ? 1 : 100;
        int divider = config.equals(Config.MAX_HEALTH_AMPLIFIER_RANGE) ? 1 : naturalNumbers ? 100 : 10000;
        Pair<Integer, Integer> range = configData.getRange(config);
        return (double) ThreadLocalRandom.current()
                .nextInt(range.first() * rangeM, range.second() * rangeM + 1) / divider;
    }

    public static boolean hasAnyAmplifier(ItemStack item) {
        if (DurabilityAmplifierUtil.getAmplifier(item) != 0) return true;
        boolean isRanged = ItemType.isRanged(item.getType());

        if (!isRanged) {
            ItemAttributeModifiers defaultAttributes = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
            for (ItemAttributeModifiers.Entry e : defaultAttributes.modifiers()) {
                if (e.attribute().equals(Attribute.MAX_HEALTH)) return true;
                if (e.attribute().equals(Attribute.ATTACK_DAMAGE)) {
                    ItemAttributeModifiers.Entry ogEntry =
                            ItemStack.of(item.getType()).getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                                    .modifiers().stream()
                                    .filter(i -> i.attribute()
                                            .equals(Attribute.ATTACK_DAMAGE)).findFirst().orElse(null);
                    if (ogEntry == null) return true;

                    double baseAmount = ogEntry.modifier().getAmount();
                    double modifiedAmount = e.modifier().getAmount();

                    if (baseAmount != modifiedAmount) return true;
                }
            }
        } else
            return item.getItemMeta().getPersistentDataContainer().has(AttributeModifiersAmplifierUtil.DAMAGE_AMPLIFIER);

        return false;
    }
}
