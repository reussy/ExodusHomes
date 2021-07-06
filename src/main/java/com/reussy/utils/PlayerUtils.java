package com.reussy.utils;

import com.cryptomorin.xseries.XSound;
import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class PlayerUtils {

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

    public void sendTitle(Player player, String title, int fadeIn, int stay, int fadeOut, ChatColor chatColor) {

        try {

            Object chatTitle = getNMS("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\",color:" + chatColor.name().toLowerCase() + "}");

            Constructor<?> titleConstructor = getNMS("PacketPlayOutTitle").getConstructor(getNMS("PacketPlayOutTitle").getDeclaredClasses()[0], getNMS("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(getNMS("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle, fadeIn, stay, fadeOut);

            sendPacket(player, packet);

        } catch (Exception e) {
            e.getCause();
        }
    }

    private void sendPacket(Player player, Object packet) {

        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMS("Packet")).invoke(playerConnection, packet);
        } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
            e.getCause();
        }
    }

    private Class<?> getNMS(String version) {

        try {

            return Class.forName("net.minecraft.server" + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + version);
        } catch (ClassNotFoundException e) {
            e.getCause();
        }
        return null;
    }
}
