package com.reussy.events;

import com.reussy.ExodusHomes;
import com.reussy.managers.FileManager;
import com.reussy.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PlayerCommandPreListener implements Listener {

    private final ExodusHomes plugin;
    PlayerUtils playerUtils = new PlayerUtils();

    public PlayerCommandPreListener(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreProcess(PlayerCommandPreprocessEvent e) {

        FileManager fileManager = new FileManager(plugin);
        Player player = e.getPlayer();
        String message = e.getMessage();
        boolean enabledSystemWorld = plugin.getConfig().getBoolean("World-System.Enabled");
        boolean opBypass = plugin.getConfig().getBoolean("World-System.Op-Bypass");

        if (!enabledSystemWorld) return;

        if (opBypass) return;

        if (plugin.getConfig().getString("World-System.Type").equalsIgnoreCase("BLACKLIST") || plugin.getConfig().getString("World-System.Type").equalsIgnoreCase("WHITELIST"))
            return;

        List<String> worlds = plugin.getConfig().getStringList("World-System.Worlds");

        if (plugin.getConfig().getString("World-System.Type").equalsIgnoreCase("BLACKLIST") && worlds.contains(player.getWorld().getName())) {

            for (String action : plugin.getConfig().getStringList("World-System.Actions")) {

                if (message.contains("/" + action)) {
                    playerUtils.sendMessageWithPrefix(player, fileManager.getMessage("Deny-Command-In-World").replace("%parameter%", message));
                    e.setCancelled(true);
                }
            }
        }
    }
}
