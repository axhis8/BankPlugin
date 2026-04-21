package com.axhislmc.bankPlugin.managers;

import com.axhislmc.bankPlugin.BankPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EconomyManager {

    private final BankPlugin plugin;
    private final Map<UUID, Double> balances = new HashMap<>();
    private final File file;
    private final FileConfiguration config;

    public EconomyManager(BankPlugin plugin) {
        this.plugin = plugin;

        this.file = new File(plugin.getDataFolder(), "balances.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        // So that if in the same time while saving the balances map is modified, it won't crash because of the snapshop
        Map<UUID, Double> snapshot = new HashMap<>(balances);

        config.set("balances", null);
        for (Map.Entry<UUID, Double> set : snapshot.entrySet()) {
            config.set("balances."+ set.getKey(), set.getValue());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Error saving Balances in File");
            e.printStackTrace();
        }
    }

    public void load() {
        if (!file.exists()) return;

        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().severe("Error loading Balances from File");
            e.printStackTrace();
        }

        ConfigurationSection section = config.getConfigurationSection("balances");
        if (section != null) {

            for (String key : section.getKeys(false)) {
                UUID playerUUID = UUID.fromString(key);
                double playerBalance = section.getDouble(key);

                balances.put(playerUUID, playerBalance);
            }
            plugin.getLogger().info("Successfully loaded balances-");
        }
    }

    public double getBalance(UUID uuid) {
        return balances.getOrDefault(uuid, 0.0);
    }

    public void setBalance(UUID uuid, double amount) {
        balances.put(uuid, amount);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::save);
    }

    public void addMoney(UUID uuid, double amount) {
        setBalance(uuid, getBalance(uuid) + amount);
    }

    public boolean removeMoney(UUID uuid, double amount) {
        double currentBalance = getBalance(uuid);
        if (currentBalance < amount) {
            return false;
        }
        setBalance(uuid, currentBalance - amount);
        return true;
    }
}
