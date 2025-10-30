package me.xdanez.roguelikeitems.models;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

import java.util.List;

public class CustomAttributeModifier {
    private final Attribute attribute;
    private final boolean inPercent;
    private final float from;
    private final float to;
    private final int chance;
    private final List<Material> ignoreItems;
    private final boolean toolsAndWeapons;
    private final boolean armorAndShield;
    private final boolean useOnlyNaturalNumbers;

    public CustomAttributeModifier(
            Attribute attribute,
            boolean inPercent,
            List<Float> range,
            int chance,
            List<Material> ignoreItems,
            boolean toolsAndWeapons,
            boolean armorAndShield,
            boolean useOnlyNaturalNumbers
    ) {
        this.attribute = attribute;
        this.inPercent = inPercent;
        this.from = range.getFirst();
        this.to = range.getLast();
        this.chance = chance;
        this.ignoreItems = ignoreItems;
        this.toolsAndWeapons = toolsAndWeapons;
        this.armorAndShield = armorAndShield;
        this.useOnlyNaturalNumbers = useOnlyNaturalNumbers;
    }

    public Attribute attribute() {
        return attribute;
    }

    public boolean inPercent() {
        return inPercent;
    }

    public float from() {
        return from;
    }

    public float to() {
        return to;
    }

    public int chance() {
        return chance;
    }

    public List<Material> ignoreItemsList() {
        return ignoreItems;
    }

    public boolean useToolsAndWeapons() {
        return toolsAndWeapons;
    }

    public boolean useArmorAndShield() {
        return armorAndShield;
    }

    public boolean useOnlyNaturalNumbers() {
        return useOnlyNaturalNumbers;
    }
}
