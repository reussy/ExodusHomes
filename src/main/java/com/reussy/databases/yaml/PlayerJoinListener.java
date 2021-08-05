package com.reussy.databases.yaml;

import com.reussy.ExodusHomes;
import com.reussy.managers.StorageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);

    @EventHandler
    public void createStorage(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);

        new BukkitRunnable() {
            @Override
            public void run() {
                storageManager.createPlayerFile(player);
            }
        }.runTaskAsynchronously(plugin);
    }
}
