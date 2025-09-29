package me.xdanez.roguelikeitems.commands.subcommands;

import me.xdanez.roguelikeitems.commands.SubCommand;
import me.xdanez.roguelikeitems.enums.ItemType;
import me.xdanez.roguelikeitems.utils.AmplifierUtil;
import me.xdanez.roguelikeitems.utils.CommandUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// TODO: modifiers
public class Give extends SubCommand {
    @Override
    public String getName() {
        return "Give";
    }

    @Override
    public String getSyntax() {
        return "/rli " + getName().toLowerCase() + " <Player> <Item>";
    }

    @Override
    public String getDescription() {
        return "Give an item with random modifiers to a player";
    }

    @Override
    public String getPermission() {
        return "roguelikeitems.give";
    }

    @Override
    public List<String> getTabCompletion(String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            for (Player player : players) {
                suggestions.add(player.getName());
            }
            suggestions.add("@p");
            suggestions.add("@a");
            suggestions.add("@r");
        }

        if (args.length == 2) {
            suggestions.add("<Item>");
        }

        return CommandUtil.getFilteredOptions(suggestions, args[args.length-1]);

    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        boolean hasPermission = CommandUtil.checkPermission(sender, getPermission(), true);
        if (!hasPermission) return;

        if (args.length != 2) {
            sender.sendMessage(getSyntax());
            return;
        }

        // Check players
        List<Player> playersToGive = new ArrayList<>(List.of());
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
        String providedPlayerName = args[0];
        switch (providedPlayerName) {
            case "@P":
            case "@p": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("Player name needed")
                            .color(TextColor.color(255, 0, 0)));
                    return;
                }
                playersToGive.add((Player) sender);
                break;
            }
            case "@A":
            case "@a": {
                playersToGive = playerList;
                break;
            }
            case "@R":
            case "@r": {
                int randomIndex = ThreadLocalRandom.current().nextInt(0, playerList.size());
                playersToGive.add(playerList.get(randomIndex));
                break;
            }
            default: {
                Player player = Bukkit.getPlayerExact(providedPlayerName);
                if (player == null) {
                    sender.sendMessage(Component.text("Player not found").color(TextColor.color(255, 0, 0)));
                    return;
                }
                playersToGive.add(player);
                break;
            }
        }

        // Check material
        Material material;
        String providedMaterial = args[1];
        try {
            material = Material.valueOf(providedMaterial.toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(
                    Component.text(providedMaterial + " is no valid item").color(TextColor.color(255, 0, 0))
            );
            return;
        }
        if (!ItemType.isModifiable(material)) {
            sender.sendMessage(
                    Component.text(material + " can not be modified").color(TextColor.color(255, 0, 0))
            );
            return;
        }
        ItemStack item = new ItemStack(material);

        AmplifierUtil.setAmplifiers(item);

        playersToGive.forEach(player -> player.getInventory().addItem(item));
        String name = playersToGive.size() > 1 ? String.valueOf(playersToGive.size()) : playersToGive.get(0).getName();
        sender.sendMessage(
                Component.text("Gave ")
                        .append(item.displayName().hoverEvent(item))
                        .append(Component.text(" to "))
                        .append(Component.text(name)
                                .hoverEvent(playersToGive.size() == 1 ? playersToGive.get(0) : null))
        );
    }
}
