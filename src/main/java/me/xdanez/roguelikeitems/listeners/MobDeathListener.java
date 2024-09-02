package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MobDeathListener implements Listener {

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        if (!ConfigData.getConfigData().useMobDrops()) return;
        LivingEntity entity = e.getEntity();
        if (!(entity instanceof Monster)) return;
        List<ItemStack> drops = e.getDrops();
        for (ItemStack item : drops) {
            AmplifierUtil.setAmplifiers(item);
        }
    }
}
