package me.xdanez.roguelikeitems.utils;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.models.CustomAttributeModifier;
import me.xdanez.roguelikeitems.utils.amplifiers.AttributeModifiersAmplifierUtil;
import me.xdanez.roguelikeitems.utils.amplifiers.DurabilityAmplifierUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

final public class AmplifierUtil {

    public static final NamespacedKey DISPLAY_DURABILITY_KEY = new NamespacedKey(RogueLikeItems.plugin(), "display_durability");

    public static void setAmplifiers(ItemStack item) {
        if (item == null) return;
        Material material = item.getType();
        if (material.equals(Material.AIR)) return;
        if (!ItemType.isModifiable(material)) return;

        if (ConfigData.getConfigData().getIgnoreItemList().contains(material)) return;

        AttributeModifiersAmplifierUtil.setAmplifiers(item);
    }

    public static double getRandomAmplifierValue(CustomAttributeModifier cam) {
        boolean naturalNumbers = cam.useOnlyNaturalNumbers();
        float from = cam.from();
        float to = cam.to();
        int rangeM = naturalNumbers ? 1 : 1000;
        int divider = naturalNumbers ? 100 : 100000;
        double absolute = cam.inPercent() ? 1 : 100;
        if (!naturalNumbers)
            return (double) ThreadLocalRandom.current()
                    .nextFloat(from * rangeM, to * rangeM + 1) / divider * absolute;
        return (double) ThreadLocalRandom.current()
                .nextInt((int) from * rangeM, (int) to * rangeM + 1) / divider * absolute;
    }

    public static boolean hasAnyAmplifier(ItemStack item) {
        if (DurabilityAmplifierUtil.getAmplifier(item) != 0) return true;

        ItemAttributeModifiers attributes = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        for (ItemAttributeModifiers.Entry e : attributes.modifiers()) {
            if (isCustomKey(e.modifier().getKey())) return true;
        }
        return false;
    }

    private static boolean isCustomKey(NamespacedKey key) {
        return key.key().toString().contains("roguelikeitems");
    }
}
