package net.plutondev.expShop.message;

import net.plutondev.expShop.ExpShop;
import net.plutondev.expShop.objects.MessageObject;
import net.plutondev.expShop.objects.SoundObject;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class MessageManager {
    private final ExpShop plugin;

    private final Map<String, MessageObject> messagesMap = new HashMap<>();
    private final Map<String, SoundObject> soundObjects = new HashMap<>();

    public MessageManager(ExpShop plugin) {
        this.plugin = plugin;
        loadSounds();
        messagesList(); // Add fixed messages from the config
    }

    // Adds fixed message keys from the config
    public void messagesList() {
        messagesMap.clear();
        addMessage("no-permission");
        addMessage("invalid-page");
        addMessage("insufficient-exp");
        addMessage("item-purchased");
        addMessage("expshop-wrong-usage");
        addMessage("reload");
        addMessage("reload-fail");
        addMessage("expshop-no-permission");
        addMessage("expshop-open");
        addMessage("help");
    }

    public void sendMessage(Player player, String message) {
        MessageObject messageObject = messagesMap.get(message);
        for (String line : colorize(messageObject.getMessage())) player.sendMessage(line);

        playSound(player, messageObject.getSound().getSoundName());
    }

    public void addMessage(String key) {
        List<String> message = new ArrayList<>();
        if(!plugin.getConfig().isList("messages." + key + ".message")) {
            message.add(plugin.getConfig().getString("messages." + key + ".message"));
        }

        else {
            message.addAll(plugin.getConfig().getStringList("messages." + key + ".message"));
        }

        String sound = plugin.getConfig().getString("messages." + key + ".sound");

        MessageObject messageObject = new MessageObject(message, getSound(sound));
        messagesMap.put(key, messageObject);
    }

    // File: src/main/java/net/plutondev/expShop/message/MessageManager.java
    // Java
    public void loadSounds() {
        // Check if the configuration section exists
        if (plugin.getConfig().getConfigurationSection("sounds") == null) {
            plugin.getLogger().warning("Missing configuration section 'sounds'. No sounds were loaded.");
            return;
        }

    Objects.requireNonNull(plugin.getConfig().getConfigurationSection("sounds")).getKeys(false)
        .forEach(key -> {
            String soundValue = plugin.getConfig().getString("sounds." + key + ".name");
            // Log a warning if the sound is not defined in the config
            if (soundValue == null || soundValue.trim().isEmpty()) {
                plugin.getLogger().warning("Sound value for key " + key + " is null or empty. Skipping.");
                return;
            }
            float volume = (float) plugin.getConfig().getDouble("sounds." + key + ".volume", 1.0);
            float pitch = (float) plugin.getConfig().getDouble("sounds." + key + ".pitch", 1.0);

            Sound parsedSound = stringToSound(soundValue);
            SoundObject soundObject = new SoundObject(key, parsedSound, volume, pitch);
            // Use the actual sound value as key to match with messages config
            soundObjects.put(key, soundObject);
        });
    }

    public void playSound(Player player, String sound) {
        SoundObject soundObject = getSound(sound);
        if (soundObject != null) {
            player.playSound(player.getLocation(), soundObject.getSound(), soundObject.getVolume(), soundObject.getPitch());
        } else {
            plugin.getLogger().warning("Sound " + sound + " not found. Cannot play sound.");
        }
    }

    public SoundObject getSound(String soundName) {
        if (soundName == null || soundName.trim().isEmpty()) {
            plugin.getLogger().warning("Sound name is null or empty. Using default sound.");
            return new SoundObject("default", Sound.BLOCK_ANVIL_FALL, 1.0f, 1.0f);
        }

        if (!soundObjects.containsKey(soundName)) {
            plugin.getLogger().warning("Sound " + soundName + " not found in config. Using default sound.");
            plugin.getLogger().log(Level.WARNING, "Available sounds: " + soundObjects.keySet());
            return new SoundObject(soundName, Sound.BLOCK_ANVIL_FALL, 1.0f, 1.0f);
        }
        return soundObjects.get(soundName);
    }

    public Sound stringToSound(String soundName) {
        if (soundName == null) {
            plugin.getLogger().warning("Sound name " + soundName + " is null. Using default sound.");
            return Sound.BLOCK_ANVIL_FALL;
        }
        try {
            return Sound.valueOf(soundName);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Sound " + soundName + " cannot be parsed. Using default sound.");
            return Sound.BLOCK_ANVIL_FALL;
        }
    }

    public List<String> colorize(List<String> message) {
        List<String> coloredMessage = new ArrayList<>();

        for (String line : message) {
            coloredMessage.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return coloredMessage;
    }
}