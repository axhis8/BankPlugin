package com.axhislmc.bankPlugin.commands;

import com.axhislmc.bankPlugin.managers.SubCommand;
import com.axhislmc.bankPlugin.utils.BankPermission;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HelpSubCommand implements SubCommand {
    private final List<Map.Entry<String, SubCommand>> subCommands;
    private final List<SubCommand> adminCommands;

    public HelpSubCommand(Map<String, SubCommand> subCommands, List<SubCommand> adminCommands) {
        this.subCommands = new ArrayList<>(subCommands.entrySet());
        this.subCommands.sort(Map.Entry.comparingByKey());

        this.adminCommands = adminCommands;
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows this list.";
    }

    @Override
    public List<String> getSubCommandArgs(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        sender.sendRichMessage("<bold><#FFA500>---- Bank Help ----");

        for (Map.Entry<String, SubCommand> entry : subCommands) {
            String commandName = entry.getKey();
            SubCommand command = entry.getValue();

            if (adminCommands.contains(command) && !(sender.hasPermission(BankPermission.BANK_ADMIN.getPermission()))) {
                continue;
            }

            String description = command.getDescription();
            if (adminCommands.contains(command)) {
                description = "<red>[Admin]</red> " + description;
            }

            String text = String.format("<yellow><click:suggest_command:/bank %s>/bank %s</click> <dark_grey>» <white>%s",
                    commandName, commandName, description);
            sender.sendRichMessage(text);
        }

        sender.sendRichMessage("<bold><#FFA500>-----------------");
    }
}
