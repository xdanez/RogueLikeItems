package me.xdanez.roguelikeitems.listeners;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import me.xdanez.roguelikeitems.utils.amplifiers.MaxHealthAmplifierUtil;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerChangeEquipmentListener implements Listener {

    @EventHandler
    public void onPlayerChangeHeldItem(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        setMaxHealth(player, e.getNewSlot());
    }

    @EventHandler
    public void onPlayerInteractInventory(PlayerInventorySlotChangeEvent e) {
        Player player = e.getPlayer();
        setMaxHealth(player, -1);
    }

    private void setMaxHealth(Player player, int newSlot) {
        PlayerInventory inventory = player.getInventory();
        ItemStack mainHand;
        if (newSlot != -1) {
            mainHand = inventory.getContents()[newSlot];
        } else {
            mainHand = inventory.getItemInMainHand();
        }

        ItemStack offHand = inventory.getItemInOffHand();
        ItemStack[] armor = inventory.getArmorContents();

        AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealth == null) return;
        maxHealth.setBaseValue(maxHealth.getDefaultValue());
        for (ItemStack piece : armor) {
            if (piece == null) continue;
            if (!MaxHealthAmplifierUtil.hasMaxHealthAmplifier(piece)) continue;
            maxHealth.setBaseValue(maxHealth.getBaseValue() + MaxHealthAmplifierUtil.getMaxHealthAmplifier(piece));
        }
        if (mainHand != null && !mainHand.getType().equals(Material.AIR) && MaxHealthAmplifierUtil.hasMaxHealthAmplifier(mainHand)) {
            maxHealth.setBaseValue(maxHealth.getBaseValue() + MaxHealthAmplifierUtil.getMaxHealthAmplifier(mainHand));
        }
        if (!offHand.getType().equals(Material.AIR) && MaxHealthAmplifierUtil.hasMaxHealthAmplifier(offHand)) {
            maxHealth.setBaseValue(maxHealth.getBaseValue() + MaxHealthAmplifierUtil.getMaxHealthAmplifier(offHand));
        }
    }
}
