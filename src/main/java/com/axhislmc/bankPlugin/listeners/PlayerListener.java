package com.axhislmc.bankPlugin.listeners;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.menus.BankMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final BankPlugin plugin;

    public PlayerListener(BankPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().addPlayerJoin(event.getPlayer());
            plugin.getEconomyManager().loadPlayer(event.getPlayer().getUniqueId());
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getEconomyManager().unloadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void cancelTakingItemFromInventory(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof BankMenuHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void removeColonCommand(PlayerCommandSendEvent event) {
        event.getCommands().remove("bankplugin:bank");
    }
}
