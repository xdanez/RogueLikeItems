package me.xdanez.roguelikeitems.listeners;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.utils.amplifiers.AttributeModifiersAmplifierUtil;
import me.xdanez.roguelikeitems.utils.amplifiers.DurabilityAmplifierUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerPrepareSmithingTableListener implements Listener {

    @EventHandler
    public void onPlayerPrepareSmithingTable(PrepareSmithingEvent e) {
        ItemStack input = e.getInventory().getInputEquipment();
        if (input == null) return;

        ItemStack result = e.getResult();
        if (result == null) return;

        double durabilityAmplifier = DurabilityAmplifierUtil.getAmplifier(input);

        result.lore(null);

        if (durabilityAmplifier != 0)
            DurabilityAmplifierUtil.setDurabilityData(result, Math.round(durabilityAmplifier * 100.0) / 100.0);

        boolean isArmor = ItemType.isArmor(result.getType());
        if (!isArmor) {
            ItemAttributeModifiers.Builder attributes = AttributeModifiersAmplifierUtil.getAttributeModifiersNetherite(input, result);
            result.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes.build());
        }
    }

}
