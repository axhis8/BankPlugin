package com.axhislmc.bankPlugin.commands;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.managers.CommandManager;
import com.axhislmc.bankPlugin.managers.SubCommand;
import com.axhislmc.bankPlugin.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PaySubCommand implements SubCommand {
    private final BankPlugin plugin;

    public PaySubCommand(BankPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommand() {
        return "pay";
    }

    @Override
    public String getDescription() {
        return "Pays an amount to a Player with /pay <Player> <amount>";
    }

    @Override
    public List<String> getSubCommandArgs(CommandSender sender, String[] args) {

        if (args.length == 2) {
            return CommandManager.getOnlinePlayerNames(sender, false);
        }
        else if (args.length == 3) {
            List<String> moneySuggestions = List.of("100", "500", "1000", "5000");
            return StringUtil.copyPartialMatches(args[2], moneySuggestions, new ArrayList<>());
        }

        return Collections.emptyList();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            Message.NOT_A_PLAYER.send(sender);
            return;
        }

        // Checks if Amount is given
        if (args.length == 3) {

            // Checks if Player exists
            Player target = plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                Message.PLAYER_DOESNT_EXIST.send(sender, args[1]);
                return;
            }

            else if (target == player) {
                Message.SELF_PAY.send(sender);
                return;
            }

            // Checks valid Type
            try {
                double amount = Double.parseDouble(args[2]);

                // Checks valid Amount
                if (amount <= 0) {
                    Message.AMOUNT_NEGATIVE.send(sender);
                    return;
                }

                // Checks the Transfer
                if (plugin.getEconomyManager().removeMoney(player.getUniqueId(), amount)) {
                    plugin.getEconomyManager().addMoney(target.getUniqueId(), amount);
                    Message.TRANSFERRED_MONEY.send(sender, String.format("%.2f", amount), target.getName());
                    Message.RECEIVED_MONEY.send(target, String.format("%.2f", amount), sender.getName());
                } else {
                    Message.NOT_ENOUGH_MONEY.send(sender, String.format("%.2f", amount));
                }

            } catch (NumberFormatException e) {
                Message.NO_AMOUNT_GIVEN.send(sender);
            }

        } else {
            Message.INVALID_COMMAND.send(sender);
        }
    }
}
