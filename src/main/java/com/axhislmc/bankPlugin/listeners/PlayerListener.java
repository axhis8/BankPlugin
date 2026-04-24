package com.axhislmc.bankPlugin.listeners;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.config.ActionBarType;
import com.axhislmc.bankPlugin.menus.Menu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class PlayerListener implements Listener {
    private final BankPlugin plugin;

    public PlayerListener(BankPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player joinedPlayer = event.getPlayer();
        boolean showActionbar = plugin.getBankConfig().getBoolean(ActionBarType.SHOW_BALANCE_UPON_JOIN);

        // Add Data to Database
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDatabaseManager().addPlayerJoin(joinedPlayer);
            plugin.getEconomyManager().loadPlayer(joinedPlayer.getUniqueId());

            // Actionbar
            if (joinedPlayer.isOnline() && showActionbar) { // If Player left while the Database was loading
                double balance = plugin.getEconomyManager().getBalance(joinedPlayer.getUniqueId());

                // Run UIs back in Main Thread
                Bukkit.getScheduler().runTask(plugin, () -> {
                    joinedPlayer.sendActionBar(MiniMessage.miniMessage().deserialize("<grey>Loading Balance..."));
                    actionBarOnJoin(joinedPlayer, balance);
                });
            }
        });
    }

    private void actionBarOnJoin(Player player, double balance) {
        int actionBarDelayInSeconds = plugin.getBankConfig().getInt(ActionBarType.SHOW_UPON_JOINING_DELAY_SECONDS);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            TagResolver amountTag = Placeholder.parsed("amount", String.format("%.2f", balance));
            String message = plugin.getBankConfig().getString(ActionBarType.TEXT);
            Component actionBarMessage = MiniMessage.miniMessage().deserialize(message, amountTag);

            player.sendActionBar(actionBarMessage);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.3f, 1f);
        }, 20L * actionBarDelayInSeconds); // Delay in ticks
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getEconomyManager().unloadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();

        // Happens when Player clicks outside the Inventory
        if (clickedInventory == null)
            return;

        if (clickedInventory.getHolder() instanceof final Menu menu) {
            event.setCancelled(true);
            menu.click((Player) event.getWhoClicked(), event.getSlot());
        }
    }

    @EventHandler
    public void removeColonCommand(PlayerCommandSendEvent event) {
        event.getCommands().remove(plugin.getName().toLowerCase() + ":bank");
    }
}
