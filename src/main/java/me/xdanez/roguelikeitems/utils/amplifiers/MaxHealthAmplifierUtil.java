package me.xdanez.roguelikeitems.utils.amplifiers;

import it.unimi.dsi.fastutil.Pair;
import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.models.ConfigData;
import me.xdanez.roguelikeitems.utils.LoreUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

final public class MaxHealthAmplifierUtil {

    private static final NamespacedKey MAX_HEALTH_AMPLIFIER =
            new NamespacedKey(RogueLikeItems.plugin(), "maxHealth");

    public static void setMaxHealthData(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        ConfigData config = ConfigData.getConfigData();

        Pair<Integer, Integer> range = config.getMaxHealthAmplifierRange();

        int amplifier = ThreadLocalRandom.current().nextInt(range.first(), range.second() + 1);

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(MAX_HEALTH_AMPLIFIER, PersistentDataType.INTEGER, amplifier);
        item.setItemMeta(meta);
        LoreUtil.setMaxHealthAmplifierLore(item, amplifier);
    }

    public static int getMaxHealthAmplifier(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return Objects.requireNonNull(container.get(MAX_HEALTH_AMPLIFIER, PersistentDataType.INTEGER));
    }

    public static boolean hasMaxHealthAmplifier(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().has(MAX_HEALTH_AMPLIFIER);
    }
}
