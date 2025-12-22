package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

public class ItemInItemFrameSpawnListener implements Listener {

    @EventHandler
    public void onItemInItemFrameSpawn(ChunkLoadEvent e) {
        if (!e.isNewChunk()) return;
        for (Entity entity : e.getChunk().getEntities()) {
            if (!entity.getType().equals(EntityType.ITEM_FRAME)) continue;
            ItemFrame itemFrame = (ItemFrame) entity;
            ItemStack item = itemFrame.getItem();
            AmplifierUtil.setAmplifiers(item);
            itemFrame.setItem(item);
        }
    }
}
