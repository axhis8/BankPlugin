package com.axhislmc.bankPlugin.commands;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.managers.SubCommand;
import com.axhislmc.bankPlugin.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
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
            Collection<? extends Player> playersOnServer = plugin.getServer().getOnlinePlayers();
            ArrayList<String> onlinePlayerNames = new ArrayList<>();

            for (Player p : playersOnServer) {
                onlinePlayerNames.add(p.getName());
            }
            return StringUtil.copyPartialMatches(args[1], onlinePlayerNames, new ArrayList<>());
        }

        if (args.length == 3) {
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
            // Checks if Amount is given
            if (args.length == 3) {

                // Checks if Player exists
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    Message.PLAYER_DOESNT_EXIST.send(sender, args[1]);
                    return;
                }

                // Checks valid Type
                try {
                    double amount = Double.parseDouble(args[2]);

                    // Checks valid Amount
                    if (amount < 0) {
                        return;
                    }

                    plugin.getEconomyManager().setBalance(target.getUniqueId(), amount);
                    Message.SET_BALANCE.send(sender, String.valueOf(amount));
                } catch (NumberFormatException e) {
                    Message.NO_AMOUNT_GIVEN.send(sender);
                }

            } else {
                Message.INVALID_COMMAND.send(sender);
            }
        }
    }
}
