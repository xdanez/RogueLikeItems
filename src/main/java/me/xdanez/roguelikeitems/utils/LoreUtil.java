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

    public static void setDurabilityLore(ItemStack item, double amplifier) {
        List<Component> lore = item.lore();
        if (lore == null) lore = new ArrayList<>();

        if (amplifier != 0) {
            lore.add(Component.text((amplifier > 0 ? "+" : "")
                            + new DecimalFormat("#.##").format(amplifier * 100.0) + "% Durability")
                    .style(Style.style(TextColor
                            .color(amplifier < 0 ? 255 : 0,
                                    amplifier > 0 ? 255 : 0,
                                    0)))
            );
        }
        item.lore(lore);
    }

}
