package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LootGenerateListener implements Listener {

    @EventHandler
    public void onLootGenerating(LootGenerateEvent e) {
        if (!ConfigData.getConfigData().useLootTables()) return;

        List<ItemStack> loot = e.getLoot();

        for (ItemStack item : loot) {
            AmplifierUtil.setAmplifiers(item);
        }
    }
}
