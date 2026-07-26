package net.plutondev.expShop.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.plutondev.expShop.ExpShop;
import net.plutondev.expShop.objects.CommandObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor, TabCompleter {
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
            CommandObject commandObj = getCommand("open");

            if(!commandObj.hasPermission(commandSender)) {
                if (commandSender instanceof Player player) {
                    plugin.messageManager.sendMessage(player, "no-permission");
                } else {
                    commandSender.sendMessage("You do not have permission to do this.");
                }
                return true;
            }

            executeCommand(commandSender, "open", strings);
            return true;
        }

        String commandName = strings[0].toLowerCase();
        CommandObject commandObj = getCommand(commandName);
        if(commandObj == null) {
            executeCommand(commandSender, "help", strings);
            return true;
        }

        if(!commandObj.hasPermission(commandSender)) {
            if (commandSender instanceof Player player) {
                plugin.messageManager.sendMessage(player, "no-permission");
            } else {
                commandSender.sendMessage("You do not have permission to do this.");
            }
            return true;
        }

        executeCommand(commandSender, commandObj.getName(), strings);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>();
            subCommands.add("open");
            subCommands.add("reload");
            subCommands.add("help");
            
            String partial = args[0].toLowerCase();
            for (String sub : subCommands) {
                if (sub.startsWith(partial)) {
                    completions.add(sub);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("open")) {
            String partial = args[1].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(partial)) {
                    completions.add(p.getName());
                }
            }
        }
        return completions;
    }
}