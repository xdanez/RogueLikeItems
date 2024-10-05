package me.xdanez.roguelikeitems;

import me.xdanez.roguelikeitems.commands.RLI;
import me.xdanez.roguelikeitems.enums.Config;
import me.xdanez.roguelikeitems.listeners.*;
import me.xdanez.roguelikeitems.utils.ConfigUtil;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Logger;

/*TODO: config
  - locals?
  - specific range for specific items/conditions?
*/
public final class RogueLikeItems extends JavaPlugin {

    static RogueLikeItems plugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        ConfigUtil.validateConfig();

        getServer().getPluginManager().registerEvents(new PlayerCraftListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDamagesItemListener(), this);
        getServer().getPluginManager().registerEvents(new DamageDealtListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPrepareSmithingTableListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerShootsBowListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPrepareCraftListener(), this);
        getServer().getPluginManager().registerEvents(new LootGenerateListener(), this);
        getServer().getPluginManager().registerEvents(new MobDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMendingListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPrepareAnvilListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerAcquireTradeListener(), this);
        getServer().getPluginManager().registerEvents(new ItemInItemFrameSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerArmorChangeListener(), this);

        Objects.requireNonNull(getCommand("rli")).setExecutor(new RLI());
    }

    @Override
    public void onDisable() {
    }

    public static Logger logger() {
        return plugin.getLogger();
    }

    public static RogueLikeItems plugin() {
        return getPlugin(RogueLikeItems.class);
    }

    public static Configuration config() {
        return plugin.getConfig();
    }

    public static Configuration defaultConfig() {
        return plugin().getConfig().getDefaults();
    }

    public static Object getConfigVal(Config config) {
        return plugin.getConfig().get(config.getVal());
    }

    public static Configuration getPluginYML() {
        InputStream in = RogueLikeItems.class.getResourceAsStream("/plugin.yml");
        assert in != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return YamlConfiguration.loadConfiguration(reader);
    }
}
