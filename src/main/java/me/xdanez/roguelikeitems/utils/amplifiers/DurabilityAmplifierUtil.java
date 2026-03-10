package me.xdanez.roguelikeitems.utils.amplifiers;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import me.xdanez.roguelikeitems.utils.LoreUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

final public class DurabilityAmplifierUtil {

    public static boolean setDurabilityData(ItemStack item, double amplifier, boolean inPercent) {
        Material material = item.getType();
        int maxDurabilityOfItem = material.getMaxDurability();
        if (!ItemType.isBreakable(material) || maxDurabilityOfItem == 0) return false;

        int amplifiedMaxDurability = (int) Math.round(maxDurabilityOfItem + (inPercent ? maxDurabilityOfItem * amplifier : amplifier));
        if (amplifiedMaxDurability <= 0) amplifiedMaxDurability = 1;

        item.setData(DataComponentTypes.MAX_DAMAGE, amplifiedMaxDurability);

        LoreUtil.setDurabilityLore(item, amplifier);
        return true;
    }

    public static double getAmplifier(ItemStack item) {
        try {
            boolean inPercent = item.getPersistentDataContainer().get(AmplifierUtil.DISPLAY_DURABILITY_KEY, PersistentDataType.BOOLEAN);
            int ogMaxDamage = ItemStack.of(item.getType()).getData(DataComponentTypes.MAX_DAMAGE);
            int amplifiedMaxDamage = item.getData(DataComponentTypes.MAX_DAMAGE);
            return inPercent ? (double) amplifiedMaxDamage / (double) ogMaxDamage - 1 : amplifiedMaxDamage - ogMaxDamage;
        } catch (NullPointerException e) {
            // Item has no durability
            return 0;
        }
    }
}
