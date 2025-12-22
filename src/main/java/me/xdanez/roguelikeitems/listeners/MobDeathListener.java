package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class MobDeathListener implements Listener {

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        if (!ConfigData.getConfigData().useMobDrops()) return;
        if (!(e.getEntity() instanceof Monster)) return;
        for (ItemStack item : e.getDrops()) {
            AmplifierUtil.setAmplifiers(item);
        }
    }
}
