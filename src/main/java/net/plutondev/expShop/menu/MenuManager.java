package net.plutondev.expShop.menu;

import net.plutondev.expShop.ExpShop;
import net.plutondev.expShop.enums.ExpType;
import net.plutondev.expShop.enums.ItemType;
import net.plutondev.expShop.enums.NavType;
import net.plutondev.expShop.objects.*;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class MenuManager {
    private final MenuConfig menuConfig;
    private final ExpShop plugin;
    private final Map<Inventory, Integer> openInventories = new HashMap<>();
    private final List<MenuPage> menuPages = new ArrayList<>();
    private final List<MenuItem> menuItems = new ArrayList<>();
    private final Map<UUID, MenuItem> menuItemsMap = new HashMap<>();

    private String menuName;
    private int menuSize;
    public int menuPageCount;

    public MenuManager(MenuConfig menuConfig, ExpShop plugin) {
        this.menuConfig = menuConfig;
        this.plugin = plugin;
        loadMenu();
    }

    public void updateMenu() {
        menuPages.clear();
        menuItems.clear();
        menuItemsMap.clear();

        loadMenu();
    }

    public void refreshMenu(Player player, Inventory inventory, int menuPage) {
         inventory.clear();

        if (addItemsToMenu(player, menuPage, inventory)) return;
    }

    public void createMenu(Player player, int menuPage) {
        if (menuPage > menuPageCount || menuPage <= 0) {
            plugin.messageManager.sendMessage(player, "invalid-page");
            return;
        }

        Inventory inventory = player.getServer().createInventory(player, menuSize, menuName);
        if (addItemsToMenu(player, menuPage, inventory)) return;

        player.openInventory(inventory);
        addInventory(menuPage, inventory);
    }

    private boolean addItemsToMenu(Player player, int menuPage, Inventory inventory) {
        MenuPage menuPageObject = findMenuPage(menuPage);

        if (menuPageObject == null) {
            plugin.messageManager.sendMessage(player, "invalid-page");
            return true;
        }

        for (MenuItem menuItem : menuPageObject.getItems()) {
            ItemStack item = menuItem.getItem(plugin);
            processItemLore(menuItem, item, player);
            addItemToSlots(menuItem, item, inventory);
        }

        return false;
    }

    private MenuPage findMenuPage(int pageNumber) {
        for (MenuPage page : menuPages) {
            if (page.getPage() == pageNumber) {
                return page;
            }
        }
        return null;
    }

    private void processItemLore(MenuItem menuItem, ItemStack item, Player player) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        List<String> lore = new ArrayList<>(menuItem.getLore());

        for (int i = 0; i < lore.size(); i++) {
                String loreLine = lore.get(i);
                loreLine = loreLine.replace("%player_level%", String.valueOf(player.getLevel()));
                loreLine = loreLine.replace("%player_exp%", String.valueOf(plugin.experienceUtils.getAccurateTotalExperience(player)));
                loreLine = loreLine.replace("%exp_type%", menuItem.getExpType().toString());
                loreLine = loreLine.replace("%exp_cost%", String.valueOf(menuItem.getExpCost()));
                lore.set(i, loreLine);
                lore.set(i, loreLine);
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }

    public List<Inventory> getOpenInventories() {
        return new ArrayList<>(openInventories.keySet());
    }

    public void addInventory(int page, Inventory inventory) {
        openInventories.put(inventory, page);
    }

    public void closeInventory(Player player, Inventory inventory) {
        if (openInventories.containsKey(inventory)) {
            openInventories.remove(inventory);
            player.closeInventory();
        }
    }

    public void removeInventory(Inventory inventory) {
        openInventories.remove(inventory);
    }

    public void addItemToSlots(MenuItem menuItem, ItemStack itemStack, Inventory inventory) {
        List<Integer> slots = menuItem.getSlots();
        slots.forEach(slot -> inventory.setItem(slot, itemStack));
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void loadMenu() {
        FileConfiguration config = menuConfig.getConfig();
        this.menuName = config.getString("settings.name", "Shop Menu");
        this.menuSize = config.getInt("settings.size", 54);
        this.menuPageCount = config.getInt("settings.pages", 1);

        // Load items by type
        loadItemsFromConfig(config, "decorations", ItemType.DECORATION, NavType.NONE);
        loadItemsFromConfig(config, "navigation", ItemType.NAVIGATION, null);
        loadPages(config);
    }

    private void loadItemsFromConfig(FileConfiguration config, String section, ItemType itemType, NavType defaultNavType) {
        if (config.getConfigurationSection(section) != null) {
            for (String key : config.getConfigurationSection(section).getKeys(false)) {
                String itemPath = section + "." + key;
                NavType navType = defaultNavType;

                if (itemType == ItemType.NAVIGATION && config.contains(itemPath + ".nav_type")) {
                    try {
                        navType = NavType.valueOf(config.getString(itemPath + ".nav_type"));
                    } catch (IllegalArgumentException e) {
                        navType = NavType.NONE; // Fallback to NONE if invalid
                    }
                }

                createMenuItems(itemPath, itemType, navType);
            }
        }
    }

    private void loadPages(FileConfiguration config) {
        if (config.getConfigurationSection("pages") != null) {
            for (String page : config.getConfigurationSection("pages").getKeys(false)) {
                int pageNumber = Integer.parseInt(page);
                MenuPage menuPage = new MenuPage(menuConfig.getMenuTitle(), menuSize, pageNumber, null);

                // Create a list of items specific to this page
                List<MenuItem> pageItems = new ArrayList<>();

                // Add only global items (decorations and navigation) to each page
                for (MenuItem item : menuItems) {
                    if (item.getType() == ItemType.DECORATION || item.getType() == ItemType.NAVIGATION) {
                        pageItems.add(item);
                    }
                }

                // Add page-specific items
                if (config.getConfigurationSection("pages." + page) != null) {
                    for (String pageItem : config.getConfigurationSection("pages." + page).getKeys(false)) {
                        String itemPath = "pages." + page + "." + pageItem;
                        pageItems.add(createMenuItems(itemPath, ItemType.ITEM, NavType.NONE));
                    }
                }

                menuPage.setItems(pageItems);
                menuPages.add(menuPage);
            }
        }
    }

    public int getCurrentPage(Inventory openInventory) {
        Integer page = openInventories.get(openInventory);
        if (page == null) {
            plugin.getLogger().warning("Attempted to get page for an inventory that is not tracked: " + openInventory);
            return -1; // Return an invalid page number to indicate not found
        }
        return page;
    }

    public MenuItem createMenuItems(String location, ItemType itemType, NavType navType) {
        FileConfiguration config = menuConfig.getConfig();
        List<Integer> slots = config.getIntegerList(location + ".slot");

        // Add error handling for ExpType
        ExpType expType;
        try {
            expType = ExpType.valueOf(config.getString(location + ".exp_type", "NONE"));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid exp_type at " + location + ", defaulting to NONE");
            expType = ExpType.NONE;
        }

        MenuItem menuItem = new MenuItem(
                config.getString(location + ".display_name", ""),
                config.getStringList(location + ".lore"),
                config.getString(location + ".type", ""),
                UUID.randomUUID(),
                config.getString(location + ".display_item", "STONE"),
                config.getStringList(location + ".commands"),
                slots,
                navType,
                itemType,
                config.getInt(location + ".exp_cost", 0),
                expType);

        if (!menuItems.contains(menuItem)) {
            menuItems.add(menuItem);
            menuItemsMap.put(menuItem.getId(), menuItem); // Add to map
        }

        return menuItem;
    }

    public MenuItem getMenuItem(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return null;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return null;

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "menu_item_id");

        if (!container.has(key, PersistentDataType.STRING)) return null;

        String idString = container.get(key, PersistentDataType.STRING);
        if (idString == null) return null;

        try {
            UUID id = UUID.fromString(idString);
            return menuItemsMap.get(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}