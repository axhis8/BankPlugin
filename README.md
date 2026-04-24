# BankPlugin

## About this Project

A Bank Plugin to manage the Economy of your Minecraft Server. This is my most ambitious Java project so far. It was a really rewarding experience.

The goal was to build something production-ready, not just a simple tutorial project. I focused heavily on clean architecture, async database operations, caching, and keeping everything modular and configurable.

## Requirements

- Java 21 or later
- Paper 1.21 or later
- Vault (optional, for economy integration with other plugins)
- PlaceholderAPI (optional, for placeholders in scoreboards etc.)

## Features

- Player bank accounts with persistent balances via SQLite
- Transfer money between players with atomic SQL transactions — no money gets lost or duplicated on server crash
- Top richest players leaderboard with configurable update interval and caching
- Fully configurable messages, settings and action bar via `config.yml`
- Action bar balance display on join with configurable delay and MiniMessage formatting
- GUI menu for managing your bank account in-game
- Transaction logging to database
- Permission system per command
- Async database operations to prevent server lag on DB reads/writes
- Balance caching with no unnecessary database calls
- SubCommand architecture for clean `/bank` command handling
- Vault integration for compatibility with other economy plugins
- PlaceholderAPI support — use `%bankplugin_balance%` in scoreboards, tab lists and more

## Installation

1. Download the latest `.jar` from releases
2. Place it in your server's `plugins/` folder
3. Restart the server
4. Edit `plugins/BankPlugin/config.yml` as needed

No database setup required, as SQLite creates itself automatically on first launch.

Optionally drop [Vault](https://www.spigotmc.org/resources/vault.34315/) and [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) in your `plugins/` folder for extended compatibility.

## Commands

| Command | Description |
|---|---|
| `/bank balance` | Shows your current balance |
| `/bank pay <Player> <amount>` | Transfers money to another player |
| `/bank top` | Shows the top richest players |
| `/bank setbalance <Player> <amount>` | Sets a player's balance (admin) |
| `/bank help` | Shows all available commands |

## Permissions

| Permission | Description |
|---|---|
| `bankplugin.use` | Use Bank Commands |
| `bankplugin.pay` | Send money to another player |
| `bankplugin.admin` | Admin commands (setbalance) |

## Placeholders

| Placeholder | Description |
|---|---|
| `%bankplugin_balance%` | Player's current balance |
| `%bankplugin_balance_formatted%` | Player's balance formatted with $ |

## Configuration

The `config.yml` is auto-generated on first launch. Here's a quick example of what's configurable:

```yaml
settings:
  starting-balance: 1000.0
  top-list-update-interval-minutes: 5
  amount-show-top-players: 10

action-bar:
  show-balance-upon-join: true
  show-upon-joining-delay-seconds: 2
  text: "<gold>Balance: <green><amount>$"

messages:
  not-enough-money: "<red>You don't have enough money!"
  money-transferred: "<green>Successfully sent <amount>$ to <target>!"
```

All messages support MiniMessage formatting for colors and styles.

## Built With

- Java 21
- Paper API 1.21
- SQLite + HikariCP (Connection Pooling)
- Gradle (Kotlin DSL)
- Adventure / MiniMessage (Text formatting)
- Vault API
- PlaceholderAPI

## What I Learned

- Building a full SubCommand architecture with interfaces
- Async database operations and keeping UI on the main thread
- Connection pooling with HikariCP
- Atomic SQL transactions with rollback on failure
- Balance caching with cache invalidation
- GUI inventory menus in Paper
- Enum-based config and message systems to avoid hardcoding
- Java Records for clean data models
- Separating concerns across packages: commands, managers, listeners, menus, config, model, utils
- Integrating external APIs (Vault, PlaceholderAPI)