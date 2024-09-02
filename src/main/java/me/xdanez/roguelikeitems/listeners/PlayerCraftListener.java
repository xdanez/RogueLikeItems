package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import me.xdanez.roguelikeitems.utils.DamageAmplifierUtil;
import me.xdanez.roguelikeitems.utils.DurabilityAmplifierUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class PlayerCraftListener implements Listener {

    @EventHandler
    public void onPlayerCraft(CraftItemEvent e) {
        CraftingInventory craftingInventory = e.getInventory();

        // important for shift-crafting
        if (craftingInventory.isEmpty()) return;
        if (craftingInventory.getItem(0) == null) return; // result

        Player player = (Player) e.getWhoClicked();
        boolean freeSpace = player.getInventory().firstEmpty() != -1;

        if (!freeSpace) return;
        final ItemStack result = e.getInventory().getResult();

        if (result == null) return;
        final Material material = result.getType();

        if (!ItemType.isModifiable(material)) return;

        if (DurabilityAmplifierUtil.hasDurabilityData(result)) return;
        if (DamageAmplifierUtil.hasDamageAmplifier(result)) return;

        if (e.isShiftClick()) {
            shiftCrafting(e, player, material);
            return;
        }

        ItemStack item = new ItemStack(material);
        AmplifierUtil.setAmplifiers(item);
        e.setCurrentItem(item);
    }

    // This is stupid... why can't Paper be better at this?
    private void shiftCrafting(CraftItemEvent e, Player player, Material material) {
        int craftedAmt = getCraftedAmount(e);

        // without armor and shield slots
        int freeSlots = getAmountFreeSlots(player.getInventory()) - 5;

        if (freeSlots < 0)  {
            e.setCancelled(true);
            return;
        }

        if (freeSlots < craftedAmt)
            craftedAmt = freeSlots;

        for (int i = 0; i < craftedAmt; i++) {
            ItemStack item = new ItemStack(material);
            AmplifierUtil.setAmplifiers(item);
            player.getInventory().addItem(item);
        }
        ItemStack[] craftingGrid = e.getInventory().getMatrix();
        for (ItemStack item : craftingGrid) {
            if (item == null || item.getType().equals(Material.AIR)) continue;
            item.setAmount(item.getAmount() - craftedAmt);
        }
    }

    private int getCraftedAmount(CraftItemEvent e) {
        if (e.isShiftClick()) {
            final ItemStack recipeResult = e.getRecipe().getResult();
            final int resultAmt = recipeResult.getAmount();
            int leastIngredient = -1;
            for (ItemStack item : e.getInventory().getMatrix()) {
                if (item == null || item.getType().equals(Material.AIR)) continue;
                final int re = item.getAmount() * resultAmt;

                if (leastIngredient != -1 && re > leastIngredient) continue;
                leastIngredient = item.getAmount() * resultAmt;
            }
            return leastIngredient;
        }
        return Objects.requireNonNull(e.getCurrentItem()).getAmount();
    }

    private int getAmountFreeSlots(PlayerInventory inventory) {
        int freeSlots = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType().equals(Material.AIR)) {
                freeSlots++;
            }
        }
        return freeSlots;
    }
}
