package com.axhislmc.bankPlugin.managers;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.commands.*;
import com.axhislmc.bankPlugin.config.MessageType;
import com.axhislmc.bankPlugin.menus.BankMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandManager implements TabExecutor {
    private final BankPlugin plugin;

    private final Map<String, SubCommand> subCommands = new HashMap<>();
    private final List<SubCommand> opCommands = new ArrayList<>();

    public CommandManager(BankPlugin plugin) {
        this.plugin = plugin;
    }

    public void setupCommands() {
        registerSubCommand(new BalanceSubCommand(plugin), false);
        registerSubCommand(new PaySubCommand(plugin), false);
        registerSubCommand(new SetBalanceSubCommand(plugin), true);
        registerSubCommand(new TopSubCommand(plugin), false);
        registerSubCommand(new HelpSubCommand(subCommands, opCommands), false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            SubCommand target = subCommands.get(args[0].toLowerCase());
            if (target != null){
                target.perform(sender, args);
            } else {
                plugin.getMessages().send(sender, MessageType.INVALID_COMMAND);
            }
        }

        else {
            if (sender instanceof Player player) {
                new BankMenu(plugin, player).open();
            } else {
                plugin.getMessages().send(sender, MessageType.NOT_A_PLAYER);
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            for (SubCommand sub : subCommands.values()) {
                if (opCommands.contains(sub) && !sender.isOp()) {
                    continue;
                }
                completions.add(sub.getCommand());
            }

            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }

        else if (args.length > 1) {
            SubCommand target = subCommands.get(args[0].toLowerCase());
            if (target != null) {
                return target.getSubCommandArgs(sender, args);
            }
        }
        return Collections.emptyList();
    }

    public static List<String> getOnlinePlayerNames(CommandSender sender, boolean includeSelf) {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!includeSelf && player.equals(sender)) {
                continue;
            }
            names.add(player.getName());
        }
        return names;
    }

    private void registerSubCommand(SubCommand sub, boolean isOp) {
        subCommands.put(sub.getCommand(), sub);
        if (isOp) {
            opCommands.add(sub);
        }
    }
}
