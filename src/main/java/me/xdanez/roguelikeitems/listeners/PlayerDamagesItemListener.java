package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.utils.amplifiers.DurabilityAmplifierUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDamagesItemListener implements Listener {

    @EventHandler
    public void onPlayerDamagesItem(PlayerItemDamageEvent e) {
        ItemStack item = e.getItem();
        Material material = item.getType();
        if (!ItemType.isModifiable(material)) return;


        if (!DurabilityAmplifierUtil.hasDurabilityData(item)) return;
        e.setCancelled(true);

        // preventing elytras breaking
        if (material.equals(Material.ELYTRA) && DurabilityAmplifierUtil.getCurrentDurability(item) == 1) {
            e.setCancelled(false);
            return;
        }

        DurabilityAmplifierUtil.damage(item, e.getDamage());

        // break item
        if (DurabilityAmplifierUtil.getCurrentDurability(item) <= 0) {
            e.setCancelled(false);
        }
    }
}
