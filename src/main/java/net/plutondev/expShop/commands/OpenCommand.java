// File: src/main/java/net/plutondev/expShop/commands/OpenCommand.java
package net.plutondev.expShop.commands;

import net.plutondev.expShop.ExpShop;
import net.plutondev.expShop.objects.CommandObject;
import org.bukkit.Bukkit;
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
        Player targetPlayer = null;

        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("open"))) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be used by a player.");
                return;
            }
            targetPlayer = (Player) sender;
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("open")) {
            if (!sender.hasPermission("expshop.admin.openother")) {
                if (sender instanceof Player player) {
                    plugin.messageManager.sendMessage(player, "no-permission");
                } else {
                    sender.sendMessage("You do not have permission to open the shop for others.");
                }
                return;
            }
            targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                sender.sendMessage("Target player is not online.");
                return;
            }
        }

        if (targetPlayer == null) {
            sender.sendMessage("Usage: /expshop open [player]");
            return;
        }

        plugin.messageManager.sendMessage(targetPlayer, "expshop-open");
        plugin.menuManager.createMenu(targetPlayer, 1);
        if (sender != targetPlayer) {
            sender.sendMessage("Opened experience shop for " + targetPlayer.getName());
        }
    }
}