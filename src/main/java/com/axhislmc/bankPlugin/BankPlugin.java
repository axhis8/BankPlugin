package com.axhislmc.bankPlugin;

import com.axhislmc.bankPlugin.api.PlaceholderManager;
import com.axhislmc.bankPlugin.config.Config;
import com.axhislmc.bankPlugin.config.Messages;
import com.axhislmc.bankPlugin.listeners.PlayerListener;
import com.axhislmc.bankPlugin.managers.CommandManager;
import com.axhislmc.bankPlugin.managers.DatabaseManager;
import com.axhislmc.bankPlugin.managers.EconomyManager;
import com.axhislmc.bankPlugin.api.VaultManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class BankPlugin extends JavaPlugin {

    private EconomyManager economyManager;
    private DatabaseManager databaseManager;
    private VaultManager vaultManager;
    private Config config;
    private Messages messages;

    @Override
    public void onLoad() {
        this.databaseManager = new DatabaseManager(this);
        databaseManager.load();
    }

    @Override
    public void onEnable() {
        saveDefaultMessages();

        this.config = new Config(this);
        this.messages = new Messages(this);
        this.economyManager = new EconomyManager(this);

        CommandManager commandManager = new CommandManager(this);
        commandManager.setupCommands();

        getCommand("bank").setExecutor(commandManager);
        getCommand("bank").setTabCompleter(commandManager);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            this.vaultManager = new VaultManager(this);
            this.vaultManager.register();
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderManager(this).register();
            getLogger().info("PlaceholderAPI registered.");
        }

        getLogger().info("Successfully started Bank Plugin.");
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public Messages getMessages() {
        return this.messages;
    }

    public Config getBankConfig() {
        return this.config;
    }

    @Override
    public void onDisable() {
        if (vaultManager != null) vaultManager.unregister();
        databaseManager.close();
        getLogger().info("Safely disabled Bank Plugin.");
    }

    private void saveDefaultMessages() {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            saveResource("config.yml", false);
        }
    }
}
