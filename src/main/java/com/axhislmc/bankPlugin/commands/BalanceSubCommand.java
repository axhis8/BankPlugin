package com.axhislmc.bankPlugin.commands;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.config.MessageType;
import com.axhislmc.bankPlugin.managers.CommandManager;
import com.axhislmc.bankPlugin.managers.SubCommand;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class BalanceSubCommand implements SubCommand {
    private final BankPlugin plugin;

    public BalanceSubCommand(BankPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommand() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Shows yours are someone else's balance.";
    }

    @Override
    public List<String> getSubCommandArgs(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return CommandManager.getOnlinePlayerNames(sender, true);
        }
        return Collections.emptyList();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player player) {
                double balance = plugin.getEconomyManager().getBalance(player.getUniqueId());

                TagResolver amountTag = Placeholder.parsed("amount", String.format("%.2f", balance));
                plugin.getMessages().send(sender, MessageType.SHOW_BALANCE, amountTag);
            } else {
                plugin.getMessages().send(sender, MessageType.NOT_A_PLAYER);
            }
        }
        else if (args.length == 2) {
            Player target = plugin.getServer().getPlayer(args[1]);
            if (target != null) {
                double balance = plugin.getEconomyManager().getBalance(target.getUniqueId());

                TagResolver amountTag = Placeholder.parsed("amount", String.format("%.2f", balance));
                TagResolver targetTag;

                if (target == sender)
                    targetTag = Placeholder.parsed("target", "Your");
                else
                    targetTag = Placeholder.parsed("target", target.getName());

                plugin.getMessages().send(sender, MessageType.SHOW_OTHERS_BALANCE, targetTag, amountTag);
            } else {
                plugin.getMessages().send(sender, MessageType.PLAYER_DOESNT_EXIST);
            }
        }
    }
}
