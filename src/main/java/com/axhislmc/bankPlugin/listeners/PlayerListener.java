package com.axhislmc.bankPlugin.listeners;

import com.axhislmc.bankPlugin.menus.BankMenuHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class PlayerListener implements Listener {

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
