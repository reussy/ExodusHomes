package com.reussy.utils;

import com.reussy.ExodusHomes;
import com.reussy.managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerTeleport {

    private final ExodusHomes plugin;
    private final Player player;
    private final Location location;
    private final String home;
    int teleportTask;
    int time;

    public PlayerTeleport(ExodusHomes plugin, int time, Player player, Location location, String home) {
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
            plugin.pluginUtils.sendMessageWithPrefix(player, fileManager.getLang().getString("Already-Teleporting"));
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
                        plugin.pluginUtils.sendMessageWithPrefix(player, fileManager.getLang().getString("Not-Same-World").replace("%home_name%", home));
                        return;
                    }
                }

                Bukkit.getScheduler().cancelTask(teleportTask);
                player.teleport(location);
                plugin.playerCache.remove(player.getName());
                plugin.pluginUtils.sendMessageWithPrefix(player, fileManager.getLang().getString("Home-Teleport").replace("%home_name%", home));
                plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Teleport-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

            } else {

                plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Waiting-Teleport"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
                plugin.pluginUtils.sendMessageWithPrefix(player, fileManager.getLang().getString("Teleport-Delay")
                        .replace("%seconds%", String.valueOf(time)));
                time--;
            }
        }, 0L, 20);
    }
}
