package me.xdanez.roguelikeitems.commands.subcommands;

import me.xdanez.roguelikeitems.RogueLikeItems;
import me.xdanez.roguelikeitems.commands.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
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
        return "Show info about plugin";
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
        Configuration pluginYML = RogueLikeItems.getPluginYML();
        TextComponent separator = Component.text("--------------------")
                .color(TextColor.color(81, 81, 81));
        String name = pluginYML.getString("name");
        String version = pluginYML.getString("version");
        String author = pluginYML.getString("author");
        String source = pluginYML.getString("source");
        String description = pluginYML.getString("description");

        assert source != null;
        TextComponent msg = Component.text(sender instanceof Player ? "" : "\n")
                .append(separator)
                .append(defaultText("\n" + name))
                .append(defaultText(" by "))
                .append(specialText(author + "\n"))
                .append(defaultText(description + "\n"))
                .append(defaultText("Version: "))
                .append(specialText(version + "\n"))
                .append(defaultText("Source: "))
                .append(specialText(source + "\n").clickEvent(ClickEvent.openUrl(source)))
                .append(separator);

        sender.sendMessage(msg);
    }

    private Component defaultText(String text) {
        return Component.text(text).color(TextColor.color(114, 215, 0));
    }

    private Component specialText(String text) {
        return Component.text(text).color(TextColor.color(44, 152, 255));
    }
}
