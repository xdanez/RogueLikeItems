package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class VillagerAcquireTradeListener implements Listener {

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
}
