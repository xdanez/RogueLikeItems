package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.enums.AttackDamage;
import me.xdanez.roguelikeitems.models.Durability;
import me.xdanez.roguelikeitems.models.DurabilityDataType;
import me.xdanez.roguelikeitems.utils.DamageAmplifierUtil;
import me.xdanez.roguelikeitems.utils.DurabilityAmplifierUtil;
import me.xdanez.roguelikeitems.utils.LoreUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class PlayerPrepareSmithingTableListener implements Listener {

    @EventHandler
    public void onPlayerPrepareSmithingTable(PrepareSmithingEvent e) {
        ItemStack smithedItem = e.getResult();
        if (smithedItem == null) return;

        ItemStack inputEquipment = e.getInventory().getInputEquipment();
        if (inputEquipment == null) return;

        ItemMeta smithedItemMeta = smithedItem.getItemMeta();

        Material siMaterial = smithedItem.getType();
        Damageable siDmg = (Damageable) smithedItemMeta;

        if (DurabilityAmplifierUtil.hasDurabilityData(inputEquipment)) {
            double amplifier = DurabilityAmplifierUtil.getAmplifier(inputEquipment);

            int siMaxDurability = siMaterial.getMaxDurability();
            int siCurrentDurability = siMaxDurability - siDmg.getDamage();

            double siDurabilityLeftPercentage = (double) siCurrentDurability / siMaxDurability;
            int siAmplifiedDurability = (int) (siMaxDurability + siMaxDurability * amplifier / 100);
            int siNewDurabilityLeft = (int) (siAmplifiedDurability * siDurabilityLeftPercentage);

            PersistentDataContainer container = smithedItemMeta.getPersistentDataContainer();
            container.set(DurabilityAmplifierUtil.getKey(),new DurabilityDataType(),
                    new Durability(siAmplifiedDurability, amplifier, siNewDurabilityLeft)
            );
            LoreUtil.clearLore(smithedItemMeta);
            LoreUtil.setDurabilityLore(smithedItem);
        }

        if (DamageAmplifierUtil.hasDamageAmplifier(inputEquipment)) {
            double attackDamage = AttackDamage.getDamageFromMaterial(siMaterial);
            LoreUtil.setDamageAmplifierLore(smithedItem, attackDamage, siMaterial);
        }
        smithedItem.setItemMeta(smithedItemMeta);
    }

}
