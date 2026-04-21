package com.axhislmc.bankPlugin.utils;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Message {

    // ERRORS
    NOT_A_PLAYER("<red>Must be a Player to run this command.", Sound.ENTITY_VILLAGER_NO),
    PLAYER_DOESNT_EXIST("<red>Can't show Balance for %value%.", Sound.ENTITY_VILLAGER_NO),
    NO_PERMISSION("<red>You have no permission to run this command.", Sound.ENTITY_VILLAGER_NO),
    INVALID_COMMAND("<red>Invalid command. Use /bank help for help.", Sound.ENTITY_VILLAGER_NO),
    NO_AMOUNT_GIVEN("<red>Please enter an amount to pay.", Sound.ENTITY_VILLAGER_NO),
    NOT_ENOUGH_MONEY("<red>You don't have enough money to pay.", Sound.ENTITY_VILLAGER_NO),

    // INFO
    BALANCE_INFO("<grey>Your balance is: <green>%value%$", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    OTHER_BALANCE_INFO("<grey>Player balance is: <green>%value%$", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    TRANSFERRED_MONEY("<grey>Successfully transferred <green>%value%$ <grey>to Player", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    SET_BALANCE("<grey>Successfully set Player balance to <green>%value%$.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP);

    private final String text;
    private final Sound sound;

    Message(String text, Sound sound) {
        this.text = text;
        this.sound = sound;
    }

    public void send(CommandSender sender, String... replacements) {
        String finalText = text;
        if (replacements.length > 0) {
            finalText = text.replace("%value%", replacements[0]);
        }
        sender.sendRichMessage(finalText);

        if (sender instanceof Player player) {
            player.playSound(player.getLocation(), sound, 0.5f, 1f);
        }
    }
}
