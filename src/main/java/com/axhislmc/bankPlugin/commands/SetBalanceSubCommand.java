package com.axhislmc.bankPlugin.commands;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.config.MessageType;
import com.axhislmc.bankPlugin.managers.CommandManager;
import com.axhislmc.bankPlugin.managers.SubCommand;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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
            plugin.getMessages().send(sender, MessageType.NO_PERMISSION);
        } else {

            // /bank setbalance <Player> <amount>
            if (args.length == 3) {

                // Checks if Player exists
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    TagResolver targetTag = Placeholder.parsed("target", args[1]);
                    plugin.getMessages().send(sender, MessageType.PLAYER_DOESNT_EXIST, targetTag);
                    return;
                }

                executeSetBalance(sender, args[2], target);
            }

            // /bank setbalance <amount> -> for itself
            else if (args.length == 2) {
                if (sender instanceof Player) executeSetBalance(sender, args[1], null);
                else plugin.getMessages().send(sender, MessageType.NOT_A_PLAYER);
            }

            else {
                plugin.getMessages().send(sender, MessageType.INVALID_COMMAND);
            }
        }
    }

    private void executeSetBalance(CommandSender sender, String amountAsString, Player target) {
        // Checks valid Type
        try {
            double amount = Double.parseDouble(amountAsString);
            if (amount < 0) {
                plugin.getMessages().send(sender, MessageType.AMOUNT_IS_NEGATIVE);
                return;
            }

            if (target == null) {
                target = (Player) sender;
            }

            TagResolver amountTag = Placeholder.parsed("amount", String.format("%.2f", amount));
            TagResolver targetTag = Placeholder.parsed("target", target.getName());
            TagResolver selfTag = Placeholder.parsed("target", "your");

            plugin.getEconomyManager().setBalance(target.getUniqueId(), amount);
            if (target != sender) {
                plugin.getMessages().send(sender, MessageType.SET_BALANCE, targetTag, amountTag);
                plugin.getMessages().send(sender, MessageType.TARGET_SET_BALANCE, amountTag);

            } else {
                plugin.getMessages().send(sender, MessageType.SET_BALANCE, selfTag, amountTag);
            }
        } catch (NumberFormatException e) {
            plugin.getMessages().send(sender, MessageType.NO_AMOUNT_GIVEN);
        }
    }
}
