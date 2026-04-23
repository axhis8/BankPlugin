package com.axhislmc.bankPlugin.config;

import org.bukkit.Sound;

public enum MessageType {
    /*
    <target> -> The other Player, Target
    <amount> -> Referred to the Money, Balance
    <player> -> The Player self, the Sender
     */

    // ERRORS
    NOT_A_PLAYER("Command-Only-For-Players", "<red>Must be a Player to run this command.", Sound.ENTITY_VILLAGER_NO),
    PLAYER_DOESNT_EXIST("Player-Doesnt-Exist", "<red>Can't show Balance for <target>.", Sound.ENTITY_VILLAGER_NO),
    NO_PERMISSION("No-Permission", "<red>You have no permission to run this command.", Sound.ENTITY_VILLAGER_NO),
    INVALID_COMMAND("Invalid-Command", "<red>Invalid command. Use /bank help for help.", Sound.ENTITY_VILLAGER_NO),
    NO_AMOUNT_GIVEN("No-Amount-Given", "<red>Please enter an amount to pay.", Sound.ENTITY_VILLAGER_NO),
    NOT_ENOUGH_MONEY("Not-Enough-Money", "<red>You don't have enough money to pay <amount>$.", Sound.ENTITY_VILLAGER_NO),
    AMOUNT_IS_NEGATIVE("Amount-Is-Negative", "<red>Amount can't be negative.", Sound.ENTITY_VILLAGER_NO),
    SELF_PAY("Self-Pay", "<red>You can't pay yourself.", Sound.ENTITY_VILLAGER_NO),

    // INFO
    SHOW_BALANCE("Show-Balance", "<grey>Your balance is: <green><amount>$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    SHOW_OTHERS_BALANCE("Show-Others-Balance", "<grey><target> balance is: <green><amount>$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    MONEY_TRANSFERRED("Money-Transferred", "<grey>Successfully transferred <green><amount>$</green> to <target>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    MONEY_RECEIVED("Money-Received", "<grey>You received <amount>$ from <player>!", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    SET_BALANCE("Set-Balance", "<grey>Successfully set <target> balance to <green><amount>$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    TARGET_SET_BALANCE("Target-Set-Balance", "<grey>Your balance was set to <green><amount>$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
    ;

    private final String path;
    private final String defaultMsg;
    private final Sound sound;

    MessageType(String path, String defaultMsg, Sound sound) {
        this.path = path;
        this.defaultMsg = defaultMsg;
        this.sound = sound;
    }

    public String getPath() {
        return this.path;
    }

    public String getDefaultMsg() {
        return this.defaultMsg;
    }

    public Sound getSound() {
        return this.sound;
    }
}
