package com.axhislmc.bankPlugin.menus;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.config.SettingsType;
import com.axhislmc.bankPlugin.model.PlayerBalance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BankMenu extends BaseMenu {
    private final BankPlugin plugin;
    private final Player player;

    public BankMenu(BankPlugin plugin, Player player) {
        super("<b><dark_green>Bank", Rows.THREE);
        this.plugin = plugin;
        this.player = player;
    }

    private void setGlassBorder() {
        ItemStack greyGlassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = greyGlassPane.getItemMeta();
        meta.setHideTooltip(true);
        greyGlassPane.setItemMeta(meta);

        for (int i = 0; i < 9; i++) {
            setItem(i ,greyGlassPane);
        }

        for (int i = 18; i < 27; i++) {
            setItem(i ,greyGlassPane);
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
        double playerBalance = plugin.getEconomyManager().getBalance(player.getUniqueId());

        balanceItemLore.add(mm(String.format("<grey>Your balance: <!i><green>%.2f$", playerBalance)));
        balanceItemMetaData.lore(balanceItemLore);

        balanceItem.setItemMeta(balanceItemMetaData);
        return balanceItem;
    }

    private ItemStack getTopItem() {
        ItemStack topItem = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);

        ItemMeta topItemMetaData = topItem.getItemMeta();
        topItemMetaData.displayName(mm("<!i><b><dark_green>Top Players"));

        List<Component> topItemLore = new ArrayList<>();
        List<PlayerBalance> balanceTopList = plugin.getEconomyManager().getTopBalances();
        int limit = Math.min(plugin.getBankConfig().getInt(SettingsType.AMOUNT_SHOW_TOP_PLAYERS), balanceTopList.size());

        int idx = 1;
        for (PlayerBalance playerBalance : balanceTopList) {
            if (playerBalance == null || playerBalance.uuid() == null) continue;

            String playerName = plugin.getServer().getOfflinePlayer(playerBalance.uuid()).getName();
            if (playerName == null) playerName = "Unknown";

            topItemLore.add(mm(String.format("<!i><grey>%d. <white>%s<white>: <green>%.2f$", idx, playerName, playerBalance.balance())));
            idx++;
            if (idx > limit)
                break;
        }

        topItemMetaData.lore(topItemLore);

        topItem.setItemMeta(topItemMetaData);
        return topItem;
    }

    private Component mm(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    @Override
    public void onSetItems() {
        setItem(12, getPayItem());
        setItem(13, getBalanceItem());
        setItem(14, getTopItem());
        setGlassBorder();
    }
}
