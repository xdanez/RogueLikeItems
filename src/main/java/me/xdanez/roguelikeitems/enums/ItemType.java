package me.xdanez.roguelikeitems.enums;

import org.bukkit.Material;

public enum ItemType {
    ARMOR,
    WEAPON,
    TOOL;

    public static ItemType getItemType(Material material) {
        if (isArmor(material)) return ItemType.ARMOR;
        if (isWeapon(material)) return ItemType.WEAPON;
        if (isTool(material)) return ItemType.TOOL;
        return null;
    }

    public static boolean isArmor(Material material) {
        String materialStr = material.toString();
        return materialStr.endsWith("_HELMET")
                || materialStr.endsWith("_CHESTPLATE")
                || materialStr.endsWith("_LEGGINGS")
                || materialStr.endsWith("_BOOTS")
                || materialStr.endsWith("ELYTRA");
    }

    public static boolean isRanged(Material material) {
        return material.toString().endsWith("BOW");
    }

    public static boolean isWeapon(Material material) {
        String materialStr = material.toString();
        return materialStr.endsWith("_SWORD")
                || materialStr.endsWith("BOW")
                || material.equals(Material.TRIDENT)
                || material.equals(Material.MACE);
    }

    public static boolean isTool(Material material) {
        String materialStr = material.toString();
        return materialStr.endsWith("_PICKAXE")
                || materialStr.endsWith("_AXE")
                || materialStr.endsWith("_SHOVEL")
                || materialStr.endsWith("_HOE")
                || materialStr.endsWith("_ON_A_STICK")
                || material.equals(Material.FISHING_ROD)
                || material.equals(Material.SHEARS)
                || material.equals(Material.BRUSH)
                || material.equals(Material.FLINT_AND_STEEL);
    }

    public static boolean isShield(Material material) {
        return material.equals(Material.SHIELD);
    }

    public static boolean isModifiable(Material material) {
        return isTool(material) || isWeapon(material) || isArmor(material) || isShield(material);
    }
}
