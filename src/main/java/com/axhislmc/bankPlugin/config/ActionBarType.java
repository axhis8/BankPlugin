package com.axhislmc.bankPlugin.config;

public enum ActionBarType implements ConfigEntry {
    SHOW_BALANCE_UPON_JOIN("ActionBar.Show-Balance-Upon-Join", true),
    TEXT("ActionBar.Text", "<gray>Your Balance: <green><amount>$"),
    SHOW_UPON_JOINING_DELAY_SECONDS("ActionBar.Show-Upon-Joining-Delay-Seconds", 1),
    ;

    private final String path;
    private final Object defaultValue;

    ActionBarType(String path, Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }
}
