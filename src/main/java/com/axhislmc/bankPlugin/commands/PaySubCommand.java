package com.axhislmc.bankPlugin.commands;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.config.MessageType;
import com.axhislmc.bankPlugin.config.SettingsType;
import com.axhislmc.bankPlugin.managers.CommandManager;
import com.axhislmc.bankPlugin.managers.SubCommand;
import com.axhislmc.bankPlugin.utils.BankPermission;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
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
        return "Pays a Player with /bank pay <Player> <amount>";
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
            plugin.getMessages().send(sender, MessageType.NOT_A_PLAYER);
            return;
        }
        else if (!(sender.hasPermission(BankPermission.BANK_PAY.getPermission()))) {
            plugin.getMessages().send(sender, MessageType.NO_PERMISSION);
            return;
        }

        // Checks if Amount is given
        if (args.length == 3) {

            // Checks if Player exists
            Player target = plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                TagResolver unknownTag = Placeholder.parsed("target", args[1]);
                plugin.getMessages().send(sender, MessageType.PLAYER_DOESNT_EXIST, unknownTag);
                return;
            }

            else if (target == player) {
                plugin.getMessages().send(sender, MessageType.SELF_PAY);
                return;
            }

            // Checks valid Type
            try {
                double amount = Double.parseDouble(args[2]);

                // Checks valid Amount
                if (amount < 0) {
                    plugin.getMessages().send(sender, MessageType.AMOUNT_IS_NEGATIVE);
                    return;
                }
                else if (amount < plugin.getBankConfig().getDouble(SettingsType.MIN_AMOUNT_PAY)) {
                    plugin.getMessages().send(sender, MessageType.PAY_MINIMUM);
                    return;
                }
                else if (amount > plugin.getBankConfig().getDouble(SettingsType.MAX_AMOUNT_PAY)) {
                    plugin.getMessages().send(sender, MessageType.PAY_MAXIMUM);
                    return;
                }

                TagResolver amountTag = Placeholder.parsed("amount", String.format("%.2f", amount));

                // Checks the Transfer
                if (plugin.getEconomyManager().transferMoney(player.getUniqueId(), target.getUniqueId(), amount)) {

                    TagResolver targetTag = Placeholder.parsed("target", target.getName());
                    TagResolver playerTag = Placeholder.parsed("player", sender.getName());

                    plugin.getMessages().send(sender, MessageType.MONEY_TRANSFERRED, amountTag, targetTag);
                    plugin.getMessages().send(target, MessageType.MONEY_RECEIVED, amountTag, playerTag);

                } else {
                    plugin.getMessages().send(sender, MessageType.NOT_ENOUGH_MONEY, amountTag);
                }

            } catch (NumberFormatException e) {
                plugin.getMessages().send(sender, MessageType.NO_AMOUNT_GIVEN);
            }

        } else {
            plugin.getMessages().send(sender, MessageType.INVALID_COMMAND);
        }
    }
}
