package com.axhislmc.bankPlugin.config;

import org.bukkit.Sound;

public enum MessageType implements ConfigEntry {
    /*
    <target> -> The other Player, Target
    <amount> -> Referred to the Money, Balance
    <player> -> The Player self, the Sender
     */

    // PREFIX
    PREFIX("Messages.Prefix", "<dark_green>[Bank]</dark_green>", null),

    // ERRORS
    NOT_A_PLAYER("Messages.Errors.Command-Only-For-Players", "<red>Must be a Player to run this command.", Sound.ENTITY_VILLAGER_NO),
    PLAYER_DOESNT_EXIST("Messages.Errors.Player-Doesnt-Exist", "<red>Can't show Balance for <target>.", Sound.ENTITY_VILLAGER_NO),
    NO_PERMISSION("Messages.Errors.No-Permission", "<red>You have no permission to run this command.", Sound.ENTITY_VILLAGER_NO),
    INVALID_COMMAND("Messages.Errors.Invalid-Command", "<red>Invalid command. Use /bank help for help.", Sound.ENTITY_VILLAGER_NO),
    NO_AMOUNT_GIVEN("Messages.Errors.No-Amount-Given", "<red>Please enter an amount to pay.", Sound.ENTITY_VILLAGER_NO),
    NOT_ENOUGH_MONEY("Messages.Errors.Not-Enough-Money", "<red>You don't have enough money to pay <amount>$.", Sound.ENTITY_VILLAGER_NO),
    AMOUNT_IS_NEGATIVE("Messages.Errors.Amount-Is-Negative", "<red>Amount can't be negative.", Sound.ENTITY_VILLAGER_NO),
    SELF_PAY("Messages.Errors.Self-Pay", "<red>You can't pay yourself.", Sound.ENTITY_VILLAGER_NO),
    PAY_MINIMUM("Messages.Errors.Min-Pay", "<red>You need to pay at least 0.01$.", Sound.ENTITY_VILLAGER_NO),
    PAY_MAXIMUM("Messages.Errors.Max-Pay", "<red>You can only pay upto 1B$.", Sound.ENTITY_VILLAGER_NO),

    // INFO
    SHOW_BALANCE("Messages.Info.Show-Balance", "<grey>Your balance is: <green><amount>$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    SHOW_OTHERS_BALANCE("Messages.Info.Show-Others-Balance", "<grey><target> balance is: <green><amount>$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    MONEY_TRANSFERRED("Messages.Info.Money-Transferred", "<grey>Payed <green><amount>$</green> to <target>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    MONEY_RECEIVED("Messages.Info.Money-Received", "<grey>You received <amount>$ from <player>!", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    SET_BALANCE("Messages.Info.Set-Balance", "<grey>Successfully set <target> balance to <green><amount>$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    TARGET_SET_BALANCE("Messages.Info.Target-Set-Balance", "<grey>Your balance was set to <green><amount>$</green>.", Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    ;

    private final String path;
    private final String defaultMsg;
    private final Sound sound;

    MessageType(String path, String defaultMsg, Sound sound) {
        this.path = path;
        this.defaultMsg = defaultMsg;
        this.sound = sound;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultMsg;
    }

    public Sound getSound() {
        return this.sound;
    }
}
