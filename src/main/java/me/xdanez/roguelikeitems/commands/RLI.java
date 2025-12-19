package me.xdanez.roguelikeitems.commands;

import me.xdanez.roguelikeitems.commands.subcommands.*;
import me.xdanez.roguelikeitems.utils.CommandUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
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

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender,
                             @NotNull Command command,
                             @NotNull String s,
                             String[] args) {
        if (args.length == 0) {
            sendHelpMessage(commandSender);
            return false;
        }

        String subCommandName = args[0];
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(subCommandName)) {
                if (!CommandUtil.checkPermission(commandSender, subCommand.getPermission(), true))
                    return false;
                subCommand.execute(commandSender, getArgsWithoutCommandName(args));
                return false;
            }
        }
        sendHelpMessage(commandSender);
        return false;
    }

    private void sendHelpMessage(CommandSender sender) {
        Component helpMessage = Component.text((sender instanceof Player ? "" : "\n") + ">---RogueLikeItems Commands---<" + "\n")
                .append(getCommands(sender)).append(Component.text(">-----------------------------<"));
        sender.sendMessage(helpMessage);
    }

    private Component getCommands(CommandSender sender) {
        Component commands = Component.empty();
        for (SubCommand subCommand : subCommands) {
            if (!CommandUtil.checkPermission(sender, subCommand.getPermission(), false)) continue;
            commands = commands.append(commandLine(subCommand));
        }
        return commands;
    }

    private Component commandLine(SubCommand command) {
        return Component.empty()
                .append(Component.text(command.getSyntax())
                        .color(TextColor.color(0xFFD700))
                        .clickEvent(ClickEvent.suggestCommand("/rli " + command.getName().toLowerCase())))
                .append(Component.text(": " + command.getDescription() + "\n")
                        .color(TextColor.color(0x888888)));
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
                                                String[] args) {
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
        return CommandUtil.getFilteredOptions(getOptions(commandSender), args[args.length - 1]);
    }

    private String[] getArgsWithoutCommandName(String[] args) {
        return args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[]{};
    }
}
