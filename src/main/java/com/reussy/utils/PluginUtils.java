package com.reussy.utils;

import com.cryptomorin.xseries.XSound;
import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PluginUtils {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);

    public void sendMessageWithPrefix(CommandSender player, String text) {
        String messagePrefix = plugin.setHexColor(plugin.getConfig().getString("Plugin-Prefix"));
        try {
            player.sendMessage(plugin.setHexColor(messagePrefix + text));
        } catch (NullPointerException e) {
            e.getCause();
        }
    }

    public void sendMessageWithoutPrefix(CommandSender player, String text) {
        try {
            player.sendMessage(plugin.setHexColor(text));
        } catch (NullPointerException e) {
            e.getCause();
        }
    }

    public void sendSound(Player player, Location location, String xSound, float volume, float pitch) {
        if (XSound.valueOf(xSound).isSupported()) {
            player.playSound(location, Objects.requireNonNull(XSound.valueOf(xSound).parseSound()), volume, pitch);
        } else {
            Bukkit.getConsoleSender().sendMessage(plugin.setHexColor("&4[ExodusHomesDEBUG] &e" + xSound + " &cis invalid sound for your server version!"));
        }
    }

    public String setHexColor(String text) {

        final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");

        if (Bukkit.getVersion().contains("1.16")) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {

                String setColor = text.substring(matcher.start(), matcher.end());
                text = text.replace(setColor, net.md_5.bungee.api.ChatColor.of(setColor) + "");
            }
        } else {
            return org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', text);
    }
}
