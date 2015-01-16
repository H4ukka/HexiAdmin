package h4;

/**
 * Created by H4 on 16.1.2015.
 */
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class PluginConfiguration {

    private JavaPlugin plugin;

    private File configFile;
    private FileConfiguration config;

    PluginConfiguration (JavaPlugin plugin) {
        this.plugin = plugin;

        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = new YamlConfiguration();

        this.loadConfiguration();
    }

    public void loadConfiguration () {
        try {
            plugin.getLogger().info("Loading configuration file...");
            config.load(configFile);
        } catch (InvalidConfigurationException | IOException e1) {
            plugin.getLogger().warning("Error loading configuration file.\n" + e1.getMessage());
        }
    }

    public int getInt (String path) {
        return config.getInt(path);
    }

    public String getString (String path) {
        return config.getString(path);
    }

    public boolean getBoolean (String path) {
        return config.getBoolean(path);
    }

    public List getList (String path) {
        return config.getList(path);
    }

    public Set<String> getStringSet (String path, boolean depth) {
        return config.getConfigurationSection(path).getKeys(depth);
    }
}
