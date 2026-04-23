package com.axhislmc.bankPlugin.config;

import com.axhislmc.bankPlugin.BankPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {
    private final FileConfiguration yaml;
    private final File file;
    private final BankPlugin plugin;

    public Config(BankPlugin plugin) {
        this.plugin = plugin;

        this.file = new File(plugin.getDataFolder(), "config.yml");
        this.yaml = YamlConfiguration.loadConfiguration(file);

        this.yaml.options().setHeader(List.of(
                "##############################",
                "#   BankPlugin by axhislmc   #",
                "##############################"
        ));

        register(SettingsType.values());
        register(ActionBarType.values());
        register(MessageType.values());

        addSettingsComments();
        addActionBarComments();
        addMessagesComments();

        this.yaml.options().copyDefaults(true);
        save();
    }

    private void save() {
        try {
            this.yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Couldn't save config.yml: " + e.getMessage());
        }
    }

    private void register(ConfigEntry[] values) {
        for (ConfigEntry entry : values) {
            if (!this.yaml.contains(entry.getPath())) {
                this.yaml.set(entry.getPath(), entry.getDefaultValue());
            }
        }
    }

    private void addSettingsComments() {
        this.yaml.setComments("Settings", List.of("General settings for the Bank System."));
    }

    private void addActionBarComments() {
        this.yaml.setComments("ActionBar", List.of("", "Settings for the Actionbar when Joining."));
    }

    private void addMessagesComments() {
        this.yaml.setComments("Messages", (List.of(
                "",
                "The Section Messages supports MiniMessage for formatting strings.",
                "More about MiniMessage at:",
                "https://docs.papermc.io/adventure/minimessage/format/",
                " ",
                "You can check live your MiniMessage at:",
                "https://webui.advntr.dev/",
                " ",
                "Placeholders for Messages are:",
                "<amount> : Referred to the Money, Balance",
                "<target> : The other Player, Target - if the Target is the same as the Player, it will replace with 'Your'",
                "<player> : The Player self, the Sender"
        )));
    }

    public String getString(ConfigEntry entry) {
        return yaml.getString(entry.getPath(), (String) entry.getDefaultValue());
    }

    public double getDouble(ConfigEntry entry) {
        double def = ((Number) entry.getDefaultValue()).doubleValue();
        return yaml.getDouble(entry.getPath(), def);
    }

    public int getInt(ConfigEntry entry) {
        int def = ((Number) entry.getDefaultValue()).intValue();
        return yaml.getInt(entry.getPath(), def);
    }

    public boolean getBoolean(ConfigEntry entry) {
        return yaml.getBoolean(entry.getPath(), (boolean) entry.getDefaultValue());
    }
}
