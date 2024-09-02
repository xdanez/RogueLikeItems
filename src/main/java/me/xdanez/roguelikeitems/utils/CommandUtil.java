package me.xdanez.roguelikeitems.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

final public class CommandUtil {
    public static boolean checkPermission(CommandSender sender, String permission, boolean sendMessage) {
        if (permission == null) return true;
        if (!sender.hasPermission(permission)) {
            if (sendMessage) {
                sender.sendMessage(Component.text("You do not have permission to run this command")
                        .color(TextColor.color(255, 0, 0)));
            }
            return false;
        }
        return true;
    }

    public static List<String> getFilteredOptions(List<String> options, String search) {
        if (search.isEmpty()) {
            return options;
        }
        List<String> filteredOptions = new ArrayList<>();
        for (String option : options) {
            if (StringUtils.containsIgnoreCase(option, search)) {
                filteredOptions.add(option);
            }
        }
        return filteredOptions;
    }
}