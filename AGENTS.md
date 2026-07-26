# AGENTS.md - PlutonEXP Subagent Roles & Guidelines

## Overview
This file defines the strict subagent roles and delegation rules for the PlutonEXP Minecraft Spigot plugin project.

## Subagent Roles

### 1. PR & Release Wrangler
* **Domain:** Git branch management, staging, conventional commits, remote pushing, and PR drafting.
* **Responsibilities:**
  - Check active branch status.
  - Save uncommitted work before switching branches.
  - Create feature branches from `master`.
  - Perform git add, commit, push, and release management.

### 2. Commands & Core Logic Expert
* **Domain:** `net.plutondev.expShop.commands` & Command Registrations.
* **Responsibilities:**
  - Implement and fix command executors (`OpenCommand`, `ReloadCommand`, `HelpCommand`, `CommandManager`).
  - Implement `/expshop open <player>` targeting for admins with permission checks.
  - Implement `TabCompleter` in `CommandManager` for subcommands and player names.
  - Fix console sender casting errors (`ClassCastException`).

### 3. GUI & Listener Expert
* **Domain:** `net.plutondev.expShop.listener`, `net.plutondev.expShop.menu`, `net.plutondev.expShop.objects`.
* **Responsibilities:**
  - Fix `MenuListener` shift-clicking and top-inventory click checks.
  - Add `InventoryDragEvent` handling to prevent item dragging into shop GUI.
  - Fix menu reload responsiveness by tracking open inventory views or updating open GUI PDC tags.
  - Add `onDisable` open inventory closing.
  - Add robust config bounds checking (inventory size multiples of 9, slot bounds, fallback material validation for invalid material names).

### 4. System Utilities & Experience Expert
* **Domain:** `net.plutondev.expShop.utils`, `net.plutondev.expShop.message`, `PlayerJoin.java`, `ExpShop.java`.
* **Responsibilities:**
  - Fix `ExperienceUtils` level 0 level deduction fraction retention and cost validation (`expCost > 0`).
  - Add hex color code support (`&#HEX`) to `MessageManager`.
  - Fix sound reloading in `ReloadCommand` / `MessageManager`.
  - Make `PlayerJoin` update checker run asynchronously on Bukkit scheduler to prevent main thread freezes.

### 5. QA Gatekeeper
* **Domain:** Project build and compilation verification.
* **Responsibilities:**
  - Run `mvn clean package` or `mvn compile` to verify clean build without syntax errors or broken dependencies.
