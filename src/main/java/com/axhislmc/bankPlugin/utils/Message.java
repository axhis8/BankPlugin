package com.axhislmc.bankPlugin.utils;

import net.kyori.adventure.text.Component;
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
    NOT_ENOUGH_MONEY("<red>You don't have enough money to pay %value%$.", Sound.ENTITY_VILLAGER_NO),
    AMOUNT_NEGATIVE("<red>Amount can't be negative.", Sound.ENTITY_VILLAGER_NO),
    SELF_PAY("<red>You can't pay yourself.", Sound.ENTITY_VILLAGER_NO),

    // INFO
    BALANCE_INFO("<grey>Your balance is: <green>%value%$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    OTHER_BALANCE_INFO("<grey>%value% balance is: <green>%value%$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    TRANSFERRED_MONEY("<grey>Successfully transferred <green>%value%$</green> to %value%.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    RECEIVED_MONEY("<grey>You received %value%$ from %value%!", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    SET_BALANCE("<grey>Successfully set %value% balance to <green>%value%$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    TARGET_SET_BALANCE("<grey>Your balance was set to <green>%value%$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),

    // LORE
    PAY_LORE("<grey>Click or type <white>/bank pay <Player> <amount></white> to pay.", null);

    private final String text;
    private final Sound sound;

    Message(String text, Sound sound) {
        this.text = text;
        this.sound = sound;
    }

    public void send(CommandSender sender, String... replacements) {
        String finalText = checkReplacements(replacements);

        sender.sendRichMessage(finalText);
        if (sender instanceof Player player) {
            player.playSound(player.getLocation(), sound, 0.5f, 1f);
        }
    }

    public Component asComponent(String... replacements) {
        String finalText = checkReplacements(replacements);

        return net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(finalText);
    }

    private String checkReplacements(String... replacements) {
        String finalText = text;
        if (replacements != null) {
            for (String replacement : replacements) {
                finalText = finalText.replaceFirst("%value%", replacement);
            }
        }
        return finalText;
    }
}
