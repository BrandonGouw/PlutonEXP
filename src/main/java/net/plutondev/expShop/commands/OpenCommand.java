// File: src/main/java/net/plutondev/expShop/commands/OpenCommand.java
package net.plutondev.expShop.commands;

import net.plutondev.expShop.ExpShop;
import net.plutondev.expShop.objects.CommandObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends CommandObject {
    private final ExpShop plugin;

    public OpenCommand(ExpShop plugin) {
        super("open", "Opens the experience shop", new String[]{}, "plutonexp.open", "/open");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by a player.");
            return;
        }

        plugin.messageManager.sendMessage(player, "expshop-open");
        plugin.menuManager.createMenu(player, 1);
    }
}