package com.axhislmc.bankPlugin.api;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.managers.EconomyManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class VaultManager implements Economy {
    private final BankPlugin plugin;
    private final EconomyManager em;

    public VaultManager(BankPlugin plugin) {
        this.plugin = plugin;
        this.em = plugin.getEconomyManager();
    }

    public void register() {
        plugin.getServer().getServicesManager().register(
                Economy.class, this, plugin, org.bukkit.plugin.ServicePriority.Normal
        );
        plugin.getLogger().info("Vault Economy registered.");
    }

    public void unregister() {
        plugin.getServer().getServicesManager().unregisterAll(plugin);
    }

    private OfflinePlayer getPlayer(String playerName) {
        return plugin.getServer().getOfflinePlayer(playerName);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "BankPlugin";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public String currencyNamePlural() {
        return "Dollars";
    }

    @Override
    public String currencyNameSingular() {
        return "Dollar";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return true;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return true;
    }

    @Override
    public double getBalance(String playerName) {
        return getBalance(getPlayer(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return em.getBalance(player.getUniqueId());
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(getPlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        boolean success = em.removeMoney(player.getUniqueId(), amount);
        if (success)
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "");
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Not enough money.");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(getPlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        em.addMoney(player.getUniqueId(), amount);
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return true;
    }
}