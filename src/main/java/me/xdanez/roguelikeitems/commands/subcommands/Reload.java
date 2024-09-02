package me.xdanez.roguelikeitems.commands.subcommands;

import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.commands.SubCommand;
import me.xdanez.roguelikeitems.enums.Config;
import me.xdanez.roguelikeitems.enums.ConfigState;
import me.xdanez.roguelikeitems.utils.CommandUtil;
import me.xdanez.roguelikeitems.utils.ConfigUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Reload extends SubCommand {
    @Override
    public String getName() {
        return "Reload";
    }

    @Override
    public String getSyntax() {
        return "/rli reload";
    }

    @Override
    public String getDescription() {
        return "Reload config";
    }

    @Override
    public String getPermission() {
        return "roguelikeitems.reload";
    }

    @Override
    public List<String> getTabCompletion(String[] args) {
        return List.of();
    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        boolean hasPermission = CommandUtil.checkPermission(sender, getPermission(), true);
        if (!hasPermission) return;

        RogueLikeItems.getPlugin(RogueLikeItems.class).reloadConfig();
        ConfigState validConfig = ConfigUtil.validateConfig();
        if (!(sender instanceof Player)) {
            switch (validConfig) {
                case SUCCESS:
                    RogueLikeItems.logger().info("Config reloaded!");
                    break;
                case WARNING:
                    RogueLikeItems.logger().warning("Config reloaded with warnings");
                    break;
                case ERROR:
                    RogueLikeItems.logger().severe("Config reloaded with errors");
                    break;
            }
            return;
        }

        Player player = (Player) sender;
        switch (validConfig) {
            case SUCCESS:
                player.sendMessage(Component.text("Config reloaded!").color(TextColor.color(0, 150, 0)));
                break;
            case WARNING:
                player.sendMessage(Component.text("Config reloaded with warnings").color(TextColor.color(147, 150, 17)));
                break;
            case ERROR:
                player.sendMessage(Component.text("Config reloaded with errors").color(TextColor.color(255, 0, 0)));
                break;
        }
    }
}
