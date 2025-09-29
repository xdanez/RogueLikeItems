package me.xdanez.roguelikeitems.listeners;

import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerCraftListener implements Listener {

    @EventHandler
    public void onPlayerCraft(CraftItemEvent e) {
        if (!ConfigData.getConfigData().useCrafting()) return;

        final ItemStack result = e.getInventory().getResult();

        if (result == null) return;
        final Material material = result.getType();

        if (!ItemType.isModifiable(material)) return;

        if (AmplifierUtil.hasAnyAmplifier(result)) return;

        if (e.isShiftClick()) {
            shiftCrafting(e, material);
            return;
        }

        ItemStack item = new ItemStack(material);
        AmplifierUtil.setAmplifiers(item);
        e.setCurrentItem(item);
    }

    private void shiftCrafting(CraftItemEvent e, Material material) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();

        // without armor and shield slots
        int freeSlots = getAmountFreeSlots(player.getInventory()) - 5;

        if (freeSlots <= 0) {
            e.setCancelled(true);
            return;
        }

        int craftedAmt = getCraftedAmount(e);
        if (freeSlots < craftedAmt)
            craftedAmt = freeSlots;

        for (int i = 0; i < craftedAmt; i++) {
            ItemStack item = new ItemStack(material);
            AmplifierUtil.setAmplifiers(item);
            player.getInventory().addItem(item);
        }
        ItemStack[] craftingGrid = e.getInventory().getMatrix();
        for (ItemStack item : craftingGrid) {
            if (item == null || item.getType().equals(Material.AIR)) {
                continue;
            }
            int itemAmt = item.getAmount() - craftedAmt;
            item.setAmount(itemAmt);
        }
        e.getInventory().setMatrix(craftingGrid);
        if (e.getInventory().getResult() == null) {
            e.getInventory().setResult(null);
        }
    }

    private int getCraftedAmount(CraftItemEvent e) {
        final ItemStack result = e.getInventory().getResult();
        if (result == null) return 0;
        int resultAmt = result.getAmount();
        int leastIngredient = -1;
        for (ItemStack item : e.getInventory().getMatrix()) {
            if (item == null || item.getType().equals(Material.AIR)) continue;
            final int re = item.getAmount() * resultAmt;

            if (leastIngredient == -1 || re < leastIngredient)
                leastIngredient = re;
        }
        return leastIngredient;
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
