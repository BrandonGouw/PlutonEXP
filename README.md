# PlutonEXP - Experience Shop Plugin

[![Version](https://img.shields.io/badge/version-1.12-blue.svg)](https://github.com/plutondev/expshop)
[![Minecraft](https://img.shields.io/badge/minecraft-1.20+-green.svg)](https://www.spigotmc.org/)
[![Java](https://img.shields.io/badge/java-17+-orange.svg)](https://www.oracle.com/java/)

## 📖 Overview

PlutonEXP is a comprehensive Minecraft plugin that allows players to exchange their experience points for valuable items through an intuitive GUI shop system. Players can spend both experience levels and experience points to purchase weapons, armor, tools, and special items.

## ✨ Features

- **🛍️ Interactive GUI Shop** - Easy-to-use inventory-based shop interface
- **💰 Dual Currency System** - Support for both experience levels and experience points
- **📄 Multi-Page Support** - Organized items across multiple pages
- **🎯 Customizable Items** - Fully configurable shop items with custom enchantments
- **🔊 Sound Effects** - Configurable sounds for actions and feedback
- **💬 Custom Messages** - Personalized messaging system with color support
- **🔄 Live Reload** - Hot-reload configuration without server restart
- **📊 bStats Integration** - Optional metrics collection
- **🔐 Permission-Based** - Granular permission control
- **🎨 Customizable UI** - Configurable shop title, size, and decorations

## 📋 Requirements

- **Minecraft Server**: Spigot/Paper 1.20+
- **Java Version**: 17 or higher
- **Dependencies**: None (standalone plugin)

## 🚀 Installation

1. Download the latest `PlutonEXP-X.X.jar` from the releases
2. Place the jar file in your server's `plugins/` folder
3. Restart your server or use `/reload` (not recommended)
4. Configure the plugin using the generated config files

## ⚙️ Configuration

### Main Configuration (`config.yml`)

The main configuration file contains:

- **Settings**: bStats metrics configuration
- **Sounds**: Customizable sound effects for success/failure actions
- **Messages**: All plugin messages with color support and sound integration

```yaml
settings:
  bstats-metrics:
    enabled: true

sounds:
  failure:
    name: ENTITY_VILLAGER_NO
    volume: 1.0
    pitch: 1.0
  success:
    name: ENTITY_PLAYER_LEVELUP
    volume: 1.0
    pitch: 1.0

messages:
  item-purchased:
    message: "&aYou have purchased the item!"
    sound: success
```

### Shop Configuration (`shop.yml`)

The shop configuration defines the GUI layout and items:

- **Settings**: Shop title, size, and page count
- **Decorations**: Border items and visual elements
- **Navigation**: Page navigation and utility buttons
- **Pages**: Items organized by categories

#### Example Shop Item Configuration:

```yaml
pages:
  1:
    diamond_sword:
      display_item: DIAMOND_SWORD
      display_name: "&b&lDiamond Sword"
      lore:
        - "&7A powerful diamond sword"
        - ""
        - "&eCost: &a%exp_cost% %exp_type%"
      slot: [10]
      exp_cost: 20
      exp_type: LEVEL  # or POINTS
      commands:
        - "give %player% diamond_sword{Enchantments:[{id:sharpness,lvl:5}]} 1"
```

## 🎮 Commands

| Command | Description | Permission | Aliases |
|---------|-------------|------------|---------|
| `/expshop` | Opens the experience shop | `plutonexp.open` | `plutonexp`, `xpshop`, `xpmenu`, `expmenu`, `eshop` |
| `/expshop help` | Shows help information | `plutonexp.help` | - |
| `/expshop reload` | Reloads the configuration | `plutonexp.reload` | - |

## 🔐 Permissions

| Permission | Description | Default |
|-----------|-------------|---------|
| `plutonexp.open` | Access to the experience shop | `true` |
| `plutonexp.help` | Access to help command | `true` |
| `plutonexp.reload` | Ability to reload configuration | `op` |

## 🛍️ Shop Categories

The default shop includes three organized pages:

### Page 1: Weapons & Combat
- **Diamond Sword** (Sharpness V) - 20 levels
- **Power Bow** (Power V) - 22 levels
- **Loyalty Trident** (Loyalty III) - 30 levels
- **Golden Apples** (16x) - 500 experience points

### Page 2: Armor
- **Netherite Helmet** (Protection IV) - 25 levels
- **Elytra** (Unbreaking III) - 45 levels
- **Netherite Chestplate** (Protection IV) - 35 levels
- **Netherite Boots** (Protection IV, Depth Strider III) - 25 levels

### Page 3: Special Items
- **Beacon** - 40 levels
- **Dragon Egg** - 100 levels
- **Enchanting Table** - 40 levels
- **Shulker Box** - 20 levels

## 🔧 Technical Details

### Project Structure
```
src/main/java/net/plutondev/expShop/
├── ExpShop.java              # Main plugin class
├── commands/                 # Command handling
│   ├── CommandManager.java   # Command registration and routing
│   ├── OpenCommand.java      # Shop opening command
│   ├── HelpCommand.java      # Help command
│   └── ReloadCommand.java    # Configuration reload
├── enums/                    # Enumeration types
│   ├── ExpType.java          # Experience types (LEVEL/POINTS)
│   ├── ItemType.java         # Item types
│   └── NavType.java          # Navigation types
├── listener/                 # Event listeners
│   ├── MenuListener.java     # GUI interaction handling
│   └── PlayerJoin.java       # Player join events
├── menu/                     # GUI management
│   ├── MenuConfig.java       # Menu configuration loader
│   └── MenuManager.java      # Menu creation and management
├── message/                  # Messaging system
│   └── MessageManager.java   # Message handling with sounds
├── objects/                  # Data objects
│   ├── CommandObject.java    # Command abstraction
│   ├── MenuItem.java         # Shop item representation
│   ├── MenuPage.java         # Page management
│   ├── MessageObject.java    # Message with sound
│   └── SoundObject.java      # Sound configuration
└── utils/                    # Utility classes
    └── ExperienceUtils.java  # Experience calculations
```

### Building from Source

1. Clone the repository
2. Ensure Java 17+ is installed
3. Run `mvn clean package`
4. Find the built jar in `target/PlutonEXP-X.X.jar`

### Dependencies
- **Spigot API** 1.20.1 (provided)
- **bStats** 3.0.2 (shaded)

## 🎨 Customization

### Adding New Items

1. Open `shop.yml`
2. Navigate to the desired page
3. Add a new item entry following this structure:

```yaml
pages:
  1:
    your_item_name:
      display_item: MATERIAL_NAME
      display_name: "&c&lYour Item"
      lore:
        - "&7Description line 1"
        - "&7Description line 2"
        - ""
        - "&eCost: &a%exp_cost% %exp_type%"
      slot: [SLOT_NUMBER]
      exp_cost: COST_AMOUNT
      exp_type: LEVEL  # or POINTS
      commands:
        - "give %player% material_name 1"
        - "additional command if needed"
```

### Custom Commands

Items can execute multiple commands when purchased. Available placeholders:
- `%player%` - Player's name
- `%exp_cost%` - Item cost
- `%exp_type%` - Experience type (LEVEL/POINTS)

### Sound Customization

All sounds can be customized in `config.yml`. Available sounds can be found in the [Bukkit Sound documentation](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html).

## 🐛 Troubleshooting

### Common Issues

**Shop not opening:**
- Check if player has `plutonexp.open` permission
- Verify plugin is enabled in console
- Check for any error messages

**Items not working:**
- Ensure material names are correct (check [Material list](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html))
- Verify command syntax in shop.yml
- Check console for errors

**Configuration not loading:**
- Verify YAML syntax (use a YAML validator)
- Check file permissions
- Use `/expshop reload` after changes

## 📞 Support

- **Issues**: Create an issue on the project repository
- **Documentation**: Check the configuration files for inline comments
- **Updates**: Plugin checks for updates automatically and notifies ops

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🏆 Credits

- **Author**: BrandonGouw
- **Plugin ID**: 25487 (bStats)
- **Spigot Resource**: [124096](https://www.spigotmc.org/resources/124096/)

## 📈 Statistics

This plugin uses bStats to collect anonymous usage statistics. This can be disabled in the configuration file. View statistics at [bStats PlutonEXP](https://bstats.org/plugin/bukkit/PlutonEXP/25487).

---

**Made with ❤️ for the Minecraft community**
