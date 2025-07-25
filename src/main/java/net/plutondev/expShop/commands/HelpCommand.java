package net.plutondev.expShop.commands;

import net.plutondev.expShop.ExpShop;
import net.plutondev.expShop.objects.CommandObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends CommandObject {
    private final ExpShop plugin;

    public HelpCommand(ExpShop plugin) {
        super("help", "Displays help information", new String[]{ "h" }, "plutonexp.help", "/help");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String helpMessage = "Available commands: /expshop help, /expshop reload, /expshop open";
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.messageManager.sendMessage(player, "help");
        } else {
            sender.sendMessage(helpMessage);
        }
    }
}