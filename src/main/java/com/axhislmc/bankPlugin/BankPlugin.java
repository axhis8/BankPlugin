package com.axhislmc.bankPlugin;

import com.axhislmc.bankPlugin.managers.CommandManager;
import com.axhislmc.bankPlugin.managers.EconomyManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BankPlugin extends JavaPlugin {

    private EconomyManager economyManager;

    @Override
    public void onEnable() {

        this.economyManager = new EconomyManager(this);
        economyManager.load();

        CommandManager commandManager = new CommandManager(this);
        commandManager.setupCommands();

        getCommand("bank").setExecutor(commandManager);
        getCommand("bank").setTabCompleter(commandManager);

        getLogger().info("Successfully started Bank Plugin!");
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    @Override
    public void onDisable() {
        economyManager.save();
        getLogger().info("Safely disabled Bank Plugin.");
    }
}
