package me.xdanez.roguelikeitems.commands.subcommands;

import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.commands.SubCommand;
import me.xdanez.roguelikeitems.enums.ConfigState;
import me.xdanez.roguelikeitems.utils.CommandUtil;
import me.xdanez.roguelikeitems.utils.ConfigUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
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
        List<ConfigState> validConfig = ConfigUtil.validateConfig();
        int amtWarning = Collections.frequency(validConfig, ConfigState.WARNING);
        int amtError = Collections.frequency(validConfig, ConfigState.ERROR);
        if (!(sender instanceof Player)) {
            if (validConfig.isEmpty() || validConfig.stream().allMatch(ConfigState.SUCCESS::equals)) {
                RogueLikeItems.logger().info("Config successfully reloaded");
                return;
            }
            if (amtError > 0) {
                RogueLikeItems.logger().severe("Config reloaded with " + amtError + " errors and " + amtWarning + " warnings!");
                return;
            }
            RogueLikeItems.logger().warning("Config reloaded with " + amtWarning + " warnings!");
            return;
        }

        Player player = (Player) sender;
        if (validConfig.isEmpty() || validConfig.stream().allMatch(ConfigState.SUCCESS::equals)) {
            player.sendMessage(Component.text("Config successfully reloaded!").color(TextColor.color(0x00ff00)));
            return;
        }
        player.sendMessage(
                Component.text("Config reloaded with " + (amtError > 0 ? amtError + " errors and " : "") + amtWarning + " warnings!")
                        .color(TextColor.color(amtError > 0 ? 0xff0000 : 0xffff00))
        );
    }
}
