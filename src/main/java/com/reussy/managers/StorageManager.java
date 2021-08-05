package com.reussy.managers;

import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public class StorageManager {

    private final ExodusHomes plugin;
    UUID uuid;
    File playerFile;
    FileConfiguration playerYaml;

    /**
     * @param uuid   uuid for the file
     * @param plugin main class
     */
    public StorageManager(UUID uuid, ExodusHomes plugin) {

        this.plugin = plugin;
        playerFile = new File(plugin.getDataFolder() + File.separator + "storage" + File.separator + uuid + ".yml");
        playerYaml = YamlConfiguration.loadConfiguration(playerFile);
    }

    public void createPlayerFile(Player player) { //Create a file for each player

        if (!playerFile.exists()) {

            new BukkitRunnable() {
                @Override
                public void run() {

                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCreating new file in storage for &e" + player.getName()));
                    try {
                        playerFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    playerYaml.set("Information" + ".UUID", player.getUniqueId().toString());
                    playerYaml.set("Information" + ".PlayerName", player.getName());
                    playerYaml.createSection("Homes");
                    try {
                        playerYaml.save(playerFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccess!"));
                }
            }.runTaskAsynchronously(plugin);
        }
    }

    public FileConfiguration getFile() {

        if (playerFile == null)
            reloadFile();
        return playerYaml;
    }

    public void reloadFile() {

        try {
            playerYaml = YamlConfiguration.loadConfiguration(playerFile);
            Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource(File.separator + "storage" + File.separator + uuid + ".yml")), StandardCharsets.UTF_8);
            YamlConfiguration defStream = YamlConfiguration.loadConfiguration(defConfigStream);
            playerYaml.setDefaults(defStream);
            defConfigStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {

        try {
            playerYaml.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}