package com.axhislmc.bankPlugin.api;

import com.axhislmc.bankPlugin.BankPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderManager extends PlaceholderExpansion {
    private final BankPlugin plugin;

    public PlaceholderManager(BankPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bankplugin";
    }

    @Override
    public @NotNull String getAuthor() {
        return "axhislmc";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return null;

        return switch (params.toLowerCase()) {
            case "balance" -> String.format("%.2f", plugin.getEconomyManager().getBalance(player.getUniqueId()));
            case "balance_formatted" -> String.format("%.2f$", plugin.getEconomyManager().getBalance(player.getUniqueId()));
            default -> null;
        };
    }
}
