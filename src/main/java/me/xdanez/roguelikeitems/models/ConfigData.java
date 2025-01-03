package me.xdanez.roguelikeitems.models;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConfigData {

    private static final ConfigData CONFIG_DATA = new ConfigData();

    private ConfigData() {
    }

    public static ConfigData getConfigData() {
        return CONFIG_DATA;
    }

    private Pair<Integer, Integer> durabilityRange;
    private Pair<Integer, Integer> damageAmplifierRange;
    private Pair<Integer, Integer> maxHealthAmplifierRange;
    private List<ItemStack> ignoreItemList;
    private boolean useArmorDamageAmplifier;
    private boolean useNaturalNumbers;
    private boolean useLootTables;
    private boolean useMobDrops;
    private boolean useDurabilityAmplifier;
    private boolean useDamageAmplifier;
    private boolean useVillagerTrades;
    private boolean useCrafting;
    private boolean useMaxHealthAmplifier;
    private AmplifierChance amplifierChance;

    public AmplifierChance getAmplifierChance() {
        return amplifierChance;
    }

    public void setAmplifierChance(AmplifierChance amplifierChance) {
        this.amplifierChance = amplifierChance;
    }

    public Pair<Integer, Integer> getMaxHealthAmplifierRange() {
        return maxHealthAmplifierRange;
    }

    public void setMaxHealthAmplifierRange(int from, int to) {
        this.maxHealthAmplifierRange = Pair.of(from, to);
    }

    public boolean useMaxHealthOnTools() {
        return maxHealthOnTools;
    }

    public void setMaxHealthOnTools(boolean maxHealthOnTools) {
        this.maxHealthOnTools = maxHealthOnTools;
    }

    public boolean useMaxHealthAmplifier() {
        return useMaxHealthAmplifier;
    }

    public void setUseMaxHealthAmplifier(boolean useMaxHealthAmplifier) {
        this.useMaxHealthAmplifier = useMaxHealthAmplifier;
    }

    private boolean maxHealthOnTools;

    public boolean useCrafting() {
        return useCrafting;
    }

    public void setUseCrafting(boolean useCrafting) {
        this.useCrafting = useCrafting;
    }

    public boolean useVillagerTrades() {
        return useVillagerTrades;
    }

    public void setUseVillagerTrades(boolean useVillagerTrades) {
        this.useVillagerTrades = useVillagerTrades;
    }

    public Pair<Integer, Integer> getDurabilityRange() {
        return durabilityRange;
    }

    public void setDurabilityRange(Integer from, Integer to) {
        this.durabilityRange = Pair.of(from, to);
    }

    public Pair<Integer, Integer> getDamageAmplifierRange() {
        return damageAmplifierRange;
    }

    public void setDamageAmplifierRange(Integer from, Integer to) {
        this.damageAmplifierRange = Pair.of(from, to);
    }

    public boolean useArmorDamageAmplifier() {
        return useArmorDamageAmplifier;
    }

    public void setUseArmorDamageAmplifier(boolean useArmorDamageAmplifier) {
        this.useArmorDamageAmplifier = useArmorDamageAmplifier;
    }

    public boolean useNaturalNumbers() {
        return useNaturalNumbers;
    }

    public void setUseNaturalNumbers(boolean useNaturalNumbers) {
        this.useNaturalNumbers = useNaturalNumbers;
    }

    public boolean useLootTables() {
        return useLootTables;
    }

    public void setUseLootTables(boolean useLootTables) {
        this.useLootTables = useLootTables;
    }

    public boolean useMobDrops() {
        return useMobDrops;
    }

    public void setUseMobDrops(boolean useMobDrops) {
        this.useMobDrops = useMobDrops;
    }

    public boolean useDurabilityAmplifier() {
        return useDurabilityAmplifier;
    }

    public void setUseDurabilityAmplifier(boolean useDurabilityAmplifier) {
        this.useDurabilityAmplifier = useDurabilityAmplifier;
    }

    public boolean useDamageAmplifier() {
        return useDamageAmplifier;
    }

    public void setUseDamageAmplifier(boolean useDamageAmplifier) {
        this.useDamageAmplifier = useDamageAmplifier;
    }

    public List<ItemStack> getIgnoreItemList() {
        return ignoreItemList;
    }

    public void setIgnoreItemList(List<ItemStack> ignoreItemList) {
        this.ignoreItemList = ignoreItemList;
    }
}
