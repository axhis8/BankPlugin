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
        return "Show your balance or type a Players name to show their balance.";
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
        return Collections.emptyList();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player player) {
                double balance = plugin.getEconomyManager().getBalance(player.getUniqueId());
                Message.BALANCE_INFO.send(sender, String.valueOf(balance));
            } else {
                Message.NOT_A_PLAYER.send(sender);
            }
        }
        else if (args.length == 2) {
            Player target = plugin.getServer().getPlayer(args[1]);
            if (target != null) {
                double balance = plugin.getEconomyManager().getBalance(target.getUniqueId());
                Message.OTHER_BALANCE_INFO.send(sender, String.valueOf(balance));
            } else {
                Message.PLAYER_DOESNT_EXIST.send(sender, args[1]);
            }
        }
    }
}
