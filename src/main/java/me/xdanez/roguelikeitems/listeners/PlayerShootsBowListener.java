package me.xdanez.roguelikeitems.listeners;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerShootsBowListener implements Listener {

    @EventHandler
    public void onPlayerShootsBow(EntityShootBowEvent e) {
        ItemStack bow = e.getBow();
        if (bow == null) return;

        Entity projectile = e.getProjectile();
        if (!(projectile instanceof Arrow)) return;

        Arrow arrow = (Arrow) projectile;
        ItemAttributeModifiers.Entry dmg = bow.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS).modifiers().stream().filter(i -> i.attribute().equals(Attribute.ATTACK_DAMAGE)).findFirst().orElse(null);
        if (dmg == null) return;
        double damageAmplifier = dmg.modifier().getAmount();
        double baseDamage = arrow.getDamage();
        arrow.setDamage(baseDamage + (dmg.modifier().getOperation().equals(AttributeModifier.Operation.ADD_SCALAR) ? baseDamage * damageAmplifier : damageAmplifier));
        e.setProjectile(arrow);
    }
}
