package com.axhislmc.bankPlugin;

import com.axhislmc.bankPlugin.listeners.PlayerListener;
import com.axhislmc.bankPlugin.managers.CommandManager;
import com.axhislmc.bankPlugin.managers.DatabaseManager;
import com.axhislmc.bankPlugin.managers.EconomyManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BankPlugin extends JavaPlugin {

    private EconomyManager economyManager;
    private DatabaseManager databaseManager;

    @Override
    public void onLoad() {
        this.databaseManager = new DatabaseManager(this);
        databaseManager.load();
    }

    @Override
    public void onEnable() {

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

    @Override
    public void onDisable() {
        databaseManager.close();
        getLogger().info("Safely disabled Bank Plugin.");
    }
}
