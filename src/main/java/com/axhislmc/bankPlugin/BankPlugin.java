package com.axhislmc.bankPlugin;

import com.axhislmc.bankPlugin.config.Messages;
import com.axhislmc.bankPlugin.listeners.PlayerListener;
import com.axhislmc.bankPlugin.managers.CommandManager;
import com.axhislmc.bankPlugin.managers.DatabaseManager;
import com.axhislmc.bankPlugin.managers.EconomyManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class BankPlugin extends JavaPlugin {

    private EconomyManager economyManager;
    private DatabaseManager databaseManager;
    private Messages messages;

    @Override
    public void onLoad() {
        this.databaseManager = new DatabaseManager(this);
        databaseManager.load();

        saveResource("messages.yml", false);
    }

    @Override
    public void onEnable() {

        this.messages = new Messages(this, new File(getDataFolder(), "messages.yml"));
        this.economyManager = new EconomyManager(this);

        CommandManager commandManager = new CommandManager(this);
        commandManager.setupCommands();

        getCommand("bank").setExecutor(commandManager);
        getCommand("bank").setTabCompleter(commandManager);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

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

    @Override
    public void onDisable() {
        databaseManager.close();
        getLogger().info("Safely disabled Bank Plugin.");
    }
}
