package net.plutondev.expShop.commands;

import java.util.HashMap;
import java.util.Map;

import net.plutondev.expShop.ExpShop;
import net.plutondev.expShop.objects.CommandObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
    private final Map<String, CommandObject> commandMap = new HashMap<>();
    private final ExpShop plugin;

    public CommandManager(ExpShop plugin) {
        this.plugin = plugin;
        registerCommand(new HelpCommand(plugin));
        registerCommand(new ReloadCommand(plugin));
        registerCommand(new OpenCommand(plugin));
    }

    public void registerCommand(CommandObject command) {
        // Register with command name
        commandMap.put(command.getName().toLowerCase(), command);

        // Register using aliases
        for (String alias : command.getAliases()) {
            commandMap.put(alias.toLowerCase(), command);
        }
    }

    public CommandObject getCommand(String name) {
        return commandMap.get(name.toLowerCase());
    }

    public void executeCommand(CommandSender sender, String name, String[] args) {
        CommandObject command = getCommand(name);
        if (command != null) {
            if (command.hasPermission(sender)) {
                command.execute(sender, args);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 0) {
            executeCommand(commandSender, "open", strings);
            return false;
        }

        String commandName = strings[0].toLowerCase();
        CommandObject commandObj = getCommand(commandName);
        if(commandObj == null) {
            executeCommand(commandSender, "help", strings);
            return false;
        }

        if(!commandObj.hasPermission(commandSender)) {
            plugin.messageManager.sendMessage((Player) commandSender, "no-permission");
        }

        executeCommand(commandSender, commandObj.getName(), strings);
        return true;
    }
}