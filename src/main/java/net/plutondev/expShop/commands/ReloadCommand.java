package net.plutondev.expShop.commands;

import net.plutondev.expShop.ExpShop;
import net.plutondev.expShop.objects.CommandObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends CommandObject {
    private final ExpShop plugin;

    public ReloadCommand(ExpShop plugin) {
        super("reload", "Reloads the plugin configuration", new String[]{}, "plutonexp.reload", "/reload");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            try {
                plugin.reloadConfig();
                plugin.menuConfig.reloadConfig();

                plugin.menuManager.updateMenu();
                plugin.messageManager.messagesList();
                plugin.messageManager.sendMessage(player, "reload");
            } catch (Exception e) {
                plugin.messageManager.sendMessage(player, "reload-fail");
            }
        } else {
            sender.sendMessage("Reloaded the Plugin Configuration");
        }
    }
}