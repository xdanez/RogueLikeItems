package me.xdanez.roguelikeitems.utils;

import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.AmplifierChance;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.amplifiers.DamageAmplifierUtil;
import me.xdanez.roguelikeitems.utils.amplifiers.DurabilityAmplifierUtil;
import me.xdanez.roguelikeitems.utils.amplifiers.MaxHealthAmplifierUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

final public class AmplifierUtil {

    public static void setAmplifiers(ItemStack item) {
        if (item == null) return;
        Material material = item.getType();
        if (material.equals(Material.AIR)) return;
        if (!ItemType.isModifiable(material)) return;
        ConfigData configData = ConfigData.getConfigData();
        AmplifierChance amplifierChance = configData.getAmplifierChance();

        if (configData.getIgnoreItemList().contains(item)) return;

        if (configData.useMaxHealthAmplifier()
                && amplifierChance.getMaxHealth() >= ThreadLocalRandom.current().nextInt(100 + 1))
            MaxHealthAmplifierUtil.setMaxHealthData(item);

        if (configData.useDurabilityAmplifier()
                && amplifierChance.getDurability() >= ThreadLocalRandom.current().nextInt(100 + 1))
            DurabilityAmplifierUtil.setDurabilityData(item);

        if (configData.useDamageAmplifier() && !ItemType.hasNoDamage(material)
                && amplifierChance.getDamage() >= ThreadLocalRandom.current().nextInt(100 + 1)) DamageAmplifierUtil.setDamageAmplifierData(item);
    }
}
