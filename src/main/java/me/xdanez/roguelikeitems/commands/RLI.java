package me.xdanez.roguelikeitems.commands;

import me.xdanez.roguelikeitems.commands.subcommands.Info;
import me.xdanez.roguelikeitems.commands.subcommands.Reload;
import me.xdanez.roguelikeitems.commands.subcommands.Give;
import me.xdanez.roguelikeitems.utils.CommandUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RLI implements CommandExecutor, TabCompleter {

    private final SubCommand[] subCommands = {
            new Reload(),
            new Give(),
            new Info()
    };

    private final String SEPARATOR = ">------------------------------<";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command,
                             @NotNull String s,
                             @NotNull String[] args) {
        if (args.length == 0) {
            sendHelpMessage(commandSender);
            return false;
        }
        String subCommandName = args[0];
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(subCommandName)) {
                subCommand.execute(commandSender, getArgsWithoutCommandName(args));
                return false;
            }
        }
        sendHelpMessage(commandSender);
        return false;
    }

    private void sendHelpMessage(CommandSender sender) {
        TextComponent helpMessage = Component.text((sender instanceof Player ? "" : "\n") + SEPARATOR + "\n"
                + getCommands()
                + SEPARATOR);
        sender.sendMessage(helpMessage);
    }

    private String getCommands() {
        StringBuilder commands = new StringBuilder();
        for (SubCommand subCommand : subCommands) {
            commands.append(subCommand.getSyntax()).append(": ").append(subCommand.getDescription());
            commands.append("\n");
        }
        return commands.toString();
    }

    private List<String> getOptions(CommandSender sender) {
        List<String> options = new ArrayList<>();
        for (SubCommand subCommand : subCommands) {
            if (CommandUtil.checkPermission(sender, subCommand.getPermission(), false)) {
                options.add(subCommand.getName().toLowerCase());
            }
        }
        return options;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
                                                @NotNull Command command,
                                                @NotNull String s,
                                                @NotNull String[] args) {
        if (args.length >= 2) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().equalsIgnoreCase(args[0])) {
                    if (!CommandUtil.checkPermission(commandSender, subCommand.getPermission(), false)) {
                        return List.of();
                    }
                    return subCommand.getTabCompletion(getArgsWithoutCommandName(args));
                }
            }
        }
        return CommandUtil.getFilteredOptions(getOptions(commandSender), args[args.length-1]);
    }

    private String[] getArgsWithoutCommandName(String[] args) {
        return args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[]{};
    }
}
