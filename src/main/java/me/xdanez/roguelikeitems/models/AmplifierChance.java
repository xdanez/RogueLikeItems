package me.xdanez.roguelikeitems.models;

public class AmplifierChance {
    private final int durability;
    private final int damage;
    private final int maxHealth;

    public AmplifierChance(int durability, int damage, int maxHealth) {
        this.durability = durability;
        this.damage = damage;
        this.maxHealth = maxHealth;
    }

    public int getDurability() {
        return durability;
    }

    public int getDamage() {
        return damage;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
