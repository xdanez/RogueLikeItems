package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class GeneralEventListener implements Listener {

    @EventHandler
    public void onPiglinBartering(PiglinBarterEvent e) {
        if (!ConfigData.getConfigData().useBartering()) return;

        for (ItemStack item : e.getOutcome()) {
            AmplifierUtil.setAmplifiers(item);
        }
    }

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

    @EventHandler
    public void onLootGenerating(LootGenerateEvent e) {
        if (!ConfigData.getConfigData().useLootTables()) return;

        for (ItemStack item : e.getLoot()) {
            AmplifierUtil.setAmplifiers(item);
        }
    }

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent e) {
        if (!ConfigData.getConfigData().useVillagerTrades()) return;

        MerchantRecipe trade = e.getRecipe();
        ItemStack item = trade.getResult();
        AmplifierUtil.setAmplifiers(item);
        MerchantRecipe newTrade = new MerchantRecipe(
                item,
                trade.getUses(),
                trade.getMaxUses(),
                trade.hasExperienceReward(),
                trade.getVillagerExperience(),
                trade.getPriceMultiplier(),
                trade.getDemand(),
                trade.getSpecialPrice(),
                trade.shouldIgnoreDiscounts()
        );
        newTrade.setIngredients(trade.getIngredients());
        e.setRecipe(newTrade);
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        if (!ConfigData.getConfigData().useMobDrops()) return;
        if (!(e.getEntity() instanceof Monster)) return;
        for (ItemStack item : e.getDrops()) {
            AmplifierUtil.setAmplifiers(item);
        }
    }
}
