package me.xdanez.roguelikeitems.enums;

import me.xdanez.roguelikeitems.models.ConfigData;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlotGroup;

public final class ItemType {
    public static boolean isArmor(Material material) {
        String materialStr = material.toString();
        return materialStr.endsWith("_HELMET")
                || materialStr.endsWith("_CHESTPLATE")
                || materialStr.endsWith("_LEGGINGS")
                || materialStr.endsWith("_BOOTS")
                || materialStr.endsWith("ELYTRA");
    }

    public static boolean isPetArmor(Material material) {
        return material.toString().endsWith("_HORSE_ARMOR") || material.equals(Material.WOLF_ARMOR);
    }

    public static boolean isRanged(Material material) {
        return material.toString().endsWith("BOW");
    }

    public static boolean isWeapon(Material material) {
        String materialStr = material.toString();
        return materialStr.endsWith("_SWORD")
                || isRanged(material)
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

    public static boolean isWeaponOrTool(Material material) {
        return isWeapon(material) || isTool(material);
    }

    public static boolean isArmorOrShield(Material material) {
        return isArmor(material) || isShield(material);
    }

    public static boolean isModifiable(Material material) {
        return isTool(material) || isWeapon(material) || isArmor(material) || isShield(material) || isPetArmor(material);
    }

    public static EquipmentSlotGroup getGroup(Material material) {
        if (isRanged(material)) {
            if (ConfigData.getConfigData().useBowMainHandAttack()) return EquipmentSlotGroup.MAINHAND;
            else return EquipmentSlotGroup.HAND;
        }
        if (isShield(material)) return EquipmentSlotGroup.HAND;
        if (isWeaponOrTool(material)) return EquipmentSlotGroup.MAINHAND;
        if (isPetArmor(material)) return EquipmentSlotGroup.BODY;

        String materialStr = material.toString();
        if (materialStr.endsWith("_HELMET")) return EquipmentSlotGroup.HEAD;
        if (materialStr.endsWith("_CHESTPLATE")) return EquipmentSlotGroup.CHEST;
        if (materialStr.endsWith("_LEGGINGS")) return EquipmentSlotGroup.LEGS;
        if (materialStr.endsWith("_BOOTS")) return EquipmentSlotGroup.FEET;

        return EquipmentSlotGroup.ARMOR;
    }
}
