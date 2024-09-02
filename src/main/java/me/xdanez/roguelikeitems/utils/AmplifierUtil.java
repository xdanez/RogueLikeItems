package me.xdanez.roguelikeitems.utils;

import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.ConfigData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

final public class AmplifierUtil {

    public static void setAmplifiers(ItemStack item) {
        if (item == null) return;
        Material material = item.getType();
        if (material.equals(Material.AIR)) return;
        if (!ItemType.isModifiable(material)) return;
        ConfigData configData = ConfigData.getConfigData();

        if (configData.getIgnoreItemList().contains(item)) return;

        if (configData.useDurabilityAmplifier()) {
            DurabilityAmplifierUtil.setDurabilityData(item);
        }
        if (configData.useDamageAmplifier() && !ItemType.hasNoDamage(material)) {
            DamageAmplifierUtil.setDamageAmplifierData(item);
        }
    }
}
