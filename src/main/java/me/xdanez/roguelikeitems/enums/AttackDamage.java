package me.xdanez.roguelikeitems.enums;

import org.bukkit.Material;

// Is there a better solution to this?
// VERSION: 1.21
public enum AttackDamage {
    WOODEN_SWORD(4.0),
    WOODEN_AXE(7.0),
    WOODEN_SHOVEL(2.5),
    WOODEN_PICKAXE(1.2),
    WOODEN_HOE(1),
    STONE_SWORD(5.0),
    STONE_AXE(9.0),
    STONE_SHOVEL(3.5),
    STONE_PICKAXE(3),
    STONE_HOE(1),
    GOLDEN_SWORD(4.0),
    GOLDEN_AXE(7.0),
    GOLDEN_SHOVEL(2.5),
    GOLDEN_PICKAXE(2),
    GOLDEN_HOE(1),
    IRON_SWORD(6.0),
    IRON_AXE(9),
    IRON_SHOVEL(4.5),
    IRON_PICKAXE(4),
    IRON_HOE(1),
    DIAMOND_SWORD(7.0),
    DIAMOND_AXE(9.0),
    DIAMOND_SHOVEL(5.5),
    DIAMOND_PICKAXE(5),
    DIAMOND_HOE(1),
    NETHERITE_SWORD(8.0),
    NETHERITE_AXE(10.0),
    NETHERITE_SHOVEL(6.5),
    NETHERITE_PICKAXE(6),
    NETHERITE_HOE(1),
    MACE(6);

    private final double dmg;

    AttackDamage(double dmgVal) {
        this.dmg = dmgVal;
    }

    public double getDmg() {
        return dmg;
    }

    public static double getDamageFromMaterial(Material material) {
        switch (material) {
            case WOODEN_SWORD: return WOODEN_SWORD.dmg;
            case WOODEN_AXE: return WOODEN_AXE.dmg;
            case WOODEN_SHOVEL: return WOODEN_SHOVEL.dmg;
            case WOODEN_PICKAXE: return WOODEN_PICKAXE.dmg;
            case WOODEN_HOE: return WOODEN_HOE.dmg;
            case STONE_SWORD: return STONE_SWORD.dmg;
            case STONE_AXE: return STONE_AXE.dmg;
            case STONE_SHOVEL: return STONE_SHOVEL.dmg;
            case STONE_PICKAXE: return STONE_PICKAXE.dmg;
            case STONE_HOE: return STONE_HOE.dmg;
            case GOLDEN_SWORD: return GOLDEN_SWORD.dmg;
            case GOLDEN_AXE: return GOLDEN_AXE.dmg;
            case GOLDEN_SHOVEL: return GOLDEN_SHOVEL.dmg;
            case GOLDEN_PICKAXE: return GOLDEN_PICKAXE.dmg;
            case GOLDEN_HOE: return GOLDEN_HOE.dmg;
            case IRON_SWORD: return IRON_SWORD.dmg;
            case IRON_AXE: return IRON_AXE.dmg;
            case IRON_PICKAXE: return IRON_PICKAXE.dmg;
            case IRON_SHOVEL: return IRON_SHOVEL.dmg;
            case IRON_HOE: return IRON_HOE.dmg;
            case DIAMOND_SWORD: return DIAMOND_SWORD.dmg;
            case DIAMOND_AXE: return DIAMOND_AXE.dmg;
            case DIAMOND_SHOVEL: return DIAMOND_SHOVEL.dmg;
            case DIAMOND_PICKAXE: return DIAMOND_PICKAXE.dmg;
            case DIAMOND_HOE: return DIAMOND_HOE.dmg;
            case NETHERITE_SWORD: return NETHERITE_SWORD.dmg;
            case NETHERITE_AXE: return NETHERITE_AXE.dmg;
            case NETHERITE_SHOVEL: return NETHERITE_SHOVEL.dmg;
            case NETHERITE_PICKAXE: return NETHERITE_PICKAXE.dmg;
            case NETHERITE_HOE: return NETHERITE_HOE.dmg;
            case MACE: return MACE.dmg;
            default: return 0.0;
        }
    }
}
