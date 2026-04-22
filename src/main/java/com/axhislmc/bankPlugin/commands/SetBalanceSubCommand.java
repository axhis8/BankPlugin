package com.axhislmc.bankPlugin.commands;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.managers.CommandManager;
import com.axhislmc.bankPlugin.managers.SubCommand;
import com.axhislmc.bankPlugin.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetBalanceSubCommand implements SubCommand {
    private final BankPlugin plugin;

    public SetBalanceSubCommand(BankPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommand() {
        return "setbalance";
    }

    @Override
    public String getDescription() {
        return "Sets Balance for a Player.";
    }

    @Override
    public List<String> getSubCommandArgs(CommandSender sender, String[] args) {

        if (args.length == 2) {
            return CommandManager.getOnlinePlayerNames(sender, true);
        }

        else if (args.length == 3) {
            List<String> moneySuggestions = List.of("100", "500", "1000", "5000");
            return StringUtil.copyPartialMatches(args[2], moneySuggestions, new ArrayList<>());
        }

        return Collections.emptyList();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!sender.isOp()) {
            Message.NO_PERMISSION.send(sender);
        } else {

            // /bank setbalance <Player> <amount>
            if (args.length == 3) {

                // Checks if Player exists
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    Message.PLAYER_DOESNT_EXIST.send(sender, args[1]);
                    return;
                }

                executeSetBalance(sender, args[2], target);
            }

            // /bank setbalance <amount> -> for itself
            else if (args.length == 2) {
                if (sender instanceof Player) executeSetBalance(sender, args[1], null);
                else Message.NOT_A_PLAYER.send(sender);
            }

            else {
                Message.INVALID_COMMAND.send(sender);
            }
        }
    }

    private void executeSetBalance(CommandSender sender, String amountAsString, Player target) {
        // Checks valid Type
        try {
            double amount = Double.parseDouble(amountAsString);
            if (amount < 0) {
                Message.AMOUNT_NEGATIVE.send(sender);
                return;
            }

            if (target == null) {
                target = (Player) sender;
            }

            plugin.getEconomyManager().setBalance(target.getUniqueId(), amount);
            if (target != sender) {
                Message.SET_BALANCE.send(sender, target.getName(), String.format("%.2f", amount));
                Message.TARGET_SET_BALANCE.send(target, String.format("%.2f", amount));
            } else {
                Message.SET_BALANCE.send(sender, "your", String.format("%.2f", amount));
            }
        } catch (NumberFormatException e) {
            Message.NO_AMOUNT_GIVEN.send(sender);
        }
    }
}
