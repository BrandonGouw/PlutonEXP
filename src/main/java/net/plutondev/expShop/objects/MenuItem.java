package net.plutondev.expShop.objects;

import net.plutondev.expShop.ExpShop;
import net.plutondev.expShop.enums.ExpType;
import net.plutondev.expShop.enums.ItemType;
import net.plutondev.expShop.enums.NavType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MenuItem {
    private final String itemName;
    private final List<String> lore;
    private final UUID id;
    private final String itemMaterial;
    private final List<String> commands;
    private final List<Integer> slots;
    private final int expCost;
    private final ExpType expType;
    private final NavType navType;
    private final ItemType type;

    public MenuItem(String itemName, List<String> lore, String itemType, UUID id,
                   String itemMaterial, List<String> commands, List<Integer> slots,
                   NavType navType, ItemType type, int expCost, ExpType expType) {

        this.itemName = itemName;
        this.lore = lore;
        this.id = id;
        this.itemMaterial = itemMaterial;
        this.commands = commands;
        this.slots = slots;
        this.navType = navType;
        this.type = type;
        this.expCost = expCost;
        this.expType = expType;
    }

    public String getItemName() {
        return itemName;
    }

    public List<String> getLore() {
        return lore;
    }

    public UUID getId() {
        return id;
    }

    public NavType getNavType() {
        return navType;
    }

    public String getItemMaterial() {
        return itemMaterial;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public ItemStack getItem(ExpShop plugin) {
        ItemStack item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(itemMaterial)), 1);
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) return item;

        itemMeta.setDisplayName(colorize(itemName));
        itemMeta.setLore(colorize(lore));

        // Store the menu item ID in the item's persistent data
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "menu_item_id");
        container.set(key, PersistentDataType.STRING, id.toString());

        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemType getType() {
        return type;
    }

    public int getExpCost() {
        return expCost;
    }

    public ExpType getExpType() {
        return expType;
    }

    private String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private List<String> colorize(List<String> list) {
        list.replaceAll(this::colorize);
        return list;
    }

}