package me.xdanez.roguelikeitems.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.xdanez.roguelikeitems.utils.amplifiers.MaxHealthAmplifierUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PlayerArmorChangeListener implements Listener {

    @EventHandler
    public void onPlayerChangeArmor(PlayerArmorChangeEvent e) {
        Player player = e.getPlayer();
        ItemStack[] armor = player.getInventory().getArmorContents();

        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) return;
        maxHealth.setBaseValue(maxHealth.getDefaultValue());
        for (ItemStack piece : armor) {
            if (piece == null) continue;
            if (!MaxHealthAmplifierUtil.hasMaxHealthAmplifier(piece)) continue;
            maxHealth.setBaseValue(maxHealth.getBaseValue() + MaxHealthAmplifierUtil.getMaxHealthAmplifier(piece));
        }
    }
}
