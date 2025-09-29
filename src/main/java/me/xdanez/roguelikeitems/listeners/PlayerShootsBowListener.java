package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.utils.amplifiers.AttributeModifiersAmplifierUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerShootsBowListener implements Listener {

    @EventHandler
    public void onPlayerShootsBow(EntityShootBowEvent e) {
        ItemStack bow = e.getBow();
        if (bow == null) return;

        Entity projectile = e.getProjectile();
        if (!(projectile instanceof Arrow)) return;

        Arrow arrow = (Arrow) projectile;
        PersistentDataContainer container = bow.getItemMeta().getPersistentDataContainer();
        try {
            double damageAmplifier = container.get(AttributeModifiersAmplifierUtil.DAMAGE_AMPLIFIER, PersistentDataType.DOUBLE);
            double baseDamage = arrow.getDamage();
            arrow.setDamage(baseDamage + baseDamage * damageAmplifier);
            e.setProjectile(arrow);
        } catch (NullPointerException ex) {
            // Bow has no amplifier, do nothing
        }
    }
}
