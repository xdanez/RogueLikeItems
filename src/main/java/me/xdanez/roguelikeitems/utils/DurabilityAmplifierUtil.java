package me.xdanez.roguelikeitems.utils;

import me.xdanez.roguelikeitems.RogueLikeItems;
import it.unimi.dsi.fastutil.Pair;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.models.Durability;
import me.xdanez.roguelikeitems.models.DurabilityDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

final public class DurabilityAmplifierUtil {

    private static final NamespacedKey key = new NamespacedKey(RogueLikeItems.getPlugin(RogueLikeItems.class), "durability");

    public static void setDurabilityData(ItemStack item) {

        ConfigData config = ConfigData.getConfigData();
        Pair<Integer, Integer> range = config.getDurabilityRange();
        boolean naturalNumbers = config.useNaturalNumbers();
        int rangeM = naturalNumbers ? 1 : 100;
        int divider = naturalNumbers ? 100 : 10000;

        double amplifier = (double) ThreadLocalRandom.current()
                .nextInt(range.first() * rangeM, range.second() * rangeM + 1) / divider;

        if (naturalNumbers) amplifier = NumberUtil.round(amplifier, 2);

        int maxDurabilityOfItem = item.getType().getMaxDurability();
        int amplifiedMaxDurability = (int) Math.ceil(maxDurabilityOfItem + (maxDurabilityOfItem * amplifier));

        ItemMeta meta = item.getItemMeta();
        Damageable iDmg = (Damageable) meta;
        int currentDurability = item.getType().getMaxDurability() - iDmg.getDamage();
        double durabilityLeft = (double) currentDurability / maxDurabilityOfItem;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, new DurabilityDataType(),
                new Durability(amplifiedMaxDurability, amplifier, (int) Math.ceil(amplifiedMaxDurability * durabilityLeft))
        );
        item.setItemMeta(meta);
        LoreUtil.setDurabilityLore(item);
    }

    public static void setDurabilityData(ItemStack item, double amplifier) {
        ItemMeta meta = item.getItemMeta();
        int maxDurabilityOfItem = item.getType().getMaxDurability();

        int amplifiedMaxDurability = (int) Math.ceil(maxDurabilityOfItem + (maxDurabilityOfItem * amplifier));

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, new DurabilityDataType(),
                new Durability(amplifiedMaxDurability, amplifier, amplifiedMaxDurability));
        LoreUtil.setDurabilityLore(item);
        item.setItemMeta(meta);
    }

    public static Durability getDurabilityData(@NotNull ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.get(key, new DurabilityDataType());
    }

    public static void damage(ItemStack item, int amount) {
        ItemMeta meta = item.getItemMeta();
        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        int ogDurability = item.getType().getMaxDurability();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        Durability durability = container.get(key, new DurabilityDataType());
        if (durability == null) return;

        int durabilityLevel = enchantments.getOrDefault(Enchantment.UNBREAKING, 0);
        int breakChance = ThreadLocalRandom.current().nextInt(100 + 1);

        if (durabilityLevel == 0
                || (durabilityLevel == 1 && breakChance <= 50)
                || (durabilityLevel == 2 && breakChance <= 33)
                || (durabilityLevel == 3 && breakChance <= 25)
        ) {
            int amplifiedMaxDurability = durability.getAmplifiedMaxDurability();
            durability.setCurrentDurability(durability.getCurrentDurability() - amount);
            double percentageLeft = (double) durability.getCurrentDurability() / amplifiedMaxDurability;
            int ogDurabilityLeft = (int) Math.ceil(ogDurability * percentageLeft);

            Damageable dmg = (Damageable) meta;
            if (amplifiedMaxDurability <= 0) {
                dmg.setDamage(ogDurability);
            } else {
                dmg.setDamage(ogDurability - ogDurabilityLeft);
            }

            container.set(key, new DurabilityDataType(), durability);
            LoreUtil.updateDurabilityLore(meta, durability.getCurrentDurability(), durability.getAmplifiedMaxDurability());
            item.setItemMeta(meta);
        }
    }

    public static void repair(ItemStack item, int repairAmt) {
        if (!hasDurabilityData(item)) return;
        Damageable iDmg = (Damageable) item.getItemMeta();
        int ogMaxDurability = item.getType().getMaxDurability();
        int ogRepairedDurability = ogMaxDurability - iDmg.getDamage() + repairAmt;
        double durabilityLeft = (double) ogRepairedDurability / ogMaxDurability;
        int amplifiedMaxDurability = getAmplifiedMaxDurability(item);
        int repairedDurability = (int) NumberUtil.round(amplifiedMaxDurability * durabilityLeft, 0);

        PersistentDataContainer container = iDmg.getPersistentDataContainer();
        Durability durability = container.get(key, new DurabilityDataType());
        assert durability != null;

        durability.setCurrentDurability(repairedDurability);
        container.set(key, new DurabilityDataType(), durability);
        LoreUtil.updateDurabilityLore(iDmg, durability.getCurrentDurability(), durability.getAmplifiedMaxDurability());
        item.setItemMeta(iDmg);
    }

    public static double getAmplifier(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return Objects.requireNonNull(container.get(key, new DurabilityDataType())).getAmplifier();
    }

    public static int getAmplifiedMaxDurability(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return Objects.requireNonNull(container.get(key, new DurabilityDataType())).getAmplifiedMaxDurability();
    }

    public static int getCurrentDurability(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return Objects.requireNonNull(container.get(key, new DurabilityDataType())).getCurrentDurability();
    }

    public static boolean hasDurabilityData(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().has(key);
    }

    public static NamespacedKey getKey() {
        return key;
    }
}
