package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.amplifiers.DamageAmplifierUtil;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DamageDealtListener implements Listener {

    @EventHandler
    public void onDamageDealt(EntityDamageByEntityEvent e) {
        Entity damagerEntity = e.getDamager();
        if (damagerEntity instanceof Arrow) {
            Arrow arrow = (Arrow) damagerEntity;
            if (!(arrow.getShooter() instanceof Player)) return;
            Player player = (Player) arrow.getShooter();
            damageEntity(e, arrow, player);
            return;
        }

        if (!(damagerEntity instanceof Player)) return;

        Player player = (Player) damagerEntity;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.AIR) return;

        damageEntity(e, itemInHand, player);
    }

    private void damageEntity(EntityDamageByEntityEvent e, Object damageType, Player player) {
        double damageAmplifier = 0;
        if (damageType instanceof ItemStack) {
            if (DamageAmplifierUtil.hasDamageAmplifier((ItemStack) damageType))
                damageAmplifier += DamageAmplifierUtil.getDamageAmplifier((ItemStack) damageType);
        } else if (damageType instanceof Arrow) {
            if (DamageAmplifierUtil.hasArrowDamageAmplifier((Arrow) damageType))
                damageAmplifier += DamageAmplifierUtil.getArrowDamageAmplifier((Arrow) damageType);
        }
        double currentDamage = e.getDamage();
        double armorDamageAmplifier = getArmorDamageAmplifier(player);
        double amplifiedDamage = currentDamage + currentDamage * (damageAmplifier + armorDamageAmplifier);
        e.setDamage(amplifiedDamage);
    }

    private double getArmorDamageAmplifier(Player player) {
        double armorDamageAmplifier = 0;
        if (!ConfigData.getConfigData().useArmorDamageAmplifier()) return armorDamageAmplifier;

        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack armorPiece : armor) {
            if (armorPiece == null) continue;
            if (!DamageAmplifierUtil.hasDamageAmplifier(armorPiece)) continue;
            double amplifier = DamageAmplifierUtil.getDamageAmplifier(armorPiece);
            armorDamageAmplifier += amplifier;
        }

        return armorDamageAmplifier;
    }
}
