package com.axhislmc.bankPlugin.config;

public enum SettingsType implements ConfigEntry {
    STARTING_BALANCE("Settings.Starting-Balance", 1000.0),
    TOP_LIST_UPDATE_INTERVAL_MINUTES("Settings.Top-List-Update-Interval-Minutes", 5),
    AMOUNT_SHOW_TOP_PLAYERS("Settings.Amount-To-Show-Top-Players", 10),
    MAX_AMOUNT_PAY("Settings.Max-Amount-Able-To-Pay", 1000000000),
    MIN_AMOUNT_PAY("Settings.Min-Amount-Able-To-Pay", 0.1),
    ;

    private final String path;
    private final Object defaultValue;

    SettingsType(String path, Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }
}
