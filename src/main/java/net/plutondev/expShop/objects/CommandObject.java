package net.plutondev.expShop.objects;

import org.bukkit.command.CommandSender;

public abstract class CommandObject {
    private final String name;
    private final String description;
    private final String[] aliases;
    private final String permission;
    private final String usage;

    protected CommandObject(String name, String description, String[] aliases, String permission, String usage) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.permission = permission;
        this.usage = usage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return usage;
    }

    public boolean hasPermission(org.bukkit.command.CommandSender sender) {
        return permission.isEmpty() || sender.hasPermission(permission);
    }

    public abstract void execute(CommandSender sender, String[] args);
}