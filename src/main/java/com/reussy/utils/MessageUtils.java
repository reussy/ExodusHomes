package com.reussy.utils;

import com.reussy.ExodusHomes;
import org.bukkit.command.CommandSender;

public class MessageUtils {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);

    public void sendMessage(CommandSender sender, String message) {
        String messagePrefix = plugin.setHexColor(plugin.getConfig().getString("Plugin-Prefix"));
        try {
            sender.sendMessage(plugin.setHexColor(messagePrefix + message));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
