package net.plutondev.expShop;

import net.plutondev.expShop.commands.CommandManager;
import net.plutondev.expShop.listener.MenuListener;
import net.plutondev.expShop.listener.PlayerJoin;
import net.plutondev.expShop.menu.MenuConfig;
import net.plutondev.expShop.menu.MenuManager;
import net.plutondev.expShop.message.MessageManager;
import net.plutondev.expShop.utils.ExperienceUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public final class ExpShop extends JavaPlugin {
    public MenuConfig menuConfig;
    public MenuManager menuManager;
    public MessageManager messageManager;
    public CommandManager commandManager;
    public ExperienceUtils experienceUtils;

    @Override
    public void onEnable() {
        int pluginID = 25487;

        Metrics metrics = null;

        if(getConfig().getBoolean("settings.bstats-metrics.enable"))
            metrics = new Metrics(this, pluginID);

        // Config setup
        menuConfig = new MenuConfig(this);
        menuConfig.loadConfig();
        getConfig().options().copyDefaults(true);

        saveConfig();
        saveDefaultConfig();

        // Initialize menu manager
        menuManager = new MenuManager(menuConfig, this);

        // Initialize message manager
        messageManager = new MessageManager(this);

        //Initialize experience utils
        experienceUtils = new ExperienceUtils();


        // Initialize command manager
        commandManager = new CommandManager(this);

        // Register events
        getServer().getPluginManager().registerEvents(new MenuListener(menuManager, this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);

        // Register command
        Objects.requireNonNull(getCommand("expshop")).setExecutor(commandManager);

        getLogger().info("PlutonEXP has been enabled!");
    }

    public void checkUpdates(Player player) {
        String apiUrl = "https://api.spigotmc.org/legacy/update.php?resource=124096";
        String latestVersion = null;
        HttpURLConnection connection = null;


        try {
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            latestVersion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (latestVersion == null || latestVersion.trim().isEmpty()) return;


        if(!getDescription().getVersion().equalsIgnoreCase(latestVersion)) {
            player.sendMessage(ChatColor.GOLD + "A new version of PlutonEXP is available: " + latestVersion);
            player.sendMessage(ChatColor.GOLD + "You are currently using version: " + getDescription().getVersion());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("PlutonEXP has been disabled!");
    }
}