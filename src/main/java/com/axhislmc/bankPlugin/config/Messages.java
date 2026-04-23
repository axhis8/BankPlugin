package com.axhislmc.bankPlugin.config;

import com.axhislmc.bankPlugin.BankPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    private final Config config;
    private final Component prefix;

    public Messages(BankPlugin plugin) {
        this.config = plugin.getBankConfig();

        final String prefix = this.config.getString(MessageType.PREFIX);
        this.prefix = mm.deserialize(prefix).appendSpace();
    }

    public void send(CommandSender sender, MessageType messageType) {
        send(sender, messageType, TagResolver.empty());
    }

    public void send(CommandSender sender, MessageType messageType, TagResolver... placeholders) {
        // Load message from config
        final String messageString = this.config.getString(messageType);

        Component text = prefix.append(mm.deserialize(messageString, placeholders));
        sender.sendMessage(text);

        if (sender instanceof Player player) {
            final Sound messageSound = messageType.getSound();
            player.playSound(player.getLocation(), messageSound, 0.5f, 1f);
        }
    }
}
