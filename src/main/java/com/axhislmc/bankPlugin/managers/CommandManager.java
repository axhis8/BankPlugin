package com.axhislmc.bankPlugin.managers;

import com.axhislmc.bankPlugin.BankPlugin;
import com.axhislmc.bankPlugin.commands.BalanceSubCommand;
import com.axhislmc.bankPlugin.commands.PaySubCommand;
import com.axhislmc.bankPlugin.commands.SetBalanceSubCommand;
import com.axhislmc.bankPlugin.menus.BankMenu;
import com.axhislmc.bankPlugin.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final BankPlugin plugin;
    private final Map<String, SubCommand> subCommands = new LinkedHashMap<>();

    public CommandManager(BankPlugin plugin) {
        this.plugin = plugin;
    }

    public void setupCommands() {
        registerSubCommand(new BalanceSubCommand(plugin));
        registerSubCommand(new PaySubCommand(plugin));
        registerSubCommand(new SetBalanceSubCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            SubCommand target = subCommands.get(args[0].toLowerCase());
            if (target != null){
                target.perform(sender, args);
            } else {
                Message.INVALID_COMMAND.send(sender);
            }
        }

        else {
            if (sender instanceof Player player) {
                new BankMenu(plugin, player).open();
            } else {
                Message.NOT_A_PLAYER.send(sender);
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(subCommands.keySet());
        } else if (args.length > 1) {
            SubCommand target = subCommands.get(args[0].toLowerCase());
            if (target != null) {
                return target.getSubCommandArgs(sender, args);
            }
        }
        return Collections.emptyList();
    }

    private void registerSubCommand(SubCommand sub) {
        subCommands.put(sub.getCommand(), sub);
    }
}
