package me.xdanez.roguelikeitems.models;

import java.io.Serializable;

public class Durability implements Serializable {
    private int amplifiedMaxDurability;
    private double amplifier;
    private int currentDurability;

    public Durability(int amplifiedMaxDurability, double amplifier, int currentDurability) {
        this.amplifiedMaxDurability = amplifiedMaxDurability;
        this.amplifier = amplifier;
        this.currentDurability = currentDurability;
    }

    public int getAmplifiedMaxDurability() {
        return amplifiedMaxDurability;
    }

    public void setAmplifiedMaxDurability(int amplifiedMaxDurability) {
        this.amplifiedMaxDurability = amplifiedMaxDurability;
    }

    public double getAmplifier() {
        return amplifier;
    }

    public void setAmplifier(double amplifier) {
        this.amplifier = amplifier;
    }

    public int getCurrentDurability() {
        return currentDurability;
    }

    public void setCurrentDurability(int currentDurability) {
        this.currentDurability = currentDurability;
    }
}
