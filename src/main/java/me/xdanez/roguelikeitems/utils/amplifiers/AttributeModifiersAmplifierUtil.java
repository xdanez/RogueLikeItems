package me.xdanez.roguelikeitems.utils.amplifiers;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.attribute.AttributeModifierDisplay;
import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.Config;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import me.xdanez.roguelikeitems.utils.ConfigUtil;
import me.xdanez.roguelikeitems.utils.LoreUtil;
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

final public class AttributeModifiersAmplifierUtil {

    public static final NamespacedKey DAMAGE_AMPLIFIER =
            new NamespacedKey(RogueLikeItems.plugin, "damageAmplifier");

    private static final NamespacedKey MAX_HEALTH_AMPLIFIER =
            new NamespacedKey(RogueLikeItems.plugin, "maxHealthAmplifier");

    public static void setAmplifiers(ItemStack item) {
        ConfigData config = ConfigData.getConfigData();

        ItemAttributeModifiers defaultAttributes = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        ItemAttributeModifiers.Builder modifiedAttributes = ItemAttributeModifiers.itemAttributes();

        Material material = item.getType();

        boolean isRanged = ItemType.isRanged(material);
        boolean isArmor = ItemType.isArmor(material);
        boolean isShield = ItemType.isShield(material);

        boolean useDamageAmplifier = ConfigUtil.useAmplifier(Config.USE_DAMAGE_AMPLIFIER);
        boolean useMaxHealth = ConfigUtil.useAmplifier(Config.USE_MAX_HEALTH_AMPLIFIER);

        EquipmentSlotGroup group =
                isArmor ? EquipmentSlotGroup.ARMOR
                        : isRanged || isShield ? EquipmentSlotGroup.HAND
                        : EquipmentSlotGroup.MAINHAND;

        double damageAmplifier = AmplifierUtil.getRandomAmplifierValue(Config.DAMAGE_AMPLIFIER_RANGE);
        double maxHealthAmplifier = AmplifierUtil.getRandomAmplifierValue(Config.MAX_HEALTH_AMPLIFIER_RANGE);

        if (!isRanged) {
            for (ItemAttributeModifiers.Entry e : defaultAttributes.modifiers()) {
                if (useDamageAmplifier && e.attribute().equals(Attribute.ATTACK_DAMAGE)) {
                    double base = e.modifier().getAmount();
                    double extraDmg = base * damageAmplifier;
                    double amount = base + extraDmg;
                    LoreUtil.setDamageAmplifierLore(item, damageAmplifier, extraDmg);
                    setDamageAmplifier(modifiedAttributes, amount, AttributeModifier.Operation.ADD_NUMBER, group, true);
                    continue;
                }
                modifiedAttributes.addModifier(e.attribute(), e.modifier(), e.getGroup(), e.display());
            }

            if (isArmor && useDamageAmplifier && config.useArmorDamageAmplifier()) {
                setDamageAmplifier(modifiedAttributes, damageAmplifier, AttributeModifier.Operation.ADD_SCALAR, group, false);
            }
        } else {
            if (useDamageAmplifier) {
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(DAMAGE_AMPLIFIER, PersistentDataType.DOUBLE, damageAmplifier);
                item.setItemMeta(meta);
                LoreUtil.setDamageAmplifierLore(item, damageAmplifier, 0);
            }
        }

        if (useMaxHealth && (isArmor || isShield || config.useMaxHealthOnTools())) {
            modifiedAttributes.addModifier(Attribute.MAX_HEALTH,
                    new AttributeModifier(MAX_HEALTH_AMPLIFIER,
                            maxHealthAmplifier,
                            AttributeModifier.Operation.ADD_NUMBER,
                            group
                    )
            );
        }

        item.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, modifiedAttributes.build());
    }

    private static void setDamageAmplifier(ItemAttributeModifiers.Builder modifiedAttributes,
                                           double amount,
                                           AttributeModifier.Operation operation,
                                           EquipmentSlotGroup group,
                                           boolean overrideDisplay) {
        modifiedAttributes.addModifier(Attribute.ATTACK_DAMAGE,
                new AttributeModifier(DAMAGE_AMPLIFIER, amount, operation, group),
                group,
                overrideDisplay ? AttributeModifierDisplay.override(
                        Component.text(" " + Math.round((amount + 1) * 100.0) / 100.0 + " ")
                                .append(Component.translatable("attribute.name.attack_damage"))
                                .color(TextColor.color(0, 168, 0))
                ) : AttributeModifierDisplay.reset()
        );
    }

    public static ItemAttributeModifiers.Builder getAttributeModifiersNetherite(ItemStack input, ItemStack result) {
        ItemAttributeModifiers.Entry ogEntry = ItemStack.of(input.getType()).getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                .modifiers().stream()
                .filter(e -> e.attribute().equals(Attribute.ATTACK_DAMAGE)).findFirst().orElse(null);
        if (ogEntry == null) return null;
        double ogBase = ogEntry.modifier().getAmount();

        ItemAttributeModifiers.Entry modifiedEntry = input.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                .modifiers().stream()
                .filter(e -> e.attribute().equals(Attribute.ATTACK_DAMAGE)).findFirst().orElse(null);
        if (modifiedEntry == null) return null;
        double modifiedBase = modifiedEntry.modifier().getAmount();

        if (ogBase == modifiedBase) return null;

        double damageAmplifier = (modifiedBase / ogBase) - 1;
        double extraDmg = (ogBase + 1) * damageAmplifier;
        double amount = ogBase + 1 + extraDmg;

        ItemAttributeModifiers defaultAttributes = result.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        ItemAttributeModifiers.Builder modifiedAttributes = ItemAttributeModifiers.itemAttributes();

        for (ItemAttributeModifiers.Entry e : defaultAttributes.modifiers()) {
            if (e.attribute().equals(Attribute.ATTACK_DAMAGE)) {
                LoreUtil.setDamageAmplifierLore(result, damageAmplifier, extraDmg);
                setDamageAmplifier(modifiedAttributes,
                        amount,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlotGroup.MAINHAND,
                        true
                );
                continue;
            }
            modifiedAttributes.addModifier(e.attribute(), e.modifier(), e.getGroup(), e.display());
        }
        return modifiedAttributes;
    }

}
