package me.xdanez.roguelikeitems.commands.subcommands;

import it.unimi.dsi.fastutil.Pair;
import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.commands.SubCommand;
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
        Pair<Integer, Integer> validConfig = ConfigUtil.validateConfig();
        int amtWarning = validConfig.left();
        int amtError = validConfig.right();
        String msg = msg(amtError, amtWarning);
        if (!(sender instanceof Player)) {
            if (amtWarning == 0 && amtError == 0) {
                RogueLikeItems.logger().info(msg);
                return;
            }
            if (amtError > 0) {
                RogueLikeItems.logger().severe(msg);
                return;
            }
            RogueLikeItems.logger().warning(msg);
            return;
        }

        Player player = (Player) sender;
        if (amtWarning == 0 && amtError == 0) {
            player.sendMessage(Component.text(msg));
            return;
        }
        player.sendMessage(Component.text(msg).color(TextColor.color(amtError > 0 ? 0xff0000 : 0xffff00)));
    }

    private String msg(int amtError, int amtWarning) {
        if (amtWarning == 0 && amtError == 0) {
            return "Config successfully reloaded";
        } else {
            String msg = "Config reloaded with ";
            if (amtError > 0) {
                msg += amtError;

                if (amtError == 1) msg += " error";
                else msg += " errors";

                if (amtWarning > 0) {
                    msg += " and " + amtWarning;

                    if (amtWarning == 1) msg += " warning";
                    else msg += " warnings";
                }
                msg += "!";
                return msg;
            }
            msg += amtWarning;

            if (amtWarning == 1) msg += " warning";
            else msg += " warnings";

            msg += "!";
            return msg;
        }
    }
}
