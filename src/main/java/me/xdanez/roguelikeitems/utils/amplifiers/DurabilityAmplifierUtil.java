package me.xdanez.roguelikeitems.utils.amplifiers;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.models.CustomAttributeModifier;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import me.xdanez.roguelikeitems.utils.ConfigUtil;
import me.xdanez.roguelikeitems.utils.LoreUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

final public class DurabilityAmplifierUtil {

    public static void setDurabilityData(ItemStack item) {
        CustomAttributeModifier durability = ConfigData.getConfigData().getDurabilityModifier();
        if (durability == null) return;

        Material material = item.getType();
        if (!ConfigUtil.useAmplifier(durability, material)) return;

        double amplifier = AmplifierUtil.getRandomAmplifierValue(durability);

        int maxDurabilityOfItem = material.getMaxDurability();

        if (maxDurabilityOfItem == 0) return;

        int amplifiedMaxDurability = (int) Math.round(maxDurabilityOfItem + (maxDurabilityOfItem * amplifier));
        if (amplifiedMaxDurability <= 0) amplifiedMaxDurability = 1;

        item.setData(DataComponentTypes.MAX_DAMAGE, amplifiedMaxDurability);
        LoreUtil.setDurabilityLore(item, amplifier);
    }

    public static void setDurabilityData(ItemStack item, double amplifier) {
        int maxDurabilityOfItem = item.getType().getMaxDurability();

        int amplifiedMaxDurability = (int) Math.round(maxDurabilityOfItem + (maxDurabilityOfItem * amplifier));

        item.setData(DataComponentTypes.MAX_DAMAGE, amplifiedMaxDurability);

        LoreUtil.setDurabilityLore(item, amplifier);
    }

    public static double getAmplifier(ItemStack item) {
        int ogMaxDamage = ItemStack.of(item.getType()).getData(DataComponentTypes.MAX_DAMAGE);
        int amplifiedMaxDamage = item.getData(DataComponentTypes.MAX_DAMAGE);
        return (double) amplifiedMaxDamage / (double) ogMaxDamage - 1;
    }
}
