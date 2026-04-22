package com.axhislmc.bankPlugin.menus;

import com.axhislmc.bankPlugin.BankPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BankMenu {
    private final Inventory bankMenu;
    private final BankPlugin plugin;
    private final Player player;

    public BankMenu(BankPlugin plugin, Player player) {
        this.bankMenu = Bukkit.createInventory(new BankMenuHolder(), 27, mm("<b><dark_green>Bank"));
        this.plugin = plugin;
        this.player = player;

        bankMenu.setItem(12, getPayItem());
        bankMenu.setItem(13, getBalanceItem());
        bankMenu.setItem(14, getTopItem());
        setGlassBorder();
    }

    public void open() {
        player.openInventory(bankMenu);
    }

    private void setGlassBorder() {
        ItemStack greyGlassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = greyGlassPane.getItemMeta();
        meta.setHideTooltip(true);
        greyGlassPane.setItemMeta(meta);

        for (int i = 0; i < 9; i++) {
            bankMenu.setItem(i, greyGlassPane);
        }

        for (int i = 18; i < 27; i++) {
            bankMenu.setItem(i, greyGlassPane);
        }
    }

    private ItemStack getPayItem() {
        ItemStack payItem = new ItemStack(Material.PLAYER_HEAD);

        ItemMeta payItemMetaData = payItem.getItemMeta();
        payItemMetaData.displayName(mm("<!i><b><yellow>Pay"));

        List<Component> payItemLore = new ArrayList<>();
        payItemLore.add(mm("<grey>Type <white>/bank pay <Player> <amount></white> in chat to pay."));
        payItemMetaData.lore(payItemLore);

        payItem.setItemMeta(payItemMetaData);
        return payItem;
    }

    private ItemStack getBalanceItem() {
        ItemStack balanceItem = new ItemStack(Material.GOLD_INGOT);

        ItemMeta balanceItemMetaData = balanceItem.getItemMeta();
        balanceItemMetaData.displayName(mm("<!i><b><aqua>Balance"));

        List<Component> balanceItemLore = new ArrayList<>();
        balanceItemLore.add(mm(String.format("<grey>Your balance: <!i><green>%.2f$",
                plugin.getEconomyManager().getBalance(player.getUniqueId()))));
        balanceItemMetaData.lore(balanceItemLore);

        balanceItem.setItemMeta(balanceItemMetaData);
        return balanceItem;
    }

    private ItemStack getTopItem() {
        ItemStack topItem = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);

        ItemMeta topItemMetaData = topItem.getItemMeta();
        topItemMetaData.displayName(mm("<!i><b><dark_green>Top Players"));

        List<Component> topItemLore = new ArrayList<>();
        List<Map.Entry<UUID, Double>> balanceTopList = plugin.getEconomyManager().getTopBalances();
        int limit = Math.min(10, balanceTopList.size());

        for (int i = 0; i < limit; i++) {
            Map.Entry<UUID, Double> entry = balanceTopList.get(i);
            if (entry == null || entry.getKey() == null) continue;

            String playerName = plugin.getServer().getOfflinePlayer(entry.getKey()).getName();
            if (playerName == null) playerName = "Unknown";

            topItemLore.add(mm(String.format("<!i><grey>%d. <white>%s<white>: <green>%.2f$", (i + 1), playerName, entry.getValue())));
        }

        topItemMetaData.lore(topItemLore);

        topItem.setItemMeta(topItemMetaData);
        return topItem;
    }

    private Component mm(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }
}
