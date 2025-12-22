package me.xdanez.roguelikeitems;

import me.xdanez.roguelikeitems.commands.RLI;
import me.xdanez.roguelikeitems.enums.ConfigType;
import me.xdanez.roguelikeitems.listeners.*;
import me.xdanez.roguelikeitems.utils.ConfigUtil;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class RogueLikeItems extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        ConfigUtil.validateConfig();

        getServer().getPluginManager().registerEvents(new PlayerCraftListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPrepareSmithingTableListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerShootsBowListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPrepareCraftListener(), this);
        getServer().getPluginManager().registerEvents(new GeneralEventListener(), this);

        getCommand("rli").setExecutor(new RLI());
    }

    public static RogueLikeItems plugin() {
        return getPlugin(RogueLikeItems.class);
    }

    public static Logger logger() {
        return plugin().getLogger();
    }

    public static Configuration config() {
        return plugin().getConfig();
    }

    public static Object getConfigVal(ConfigType config) {
        return plugin().getConfig().get(config.toString());
    }
}
