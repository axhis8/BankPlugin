package com.axhislmc.bankPlugin.managers;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.config.SettingsType;
import org.bukkit.Bukkit;

import java.util.*;

public class EconomyManager {

    private final BankPlugin plugin;

    private final Map<UUID, Double> cachedBalances = new HashMap<>();
    private List<Map.Entry<UUID, Double>> cachedTopList;
    private long lastTopUpdate;

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
        final long minutesInterval = plugin.getBankConfig().getInt(SettingsType.TOP_LIST_UPDATE_INTERVAL_MINUTES);
        final long INTERVAL = minutesInterval * 60 * 1000;

        if(cachedTopList == null || (System.currentTimeMillis() - lastTopUpdate) > INTERVAL) {
            int amountTopPlayers = plugin.getBankConfig().getInt(SettingsType.AMOUNT_SHOW_TOP_PLAYERS);
            this.cachedTopList = plugin.getDatabaseManager().getTopList(amountTopPlayers);

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
        lastTopUpdate = 0;
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
