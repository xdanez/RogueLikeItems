package me.xdanez.roguelikeitems.models;

import me.xdanez.roguelikeitems.enums.Config;

public class AmplifierChance {
    private int durability;
    private int damage;
    private int maxHealth;

    public AmplifierChance(int durability, int damage, int maxHealth) {
        this.durability = durability;
        this.damage = damage;
        this.maxHealth = maxHealth;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setMaxHealth(int maxHealth) {
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

    public void setAmplifierChance(Config config, int chance) {
        switch (config) {
            case AC_DAMAGE:
                setDamage(chance);
                break;
            case AC_DURABILITY:
                setDurability(chance);
                break;
            case AC_MAX_HEALTH:
                setMaxHealth(chance);
                break;
            default:
                throw new IllegalArgumentException("Invalid config");
        }
    }
}
