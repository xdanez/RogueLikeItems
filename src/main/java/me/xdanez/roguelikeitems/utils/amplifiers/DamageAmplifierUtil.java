package me.xdanez.roguelikeitems.utils.amplifiers;

import it.unimi.dsi.fastutil.Pair;
import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.enums.AttackDamage;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.LoreUtil;
import me.xdanez.roguelikeitems.utils.NumberUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

final public class DamageAmplifierUtil {

    private static final NamespacedKey DAMAGE_AMPLIFIER =
            new NamespacedKey(RogueLikeItems.plugin(), "damageAmplifier");

    private static final NamespacedKey ARROW_DAMAGE_AMPLIFIER =
            new NamespacedKey(RogueLikeItems.plugin(), "arrowDamage");

    public static void setDamageAmplifierData(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        Material material = item.getType();
        ConfigData config = ConfigData.getConfigData();

        if (ItemType.isArmor(material) && !config.useArmorDamageAmplifier()) return;

        Pair<Integer, Integer> range = config.getDamageAmplifierRange();

        boolean naturalNumbers = config.useNaturalNumbers();
        int rangeM = naturalNumbers ? 1 : 100;
        int divider = naturalNumbers ? 100 : 10000;

        double amplifier = (double) ThreadLocalRandom.current()
                .nextInt(range.first() * rangeM, range.second() * rangeM + 1) / divider;

        if (naturalNumbers) amplifier = NumberUtil.round(amplifier, 2);

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(DAMAGE_AMPLIFIER, PersistentDataType.DOUBLE, amplifier);
        item.setItemMeta(meta);
        LoreUtil.setDamageAmplifierLore(item, AttackDamage.getDamageFromMaterial(material), material);
    }

    public static boolean hasDamageAmplifier(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().has(DAMAGE_AMPLIFIER);
    }

    public static boolean hasArrowDamageAmplifier(Arrow arrow) {
        return arrow.getPersistentDataContainer().has(ARROW_DAMAGE_AMPLIFIER);
    }

    public static double getDamageAmplifier(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return Objects.requireNonNull(container.get(DAMAGE_AMPLIFIER, PersistentDataType.DOUBLE));
    }

    public static double getArrowDamageAmplifier(Arrow arrow) {
        PersistentDataContainer container = arrow.getPersistentDataContainer();
        return Objects.requireNonNull(container.get(ARROW_DAMAGE_AMPLIFIER, PersistentDataType.DOUBLE));
    }

    public static NamespacedKey getDamageAmplifierKey() {
        return DAMAGE_AMPLIFIER;
    }

    public static NamespacedKey getArrowDamageKey() {
        return ARROW_DAMAGE_AMPLIFIER;
    }
}
