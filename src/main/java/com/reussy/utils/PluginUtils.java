package com.reussy.utils;

import com.cryptomorin.xseries.XSound;
import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PluginUtils {

    private final ExodusHomes plugin;

    /**
     * @param plugin main class
     */
    public PluginUtils(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    public void sendMessageWithPrefix(CommandSender player, String text) {
        String messagePrefix = this.setHexColor(plugin.getConfig().getString("Plugin-Prefix"));
        try {
            player.sendMessage(this.setHexColor(messagePrefix + text));
        } catch (NullPointerException e) {
            e.getCause();
        }
    }

    public void sendMessageWithoutPrefix(CommandSender player, String text) {
        try {
            player.sendMessage(this.setHexColor(text));
        } catch (NullPointerException e) {
            e.getCause();
        }
    }

    public void sendSound(Player player, Location location, String xSound, float volume, float pitch) {
        if (XSound.valueOf(xSound).isSupported()) {
            player.playSound(location, Objects.requireNonNull(XSound.valueOf(xSound).parseSound()), volume, pitch);
        } else {
            Bukkit.getConsoleSender().sendMessage(this.setHexColor("&4[ExodusHomesDEBUG] &e" + xSound + " &cis invalid sound for your server version!"));
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

    public boolean getPerm(Player player) {

        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {

            String getPermission = permissionAttachmentInfo.getPermission();

            if (player.isOp()) return true;

            if (!plugin.getConfig().getBoolean("Permissions-System")) return true;

            if (getPermission.equalsIgnoreCase("homes.limit.*")) return true;

            if (getPermission.startsWith("homes.limit.")) {

                int homesLimit = Integer.parseInt(getPermission.substring(getPermission.lastIndexOf(".") + 1));

                if (plugin.databaseManager.getHomes(player).size() < homesLimit) return true;
            }
        }
        return false;
    }
}
