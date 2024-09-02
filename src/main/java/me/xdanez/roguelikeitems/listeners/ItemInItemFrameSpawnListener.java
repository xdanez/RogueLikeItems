package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemInItemFrameSpawnListener implements Listener {

    @EventHandler
    public void onItemInItemFrameSpawn(ChunkLoadEvent e) {
        if (!e.isNewChunk()) return;
        Chunk chunk = e.getChunk();
        Entity[] entities = chunk.getEntities();
        List<ItemFrame> itemFrames = getItemFrames(entities);
        if (itemFrames.isEmpty()) return;
        for (ItemFrame itemFrame : itemFrames) {
            ItemStack item = itemFrame.getItem();
            if (!ItemType.isModifiable(item.getType())) continue;
            AmplifierUtil.setAmplifiers(item);
            itemFrame.setItem(item);
        }
    }

    private List<ItemFrame> getItemFrames (Entity[] entities) {
        ArrayList<ItemFrame> itemFrames = new ArrayList<>();
        for (Entity entity : entities) {
            if (!entity.getType().equals(EntityType.ITEM_FRAME)) continue;
            itemFrames.add((ItemFrame) entity);
        }
        return itemFrames;
    }
}
