package com.axhislmc.bankPlugin.menus;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.utils.Message;
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

public class BankMenu {
    private final Inventory bankMenu;
    private final BankPlugin plugin;
    private final Player player;

    public BankMenu(BankPlugin plugin, Player player) {
        this.bankMenu = Bukkit.createInventory(new BankMenuHolder(), 27,
                MiniMessage.miniMessage().deserialize("<b><dark_green>Bank"));
        this.plugin = plugin;
        this.player = player;

        bankMenu.setItem(12, getPayItem());
        bankMenu.setItem(13, getBalanceItem());
        bankMenu.setItem(14, getHelpItem());
        setGlassBorder();
    }

    public void open() {
        player.openInventory(bankMenu);
    }

    private void setGlassBorder() {
        ItemStack greyGlassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = greyGlassPane.getItemMeta();
        meta.displayName(Component.empty());
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
        payItemMetaData.displayName(MiniMessage.miniMessage().deserialize("<!i><b><yellow>Pay"));

        List<Component> payItemLore = new ArrayList<>();
        payItemLore.add(Message.PAY_LORE.asComponent());
        payItemMetaData.lore(payItemLore);

        payItem.setItemMeta(payItemMetaData);
        return payItem;
    }

    private ItemStack getBalanceItem() {
        ItemStack balanceItem = new ItemStack(Material.GOLD_INGOT);

        ItemMeta balanceItemMetaData = balanceItem.getItemMeta();
        balanceItemMetaData.displayName(MiniMessage.miniMessage().deserialize("<!i><b><aqua>Balance"));

        List<Component> balanceItemLore = new ArrayList<>();
        balanceItemLore.add(MiniMessage.miniMessage().deserialize(String.format("<grey>Your balance: <!i><green>%.2f$",
                plugin.getEconomyManager().getBalance(player.getUniqueId()))));
        balanceItemMetaData.lore(balanceItemLore);

        balanceItem.setItemMeta(balanceItemMetaData);
        return balanceItem;
    }

    private ItemStack getHelpItem() {
        ItemStack helpItem = new ItemStack(Material.PAPER);

        ItemMeta helpItemMetaData = helpItem.getItemMeta();
        helpItemMetaData.displayName(MiniMessage.miniMessage().deserialize("<!i><b><grey>Help"));

        List<Component> helpItemLore = new ArrayList<>();
        helpItemLore.add(Component.text("this command is under development"));
        helpItemMetaData.lore(helpItemLore);

        helpItem.setItemMeta(helpItemMetaData);
        return helpItem;
    }
}
