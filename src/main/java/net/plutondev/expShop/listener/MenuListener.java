package net.plutondev.expShop.listener;

import net.plutondev.expShop.ExpShop;
import net.plutondev.expShop.enums.ExpType;
import net.plutondev.expShop.enums.ItemType;
import net.plutondev.expShop.enums.NavType;
import net.plutondev.expShop.menu.MenuManager;
import net.plutondev.expShop.objects.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Listener class responsible for handling menu interactions in the ExpShop plugin.
 * Processes player interactions with shop menus, including item purchases and menu navigation.
 */
public class MenuListener implements Listener {
    private final MenuManager menuManager;
    private final ExpShop plugin;

    /**
     * Constructs a new MenuListener instance.
     *
     * @param menuManager The manager handling menu creation and tracking
     * @param plugin The main plugin instance
     */
    public MenuListener(MenuManager menuManager, ExpShop plugin) {
        this.menuManager = menuManager;
        this.plugin = plugin;
    }

    /**
     * Handles inventory click events within shop menus.
     * Prevents default click behavior and processes menu interactions.
     *
     * @param event The inventory click event
     */
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!menuManager.getOpenInventories().contains(event.getClickedInventory())) return;

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem != null) {
            itemClickHandler((Player) event.getWhoClicked(), currentItem, event.getClickedInventory());
        }
        event.setCancelled(true);
    }

    /**
     * Handles inventory close events for shop menus.
     * Removes closed inventories from tracking.
     *
     * @param event The inventory close event
     */
    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        if (!menuManager.getOpenInventories().contains(event.getInventory())) return;
        menuManager.removeInventory(event.getInventory());
    }

    /**
     * Processes item clicks within shop menus.
     * Handles both navigation items and purchasable items that cost experience points.
     *
     * @param player The player who clicked the item
     * @param currentItem The clicked item
     * @param inventory The inventory containing the item
     */
    private void itemClickHandler(Player player, ItemStack currentItem, Inventory inventory) {
        MenuItem clickedMenuItem = menuManager.getMenuItem(currentItem);
        if (clickedMenuItem == null) return;

        int currentPage = menuManager.getCurrentPage(inventory);

        // Handle navigation items
        if (clickedMenuItem.getType() == ItemType.NAVIGATION) {
            handleNavigationItem(player, clickedMenuItem, inventory, currentPage);
            return;
        }

        if (clickedMenuItem.getType() != ItemType.ITEM) return;

        int expCost = clickedMenuItem.getExpCost();
        ExpType expType = clickedMenuItem.getExpType();

        // Check if player has enough experience
        if (!plugin.experienceUtils.hasEnoughExp(player, expCost, expType)) {
            plugin.messageManager.sendMessage(player, "insufficient-exp");
            return;
        }

        // Deduct experience before executing commands
        plugin.experienceUtils.deductExperience(player, expCost, expType);

        // Execute commands and send purchase message
        executeCommands(player, clickedMenuItem.getCommands());
        plugin.messageManager.sendMessage(player, "item-purchased");

        // Refresh the menu
//        menuManager.closeInventory(player, inventory);
//        menuManager.createMenu(player, currentPage);
        menuManager.refreshMenu(player, inventory, currentPage);
    }

    /**
     * Processes navigation item clicks.
     * Handles page navigation, menu closing, and menu reloading.
     * Note: NavType.EXP is defined but not currently handled.
     *
     * @param player The player who clicked the navigation item
     * @param navItem The navigation menu item
     * @param inventory The current inventory
     * @param currentPage The current page number
     */
    private void handleNavigationItem(Player player, MenuItem navItem, Inventory inventory, int currentPage) {
        NavType navType = navItem.getNavType();

        switch (navType) {
            case NEXT:
                menuManager.closeInventory(player, inventory);
                if (currentPage < menuManager.menuPageCount) {
                    menuManager.createMenu(player, currentPage + 1);
                }
                break;

            case PREVIOUS:
                if (currentPage <= 1) return;
                menuManager.closeInventory(player, inventory);
                menuManager.createMenu(player, currentPage - 1);
                break;

            case CLOSE:
                menuManager.closeInventory(player, inventory);
                break;

            case RELOAD:
                menuManager.closeInventory(player, inventory);
                menuManager.createMenu(player, currentPage);
                break;
        }
    }

    /**
     * Executes commands as the console on behalf of a player.
     * Replaces %player% placeholder with the player's name.
     *
     * @param player The player for whom commands are executed
     * @param commands List of commands to execute
     */
    private void executeCommands(Player player, List<String> commands) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for (String command : commands) {
            command = command.replace("%player%", player.getName());
            Bukkit.dispatchCommand(console, command);
        }
    }
}