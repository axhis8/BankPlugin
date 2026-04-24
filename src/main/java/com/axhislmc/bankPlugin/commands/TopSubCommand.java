package com.axhislmc.bankPlugin.commands;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.managers.SubCommand;
import com.axhislmc.bankPlugin.model.PlayerBalance;
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
        List<PlayerBalance> balanceTopList = plugin.getEconomyManager().getTopBalances();

        int limit = Math.min(10, balanceTopList.size());
        sender.sendRichMessage("<grey><bold>-- <yellow>Top " + limit + " richest Players</yellow> --");

        int idx = 1;
        for (PlayerBalance playerBalance : balanceTopList) {
            String playerName = plugin.getServer().getOfflinePlayer(playerBalance.uuid()).getName();
            if (playerName == null) playerName = "Unknown";

            sender.sendRichMessage(String.format("<grey>%d. <white>%s<white>: <green>%.2f$",
                    idx, playerName, playerBalance.balance()));
            idx++;
        }
    }
}
