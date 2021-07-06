package com.reussy.utils;

import com.reussy.ExodusHomes;
import com.reussy.managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class TeleportTask {

    private final ExodusHomes plugin;
    private final Player player;
    private final Location location;
    private final String home;
    int teleportTask;
    int time;
    MessageUtils messageUtils = new MessageUtils();
    PlayerUtils playerUtils = new PlayerUtils();

    public TeleportTask(ExodusHomes plugin, int time, Player player, Location location, String home) {
        this.plugin = plugin;
        this.time = time;
        this.player = player;
        this.location = location;
        this.home = home;
    }

    public void runTask() {

        FileManager fileManager = new FileManager(plugin);
        if (!plugin.playerCache.contains(player.getName())) {
            plugin.playerCache.add(player.getName());
        } else {
            playerUtils.sendMessageWithPrefix(player, fileManager.getMessage("Already-Teleporting"));
            return;
        }

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        teleportTask = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            if (time == 0) {

                if (plugin.getConfig().getBoolean("World-System.Enabled") && plugin.getConfig().getBoolean("World-System.Per-World-Home")) {

                    String worldName = plugin.getDatabaseManager().getWorld(player, home);
                    World worldHome = Bukkit.getWorld(worldName);
                    World playerHome = player.getWorld();

                    if (playerHome != worldHome) {

                        Bukkit.getScheduler().cancelTask(teleportTask);
                        playerUtils.sendMessageWithPrefix(player, fileManager.getMessage("Not-Same-World").replace("%home_name%", home));
                        return;
                    }
                }

                Bukkit.getScheduler().cancelTask(teleportTask);
                player.teleport(location);
                plugin.playerCache.remove(player.getName());
                playerUtils.sendMessageWithPrefix(player, fileManager.getMessage("Home-Teleport").replace("%home_name%", home));
                playerUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Teleport-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

            } else {

                playerUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Waiting-Teleport"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
                playerUtils.sendMessageWithPrefix(player, fileManager.getMessage("Teleport-Delay")
                        .replace("%seconds%", String.valueOf(time)));
                time--;
            }
        }, 0L, 20);
    }
}
