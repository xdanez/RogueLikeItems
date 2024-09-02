package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.enums.AttackDamage;
import me.xdanez.roguelikeitems.models.Durability;
import me.xdanez.roguelikeitems.models.DurabilityDataType;
import me.xdanez.roguelikeitems.utils.DamageAmplifierUtil;
import me.xdanez.roguelikeitems.utils.DurabilityAmplifierUtil;
import me.xdanez.roguelikeitems.utils.LoreUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerPrepareAnvilListener implements Listener {

    @EventHandler
    public void playerPrepareAnvil(PrepareAnvilEvent e) {
        ItemStack result = e.getResult();
        if (result == null) return;

        ItemStack input = e.getInventory().getFirstItem();
        if (input == null) return;
        if (!DurabilityAmplifierUtil.hasDurabilityData(input)) return;

        ItemMeta resultMeta = result.getItemMeta();
        Damageable rmDmg = (Damageable) resultMeta;

        int maxDurability = result.getType().getMaxDurability();
        int newDurability = maxDurability - rmDmg.getDamage();
        double ampDurabilityPer = (double) newDurability / maxDurability;
        int ampMaxDurability = DurabilityAmplifierUtil.getAmplifiedMaxDurability(input);
        int newAmpDurability = (int) (ampMaxDurability * ampDurabilityPer);
        PersistentDataContainer container = resultMeta.getPersistentDataContainer();
        container.set(DurabilityAmplifierUtil.getKey(), new DurabilityDataType(), new Durability(
                ampMaxDurability, DurabilityAmplifierUtil.getAmplifier(input), newAmpDurability
        ));
        double attackDamage = AttackDamage.getDamageFromMaterial(result.getType());
        if (DamageAmplifierUtil.hasDamageAmplifier(input)) {
            container.set(DamageAmplifierUtil.getDamageAmplifierKey(),
                    PersistentDataType.DOUBLE, DamageAmplifierUtil.getDamageAmplifier(input)
            );
        }
        LoreUtil.clearLore(resultMeta);
        result.setItemMeta(resultMeta);
        LoreUtil.setDurabilityLore(result);
        LoreUtil.setDamageAmplifierLore(result, attackDamage, result.getType());
    }
}
