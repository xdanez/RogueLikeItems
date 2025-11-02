package me.xdanez.roguelikeitems.listeners;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
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
        boolean hasAnyAmplifier = AmplifierUtil.hasAnyAmplifier(input);

        if (durabilityAmplifier == 0 && !hasAnyAmplifier) return;

        if (durabilityAmplifier != 0) {
            DurabilityAmplifierUtil.setDurabilityData(result, Math.round(durabilityAmplifier * 100.0) / 100.0);
        }

        if (hasAnyAmplifier) {
            ItemAttributeModifiers.Builder attributes = AttributeModifiersAmplifierUtil.getAttributeModifiersNetherite(input, result);
            result.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes.build());
        }
    }

}
