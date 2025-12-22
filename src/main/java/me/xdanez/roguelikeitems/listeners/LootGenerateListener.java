package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

public class LootGenerateListener implements Listener {

    @EventHandler
    public void onLootGenerating(LootGenerateEvent e) {
        if (!ConfigData.getConfigData().useLootTables()) return;

        for (ItemStack item : e.getLoot()) {
            AmplifierUtil.setAmplifiers(item);
        }
    }
}
