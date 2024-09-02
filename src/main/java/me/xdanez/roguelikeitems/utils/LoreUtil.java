package me.xdanez.roguelikeitems.utils;

import me.xdanez.roguelikeitems.enums.ItemType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// TODO: custom Lore
final public class LoreUtil {

    public static void setDurabilityLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore();
        if (lore == null) lore = new ArrayList<>();

        if (!DurabilityAmplifierUtil.hasDurabilityData(item)) return;
        double amplifier = DurabilityAmplifierUtil.getAmplifier(item);
        int amplifiedMaxDurability = DurabilityAmplifierUtil.getAmplifiedMaxDurability(item);
        int currentDurability = DurabilityAmplifierUtil.getCurrentDurability(item);

        System.out.println(amplifier);

        if (amplifier != 0) {
            lore.add(
                    Component.text((amplifier > 0 ? "+" : "")
                                    + new DecimalFormat("#.##%").format(amplifier) + " Durability")
                            .decorate(TextDecoration.BOLD)
                            .style(Style.style(TextColor
                                    .color(amplifier < 0 ? 255 : 0,
                                            amplifier > 0 ? 255 : 0,
                                            0)))
            );
        } else {
            lore.add(Component.text(""));
        }
        lore.add(Component.text("Durability: " + currentDurability + "/" + amplifiedMaxDurability));
        meta.lore(lore);
        item.setItemMeta(meta);
    }

    public static void setDamageAmplifierLore(ItemStack item, double baseDamage, Material material) {
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore();
        if (lore == null) lore = new ArrayList<>();

        if (!DamageAmplifierUtil.hasDamageAmplifier(item)) return;
        double amplifier = DamageAmplifierUtil.getDamageAmplifier(item);
        String extraDmg = (amplifier > 0 ? "+" : "")
                + NumberUtil.round(baseDamage * amplifier, 2);

        if (amplifier != 0) {
            lore.add(0,
                    Component.text((amplifier > 0 ? "+" : "")
                                    + new DecimalFormat("#.##%").format(amplifier) + " Damage " +
                                    (!ItemType.isArmor(material)
                                            && !material.equals(Material.BOW)
                                            && !material.equals(Material.CROSSBOW) ? "(" + extraDmg + ")" : ""))
                            .decorate(TextDecoration.BOLD)
                            .style(Style.style(TextColor
                                    .color(amplifier < 0 ? 255 : 0,
                                            amplifier > 0 ? 255 : 0,
                                            0)))
            );
        }
        meta.lore(lore);
        item.setItemMeta(meta);
    }

    public static void updateDurabilityLore(ItemMeta meta, int updatedDurability, int maxDurability) {
        List<Component> lore = meta.lore();
        lore.set(lore.size() - 1, Component.text("Durability: " + updatedDurability + "/" + maxDurability)
                .decorate(TextDecoration.BOLD));
        meta.lore(lore);
    }

    public static void clearLore(ItemMeta meta) {
        meta.lore(null);
    }

}
