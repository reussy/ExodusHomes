package com.reussy.databases;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface DatabaseManager {

    boolean hasHome(OfflinePlayer offlinePlayer);

    void createHome(Player player, String home);

    void deleteHome(Player player, String home);

    void deleteHomeByAdmin(OfflinePlayer offlinePlayer, CommandSender sender, String home);

    void deleteAll(Player offlinePlayer);

    void deleteAllByAdmin(OfflinePlayer offlinePlayer, CommandSender sender);

    void goHome(Player offlinePlayer, String home);

    void goHomeByAdmin(OfflinePlayer offlinePlayer, CommandSender sender, String home);

    void listHomes(Player offlinePlayer);

    void listHomesByAdmin(OfflinePlayer offlinePlayer, CommandSender sender);

    void setNewName(Player offlinePlayer, String home, String name);

    String getPlayer(String offlinePlayer);

    UUID getUUID(String offlinePlayerUUID);

    String getWorld(OfflinePlayer offlinePlayer, String home);

    double getX(OfflinePlayer offlinePlayer, String home);

    double getY(OfflinePlayer offlinePlayer, String home);

    double getZ(OfflinePlayer offlinePlayer, String home);

    float getPitch(OfflinePlayer offlinePlayer, String home);

    float getYaw(OfflinePlayer offlinePlayer, String home);

    List<String> getHomes(OfflinePlayer offlinePlayer);

}
