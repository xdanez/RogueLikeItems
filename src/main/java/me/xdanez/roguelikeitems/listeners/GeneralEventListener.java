package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class GeneralEventListener implements Listener {

    ConfigData config = ConfigData.getConfigData();

    @EventHandler
    public void onPiglinBartering(PiglinBarterEvent e) {
        if (!config.useBartering()) return;

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
        if (!config.useLootTables()) return;

        for (ItemStack item : e.getLoot()) {
            AmplifierUtil.setAmplifiers(item);
        }
    }

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent e) {
        if (!config.useVillagerTrades()) return;

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
    public void onMobSpawn(CreatureSpawnEvent e) {
        if (!config.useMobDrops()) return;

        EntityEquipment entityEquipment = e.getEntity().getEquipment();
        if (entityEquipment == null) return;

        ItemStack mainHand = entityEquipment.getItemInMainHand();
        ItemStack[] armor = entityEquipment.getArmorContents();
        ItemStack offHand = entityEquipment.getItemInOffHand();

        AmplifierUtil.setAmplifiers(mainHand);
        AmplifierUtil.setAmplifiers(offHand);
        for (ItemStack item : armor) {
            AmplifierUtil.setAmplifiers(item);
        }

        entityEquipment.setItemInMainHand(mainHand);
        entityEquipment.setItemInOffHand(offHand);
        entityEquipment.setArmorContents(armor);
    }
}
