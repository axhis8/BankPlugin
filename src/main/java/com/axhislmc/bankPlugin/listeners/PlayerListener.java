package com.axhislmc.bankPlugin.listeners;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.config.MessageType;
import com.axhislmc.bankPlugin.menus.BankMenuHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        final Player joinedPlayer = event.getPlayer();

        // Add Data to Database
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().addPlayerJoin(joinedPlayer);
            plugin.getEconomyManager().loadPlayer(joinedPlayer.getUniqueId());

            // Actionbar
            if (joinedPlayer.isOnline()) { // If Player left while the Database was loading
                joinedPlayer.sendActionBar(MiniMessage.miniMessage().deserialize("<grey>Loading Balance..."));

                double balance = plugin.getEconomyManager().getBalance(joinedPlayer.getUniqueId());
                actionBarOnJoin(joinedPlayer, balance);
            }
        });
    }

    private void actionBarOnJoin(Player player, double balance) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            TagResolver amountTag = Placeholder.parsed("amount", String.format("%.2f", balance));
            String message = plugin.getMessages().getRawMessage(MessageType.JOIN_BALANCE_ACTIONBAR);
            Component actionBarMessage = MiniMessage.miniMessage().deserialize(message, amountTag);

            player.sendActionBar(actionBarMessage);
            player.playSound(player.getLocation(), MessageType.JOIN_BALANCE_ACTIONBAR.getSound(), 0.3f, 1f);
        }, 20L); // 1 Second (Delay in ticks)
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
