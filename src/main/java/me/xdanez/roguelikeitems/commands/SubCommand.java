package me.xdanez.roguelikeitems.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SubCommand {
    public abstract String getName();
    public abstract String getSyntax();
    public abstract String getDescription();
    public abstract @Nullable String getPermission();
    public abstract List<String> getTabCompletion(String[] args);
    public abstract void execute(@NotNull CommandSender sender, String[] args);
}
