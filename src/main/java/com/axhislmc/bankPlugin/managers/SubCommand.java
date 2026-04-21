package com.axhislmc.bankPlugin.managers;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    String getCommand();
    String getDescription();

    List<String> getSubCommandArgs(CommandSender sender, String[] args);

    void perform(CommandSender sender, String[] args);
}
