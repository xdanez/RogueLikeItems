package me.xdanez.roguelikeitems.utils;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.models.CustomAttributeModifier;
import me.xdanez.roguelikeitems.utils.amplifiers.AttributeModifiersAmplifierUtil;
import me.xdanez.roguelikeitems.utils.amplifiers.DurabilityAmplifierUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

final public class AmplifierUtil {

    public static void setAmplifiers(ItemStack item) {
        if (item == null) return;
        Material material = item.getType();
        if (material.equals(Material.AIR)) return;
        if (!ItemType.isModifiable(material)) return;

        if (ConfigData.getConfigData().getIgnoreItemList().contains(material)) return;

        DurabilityAmplifierUtil.setDurabilityData(item);

        AttributeModifiersAmplifierUtil.setAmplifiers(item);
    }

    public static double getRandomAmplifierValue(CustomAttributeModifier cam) {
        boolean naturalNumbers = cam.useOnlyNaturalNumbers();
        int rangeM = naturalNumbers ? 1 : 100;
        int divider = naturalNumbers ? 100 : 10000;
        double absolute = cam.inPercent() ? 1 : 100;
        if (!naturalNumbers)
            return (double) ThreadLocalRandom.current()
                    .nextFloat(cam.from() * rangeM, cam.to() * rangeM + 1) / divider * absolute;
        return (double) ThreadLocalRandom.current()
                .nextInt((int) cam.from() * rangeM, (int) cam.to() * rangeM + 1) / divider * absolute;
    }

    public static boolean hasAnyAmplifier(ItemStack item) {
        if (DurabilityAmplifierUtil.getAmplifier(item) != 0) return true;

        ItemAttributeModifiers attributes = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        for (ItemAttributeModifiers.Entry e : attributes.modifiers()) {
            ItemAttributeModifiers.Entry defaultAttribute = ItemStack.of(item.getType())
                    .getData(DataComponentTypes.ATTRIBUTE_MODIFIERS).modifiers()
                    .stream().filter(i -> i.attribute().equals(e.attribute())).findFirst().orElse(null);

            if (defaultAttribute == null) return true;

            double defaultAmount = defaultAttribute.modifier().getAmount();
            double modifiedAmount = e.modifier().getAmount();

            if (defaultAmount != modifiedAmount) return true;
        }
        return false;
    }
}
