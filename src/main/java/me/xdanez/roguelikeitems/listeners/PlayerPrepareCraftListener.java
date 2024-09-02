package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.models.Durability;
import me.xdanez.roguelikeitems.utils.DurabilityAmplifierUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class PlayerPrepareCraftListener implements Listener {

    @EventHandler
    public void onPlayerPrepareCrafting(PrepareItemCraftEvent e) {
        checkRod(e);
    }

    private ItemStack getRod(ItemStack[] matrix) {
        for (ItemStack item : matrix) {
            if (item == null) continue;
            if (!item.getType().equals(Material.FISHING_ROD)) continue;
            return item;
        }
        return null;
    }

    private void checkRod(PrepareItemCraftEvent e) {
        Recipe recipe = e.getRecipe();
        if (recipe == null) return;

        ItemStack fishingRod = getRod(e.getInventory().getMatrix());
        if (fishingRod == null) return;

        Durability durabilityData = DurabilityAmplifierUtil.getDurabilityData(fishingRod);
        if (durabilityData == null) return;

        ItemStack result = recipe.getResult();
        if (!result.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK)
                && !result.getType().equals(Material.CARROT_ON_A_STICK)) return;

        DurabilityAmplifierUtil.setDurabilityData(result, durabilityData.getAmplifier());
        e.getInventory().setResult(result);
    }
}
