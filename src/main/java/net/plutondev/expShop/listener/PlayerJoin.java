package net.plutondev.expShop.listener;

import net.plutondev.expShop.ExpShop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final ExpShop plugin;

    public PlayerJoin(ExpShop plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        if(event.getPlayer().isOp()) {
            plugin.checkUpdates(event.getPlayer());
        }
    }

}
