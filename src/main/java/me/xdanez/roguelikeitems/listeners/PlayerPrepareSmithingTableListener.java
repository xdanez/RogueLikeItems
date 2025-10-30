package me.xdanez.roguelikeitems.listeners;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
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

        ItemStack newResult = ItemStack.of(result.getType());

        double durabilityAmplifier = DurabilityAmplifierUtil.getAmplifier(input);

        if (durabilityAmplifier != 0)
            DurabilityAmplifierUtil.setDurabilityData(newResult, Math.round(durabilityAmplifier * 100.0) / 100.0);

        ItemAttributeModifiers.Builder attributes = AttributeModifiersAmplifierUtil.getAttributeModifiersNetherite(input, result);

        newResult.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes.build());
        e.setResult(newResult);
    }

}
