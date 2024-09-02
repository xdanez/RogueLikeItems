package me.xdanez.roguelikeitems.enums;

import org.bukkit.Material;

import java.util.Set;

public enum ItemType {
    ARMOR,
    WEAPON,
    TOOL;

    private final static Set<Material> armor = Set.of(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
            Material.SHIELD, Material.TURTLE_HELMET, Material.ELYTRA
    );

    private final static Set<Material> weapons = Set.of(
            Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.STONE_SWORD, Material.STONE_AXE,
            Material.GOLDEN_SWORD, Material.GOLDEN_AXE, Material.IRON_SWORD, Material.IRON_AXE,
            Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.NETHERITE_SWORD, Material.NETHERITE_AXE,
            Material.BOW, Material.TRIDENT, Material.CROSSBOW, Material.MACE
    );

    private final static Set<Material> tools = Set.of(
            Material.WOODEN_PICKAXE, Material.WOODEN_AXE, Material.WOODEN_SHOVEL, Material.WOODEN_HOE,
            Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SHOVEL, Material.STONE_HOE,
            Material.GOLDEN_PICKAXE, Material.GOLDEN_AXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_HOE,
            Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SHOVEL, Material.IRON_HOE,
            Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE,
            Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE,
            Material.SHEARS, Material.FISHING_ROD, Material.CARROT_ON_A_STICK, Material.WARPED_FUNGUS_ON_A_STICK,
            Material.BRUSH
    );

    private final static Set<Material> noDamage = Set.of(
            Material.FISHING_ROD, Material.CARROT_ON_A_STICK, Material.WARPED_FUNGUS_ON_A_STICK,
            Material.SHEARS, Material.SHIELD, Material.BRUSH
    );

    public static ItemType getItemType(Material material) {
        if (armor.contains(material)) return ItemType.ARMOR;
        if (weapons.contains(material)) return ItemType.WEAPON;
        if (tools.contains(material)) return ItemType.TOOL;
        return null;
    }

    public static boolean isArmor(Material material) {
        return armor.contains(material);
    }

    public static boolean isWeapon(Material material) {
        return weapons.contains(material);
    }

    public static boolean isTool(Material material) {
        return tools.contains(material);
    }

    public static boolean hasNoDamage(Material material) {
        return noDamage.contains(material);
    }

    public static boolean isModifiable(Material material) {
        return isTool(material) || isWeapon(material) || isArmor(material);
    }
}
