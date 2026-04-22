package com.axhislmc.bankPlugin.managers;

import com.axhislmc.bankPlugin.BankPlugin;
import org.bukkit.Bukkit;

import java.util.*;

public class EconomyManager {

    private final BankPlugin plugin;

    private final Map<UUID, Double> cachedBalances = new HashMap<>();
    private List<Map.Entry<UUID, Double>> cachedTopList;
    private long lastTopUpdate;

    private final int SHOWED_TOP_PLAYERS = 10;

    public EconomyManager(BankPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadPlayer(UUID uuid) {
        double balance = plugin.getDatabaseManager().getBalance(uuid);
        cachedBalances.put(uuid, balance);
    }

    public void unloadPlayer(UUID uuid) {
        cachedBalances.remove(uuid);
    }

    public List<Map.Entry<UUID, Double>> getTopBalances() {
        long INTERVAL = 5 * 60 * 1000;

        if(cachedTopList == null || (System.currentTimeMillis() - lastTopUpdate) > INTERVAL) {
            this.cachedTopList = plugin.getDatabaseManager().getTopList(SHOWED_TOP_PLAYERS);
            this.lastTopUpdate = System.currentTimeMillis();
            plugin.getLogger().info("Updated Top Richest Players!");
        }
        return cachedTopList;
    }

    public double getBalance(UUID uuid) {
        return cachedBalances.getOrDefault(uuid, plugin.getDatabaseManager().getBalance(uuid));
    }

    public void setBalance(UUID uuid, double amount) {
        cachedBalances.put(uuid, amount);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                plugin.getDatabaseManager().setBalance(uuid, amount)
        );
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
