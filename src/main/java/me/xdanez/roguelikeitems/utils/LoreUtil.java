package me.xdanez.roguelikeitems.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// TODO: custom Lore
final public class LoreUtil {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static void setDurabilityLore(ItemStack item, double amplifier) {
        List<Component> lore = item.lore();
        if (lore == null) lore = new ArrayList<>();

        if (amplifier != 0) {
            lore.add(Component.text((amplifier > 0 ? "+" : "")
                            + DECIMAL_FORMAT.format(amplifier * 100.0) + "% Durability")
                    .style(Style.style(TextColor
                            .color(amplifier < 0 ? 255 : 0,
                                    amplifier > 0 ? 255 : 0,
                                    0)))
            );
        }
        item.lore(lore);
    }

    public static void setDamageAmplifierLore(ItemStack item, double amplifier, double extraDmg) {
        List<Component> lore = item.lore();
        if (lore == null) lore = new ArrayList<>();

        Component text = Component.text((amplifier > 0 ? "+" : "")
                        + DECIMAL_FORMAT.format(amplifier * 100.0) + "% ")
                .append(Component.translatable("attribute.name.attack_damage"))
                .append(Component.text(extraDmg != 0 ? " (" + (extraDmg > 0 ? "+" : "")
                        + DECIMAL_FORMAT.format(extraDmg) + ")" : ""))
                .style(Style.style(TextColor
                        .color(amplifier < 0 ? 255 : 0,
                                amplifier > 0 ? 255 : 0,
                                0)));

        if (amplifier != 0) {
            if (!lore.isEmpty() && lore.get(0).contains(Component.translatable("attribute.name.attack_damage"))) {
                lore.set(0, text);
            } else {
                lore.add(0, text);
            }
        }
        item.lore(lore);
    }

}
