package com.axhislmc.bankPlugin.commands;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.managers.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.*;

public class TopSubCommand implements SubCommand {
    private final BankPlugin plugin;

    public TopSubCommand(BankPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommand() {
        return "top";
    }

    @Override
    public String getDescription() {
        return "Shows the richest Player on the Server.";
    }

    @Override
    public List<String> getSubCommandArgs(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        List<Map.Entry<UUID, Double>> balanceTopList = plugin.getEconomyManager().getTopBalances();

        sender.sendRichMessage("<grey>-- <yellow>Top 10 richest Players</yellow> --");

        int limit = Math.min(10, balanceTopList.size());
        for (int i = 0; i < limit; i++) {
            Map.Entry<UUID, Double> entry = balanceTopList.get(i);

            String playerName = plugin.getServer().getOfflinePlayer(entry.getKey()).getName();
            if (playerName == null) playerName = "Unknown";

            sender.sendRichMessage(String.format("<grey>%d. <white>%s<white>: <green>%.2f$", (i + 1), playerName, entry.getValue()));
        }
    }
}
