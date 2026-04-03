package me.xdanez.roguelikeitems.utils;

import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.attribute.AttributeModifierDisplay;
import me.xdanez.roguelikeitems.models.ConfigData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// TODO: custom Lore
final public class LoreUtil {
    final static ConfigData configData = ConfigData.getConfigData();
    final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static void setDurabilityLore(ItemStack item, double amplifier) {
        if (!configData.useLegacyDurabilityLore()) return;
        List<Component> lore = item.lore();
        if (lore == null) lore = new ArrayList<>();

        if (!lore.isEmpty())
            for (Component component : lore)
                if (((TextComponent) component).content().contains("Durability")) return;


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

    public static void setDurabilityLore(ItemAttributeModifiers.Builder attributes, EquipmentSlotGroup group, double amplifier, int color, boolean inPercent) {
        if (configData.useLegacyDurabilityLore()) return;
        attributes.addModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(
                        AmplifierUtil.DISPLAY_DURABILITY_KEY, 0, AttributeModifier.Operation.ADD_NUMBER, group
                ),
                group,
                AttributeModifierDisplay.override(
                        Component.text((amplifier > 0 ? "+" : "")
                                + DECIMAL_FORMAT.format(amplifier * (inPercent ? 100.0 : 1.0))
                                + (inPercent ? "%" : "") + " Durability").color(TextColor.color(color))
                ));
    }
}
