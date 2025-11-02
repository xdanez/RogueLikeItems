package me.xdanez.roguelikeitems.utils.amplifiers;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.attribute.AttributeModifierDisplay;
import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.models.CustomAttributeModifier;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import me.xdanez.roguelikeitems.utils.ConfigUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

final public class AttributeModifiersAmplifierUtil {

    private static final int NEGATIVE = 0xfc5454;
    private static final int POSITIVE = 0x5454fc;
    private static final int NEUTRAL = 0xa8a8a8;
    private static final int GREEN = 0x00a800;

    public static void setAmplifiers(ItemStack item) {
        ItemAttributeModifiers defaultAttributes = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        ItemAttributeModifiers.Builder modifiedAttributes = ItemAttributeModifiers.itemAttributes();

        Material material = item.getType();

        EquipmentSlotGroup group = ItemType.getGroup(material);

        List<CustomAttributeModifier> camList = ConfigData.getConfigData().getCustomAttributeModifiers();
        for (ItemAttributeModifiers.Entry e : defaultAttributes.modifiers()) {

            CustomAttributeModifier cam = camList.stream()
                    .filter(c -> c.attribute() == e.attribute()).findFirst().orElse(null);

            if (cam != null) {
                if (e.attribute().equals(cam.attribute())
                        && cam.attribute().equals(e.attribute())
                        && ConfigUtil.useAmplifier(cam, material)) {
                    double amplifier = AmplifierUtil.getRandomAmplifierValue(cam);
                    double base = e.modifier().getAmount();
                    double extra = cam.inPercent() ? base * amplifier : amplifier;
                    double amount = base + extra;
                    addCustomModifier(modifiedAttributes, amount, amplifier, extra, cam.attribute(), cam.inPercent(), group, item);
                    continue;
                }

            }
            modifiedAttributes.addModifier(e.attribute(), e.modifier(), e.getGroup(), e.display());
        }

        for (CustomAttributeModifier cam : camList) {
            if (cam.attribute() == null) continue;
            if (defaultAttributes.modifiers().stream().anyMatch(d -> d.attribute() == cam.attribute())) continue;
            if (!ConfigUtil.useAmplifier(cam, material)) continue;

            double amount = AmplifierUtil.getRandomAmplifierValue(cam);
            addCustomModifier(modifiedAttributes,
                    amount,
                    cam.inPercent() ? AttributeModifier.Operation.ADD_SCALAR : AttributeModifier.Operation.ADD_NUMBER,
                    cam,
                    group,
                    item);
        }

        item.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, modifiedAttributes.build());
    }

    private static void addCustomModifier(
            ItemAttributeModifiers.Builder modifierAttributes,
            double amount,
            double amplifier,
            double extra,
            Attribute attribute,
            boolean inPercent,
            EquipmentSlotGroup group,
            ItemStack item
    ) {
        boolean showAdjustedValues = ConfigData.getConfigData().showAdjustedValues();
        boolean dmgOrSpd = attribute.equals(Attribute.ATTACK_DAMAGE) || attribute.equals(Attribute.ATTACK_SPEED);
        modifierAttributes.addModifier(attribute,
                new AttributeModifier(
                        key(attribute),
                        amount,
                        AttributeModifier.Operation.ADD_NUMBER,
                        group),
                group,
                AttributeModifierDisplay.override(
                        Component.text((dmgOrSpd ? " " : "")
                                        + (amount > 0 && !attribute.equals(Attribute.ATTACK_DAMAGE) && !attribute.equals(Attribute.ATTACK_SPEED) ? "+" : "")
                                        + (Math.round((amount + (showAdjustedValues ? (attribute.equals(Attribute.ATTACK_DAMAGE) ? 1 : attribute.equals(Attribute.ATTACK_SPEED) ? 4 : 0) : 0)) * 100.0) / 100.0)
                                        + " (" + (amplifier > 0 ? "+" : "")
                                        + (inPercent ? ((Math.round(amplifier * 100)) + "%") : Math.round(amplifier))
                                        + (inPercent && showAdjustedValues ? " / " + Math.round(extra * 100.0) / 100.0 : "") + ") ")
                                .append(Component.translatable(attribute.translationKey()))
                                .color(TextColor.color(attribute.getSentiment().equals(Attribute.Sentiment.NEUTRAL) ? NEUTRAL
                                        : dmgOrSpd ? GREEN
                                        : amount > 0 ? POSITIVE
                                        : NEGATIVE))
                ));
        if (item != null) setPersistentData(item, inPercent, attribute);
    }

    private static void addCustomModifier(
            ItemAttributeModifiers.Builder modifiedAttributes,
            double amount,
            AttributeModifier.Operation operation,
            CustomAttributeModifier cam,
            EquipmentSlotGroup group,
            ItemStack item) {
        modifiedAttributes.addModifier(cam.attribute(),
                new AttributeModifier(key(cam.attribute()), amount, operation, group), group,
                AttributeModifierDisplay.reset()
        );
        setPersistentData(item, cam.inPercent(), cam.attribute());
    }

    private static NamespacedKey key(Attribute attribute) {
        return new NamespacedKey(RogueLikeItems.plugin,
                attribute.key().toString()
                        .replace(":", "_") + "_amplifier");
    }

    private static void setPersistentData(ItemStack item, boolean inPercent, Attribute attribute) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key(attribute), PersistentDataType.BOOLEAN, inPercent);
        item.setItemMeta(meta);
    }

    public static ItemAttributeModifiers.Builder getAttributeModifiersNetherite(ItemStack input, ItemStack result) {
        ItemAttributeModifiers.Builder modifiedAttributes = ItemAttributeModifiers.itemAttributes();

        ItemAttributeModifiers inputAttributes = input.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        ItemAttributeModifiers ogInputAttributes = ItemStack.of(input.getType()).getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        ItemAttributeModifiers resOgAttributes = ItemStack.of(result.getType()).getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);

        for (ItemAttributeModifiers.Entry e : inputAttributes.modifiers()) {
            ItemAttributeModifiers.Entry ogInputEntry = ogInputAttributes.modifiers().stream()
                    .filter(i -> i.attribute().equals(e.attribute())).findFirst().orElse(null);


            ItemAttributeModifiers.Entry ogResEntry = resOgAttributes.modifiers().stream()
                    .filter(i -> i.attribute().equals(e.attribute())).findFirst().orElse(null);

            if (ogInputEntry != null && input.getItemMeta().getPersistentDataContainer().has(key(e.attribute()))) {
                double base = ogInputEntry.modifier().getAmount();
                double modifiedValue = e.modifier().getAmount();
                boolean inPercent = input.getItemMeta().getPersistentDataContainer().get(key(e.attribute()), PersistentDataType.BOOLEAN);
                double amplifier;
                if (inPercent) {
                    amplifier = (modifiedValue / base) - 1;
                } else {
                    amplifier = modifiedValue - base;
                }

                if (ogResEntry != null) base++;

                double extra = inPercent ? base * amplifier : amplifier;
                double amount = base + extra;

                addCustomModifier(modifiedAttributes, amount, amplifier, extra, e.attribute(), inPercent, e.getGroup(), null);
                continue;
            }

            if (ogResEntry != null) {
                modifiedAttributes.addModifier(ogResEntry.attribute(), ogResEntry.modifier(), ogResEntry.getGroup(), ogResEntry.display());
                continue;
            }

            modifiedAttributes.addModifier(e.attribute(), e.modifier(), e.getGroup(), e.display());
        }

        for (ItemAttributeModifiers.Entry e : resOgAttributes.modifiers()) {
            if (modifiedAttributes.build().modifiers().stream().anyMatch(i -> i.attribute().equals(e.attribute())))
                continue;

            modifiedAttributes.addModifier(e.attribute(), e.modifier(), e.getGroup(), e.display());
        }

        return modifiedAttributes;
    }

    public static void copyAttributes(ItemStack origin, ItemStack goal) {
        goal.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, origin.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS));
    }

}
