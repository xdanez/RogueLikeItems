package me.xdanez.roguelikeitems.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

final public class NumberUtil {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.DOWN);
        return bd.doubleValue();
    }
}
