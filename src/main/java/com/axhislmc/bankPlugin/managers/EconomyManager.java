package com.axhislmc.bankPlugin.managers;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.config.SettingsType;
import com.axhislmc.bankPlugin.model.PlayerBalance;
import org.bukkit.Bukkit;

import java.util.*;

public class EconomyManager {

    private final BankPlugin plugin;

    private final Map<UUID, Double> cachedBalances = new HashMap<>();
    private List<PlayerBalance> cachedTopList;
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

    public List<PlayerBalance> getTopBalances() {
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
        if (cachedBalances.containsKey(uuid)) {
            return cachedBalances.get(uuid);
        }
        return plugin.getDatabaseManager().getBalance(uuid);
    }

    public void setBalance(UUID uuid, double amount) {
        cachedBalances.put(uuid, amount);
        lastTopUpdate = 0;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                plugin.getDatabaseManager().setBalance(uuid, amount)
        );
    }

    public boolean transferMoney(UUID sender, UUID receiver, double amount) {
        if (getBalance(sender) < amount) {
            return false;
        }

        double oldSenderBalance = getBalance(sender);
        double oldReceiverBalance = getBalance(receiver);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            boolean success = plugin.getDatabaseManager().transfer(sender, receiver, amount);
            if (success) {
                plugin.getDatabaseManager().logTransaction(sender, receiver, amount);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (cachedBalances.containsKey(sender))
                        cachedBalances.put(sender, oldSenderBalance - amount);

                    if (cachedBalances.containsKey(receiver))
                        cachedBalances.put(receiver, oldReceiverBalance + amount);

                });
            }
        });

        return true;
    }

    public void addMoney(UUID uuid, double amount) {
        setBalance(uuid, getBalance(uuid) + amount);
    }

    public boolean removeMoney(UUID uuid, double amount) {
        if (getBalance(uuid) < amount) return false;
        setBalance(uuid, getBalance(uuid) - amount);
        return true;
    }
}
