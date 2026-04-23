package com.axhislmc.bankPlugin.config;

import com.axhislmc.bankPlugin.BankPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Messages {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    private final BankPlugin plugin;
    private final FileConfiguration config;
    private Component prefix = Component.empty();

    public Messages(BankPlugin plugin, File file) {
        this.plugin = plugin;
        this.config = YamlConfiguration.loadConfiguration(file);

        // Set Defaults if User leaves config empty
        this.config.addDefault("Prefix", "<dark_green>[Bank]</dark_green>");
        for (MessageType type : MessageType.values()) {
            this.config.addDefault(type.getPath(), type.getDefaultMsg());
        }
        this.config.options().copyDefaults(true);

        this.config.options().setHeader(List.of(
                "This configuration file supports MiniMessage for formatting strings.",
                "More about MiniMessage at:",
                "https://docs.papermc.io/adventure/minimessage/format/",
                " ",
                "You can check live your MiniMessage at:",
                "https://webui.advntr.dev/"
        ));

        try {
            this.config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final String prefix = this.config.getString("Prefix");
        if (prefix != null && !prefix.isEmpty()) {
            this.prefix = mm.deserialize(prefix.trim());
        }
    }

    public void send(CommandSender sender, MessageType messageType) {
        send(sender, messageType, TagResolver.empty());
    }

    public void send(CommandSender sender, MessageType messageType, TagResolver... placeholders) {
        // Load message from config
        final String messageString = this.config.getString(messageType.getPath());
        if (messageString == null) {
            plugin.getLogger().warning("Message path '" + messageType.getPath() + "' is missing in messages.yml! Using default.");
            sender.sendMessage(prefix.appendSpace().append(mm.deserialize(messageType.getDefaultMsg(), placeholders)));
            return;
        }

        Component text = prefix.appendSpace().append(mm.deserialize(messageString, placeholders));
        sender.sendMessage(text);

        if (sender instanceof Player player) {
            final Sound messageSound = messageType.getSound();
            player.playSound(player.getLocation(), messageSound, 0.5f, 1f);
        }
    }
}
