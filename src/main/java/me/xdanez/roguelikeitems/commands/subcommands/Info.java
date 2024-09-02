package me.xdanez.roguelikeitems.commands.subcommands;

import me.xdanez.roguelikeitems.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Info extends SubCommand {
    @Override
    public String getName() {
        return "Info";
    }

    @Override
    public String getSyntax() {
        return "/rli " + getName().toLowerCase();
    }

    @Override
    public String getDescription() {
        return "Info about plugin";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public List<String> getTabCompletion(String[] args) {
        return List.of();
    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        sender.sendMessage("Very Info");
    }
}
