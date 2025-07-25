package net.plutondev.expShop.menu;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class MenuConfig {
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public MenuConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    /**
     * Loads the shop.yml configuration
     */
    public void loadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "shop.yml");
        }

        if (!configFile.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource("shop.yml", true); // Use built-in method to copy default config
        }
        
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Saves the configuration to file
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save shop.yml", e);
        }
    }

    /**
     * Reloads the configuration from file
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Gets the formatted menu title
     * @return Formatted menu title
     */
    public String getMenuTitle() {
        return ChatColor.translateAlternateColorCodes('&', 
               config.getString("menu.title", "&8Experience Shop"));
    }

    /**
     * Gets the raw configuration
     * @return FileConfiguration object
     */
    public FileConfiguration getConfig() {
        return config;
    }
}
