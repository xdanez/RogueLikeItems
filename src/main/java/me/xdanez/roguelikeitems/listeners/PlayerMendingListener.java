package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.utils.DurabilityAmplifierUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMendingListener implements Listener {

    @EventHandler
    public void onPlayerMending(PlayerItemMendEvent e) {
        ItemStack item = e.getItem();
        int repairAmt = e.getRepairAmount();
        DurabilityAmplifierUtil.repair(item, repairAmt);
    }
}
