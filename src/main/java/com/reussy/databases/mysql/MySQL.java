package com.reussy.databases.mysql;

import com.reussy.ExodusHomes;
import com.reussy.databases.DatabaseManager;
import com.reussy.utils.PlayerTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MySQL implements DatabaseManager {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);

    @Override
    public boolean hasHome(OfflinePlayer offlinePlayer) {

        return plugin.mySQLQuery.hasHomes(offlinePlayer);
    }

    @Override
    public void createHome(Player player, String home) {

        String world = player.getWorld().getName();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        float pitch = player.getLocation().getPitch();
        float yaw = player.getLocation().getYaw();

        plugin.mySQLQuery.createHomes(player.getUniqueId(), player, world, home, x, y, z, pitch, yaw);
        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Home-Created").replace("%home_name%", home));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Create-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public void deleteHome(Player player, String home) {

        plugin.mySQLQuery.deleteHomes(player.getUniqueId(), home);
        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Home-Deleted").replace("%home_name%", home));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteHomeByAdmin(OfflinePlayer player, CommandSender sender, String home) {

        plugin.mySQLQuery.deleteHomes(player.getUniqueId(), home);
        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Manage.Home-Admin-Deleted").replace("%home_name%", home)
                .replace("%target%", player.getName()));
        plugin.pluginUtils.sendSound(((Player) sender), ((Player) sender).getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteAll(Player player) {

        plugin.mySQLQuery.deleteAll(player.getUniqueId());
        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Homes-Deleted"));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public void deleteAllByAdmin(OfflinePlayer offlinePlayer, CommandSender sender) {

        plugin.mySQLQuery.deleteAll(offlinePlayer.getUniqueId());
        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Manage.Homes-Admin-Deleted").replace("%target%", offlinePlayer.getName()));
        plugin.pluginUtils.sendSound(((Player) sender), ((Player) sender).getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void goHome(Player player, String home) {

        if (plugin.getConfig().getBoolean("World-System.Enabled") && plugin.getConfig().getBoolean("World-System.Per-World-Home")) {

            String worldName = plugin.getDatabaseManager().getWorld(player, home);
            World worldHome = Bukkit.getWorld(worldName);
            World playerHome = player.getWorld();

            if (playerHome != worldHome) {

                plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Not-Same-World").replace("%home_name%", home));
                return;
            }
        }

        Location homeLocation = new Location(Bukkit.getWorld(this.getWorld(player, home)), this.getX(player, home), this.getY(player, home), this.getZ(player, home), this.getYaw(player, home), this.getPitch(player, home));
        homeLocation.add(0.5D, 0.0D, 0.5D);
        int time = plugin.getConfig().getInt("Teleport-Delay.Time");
        PlayerTeleport playerTeleport = new PlayerTeleport(plugin, time, player, homeLocation, home);
        playerTeleport.runTask();

    }

    @Override
    public void goHomeByAdmin(OfflinePlayer offlinePlayer, CommandSender sender, String home) {

        Location Home = new Location(Bukkit.getWorld(this.getWorld(offlinePlayer, home)), this.getX(offlinePlayer, home), this.getY(offlinePlayer, home), this.getZ(offlinePlayer, home), this.getYaw(offlinePlayer, home), this.getPitch(offlinePlayer, home));
        Home.add(0.5D, 0.0D, 0.5D);
        ((Player) sender).teleport(Home);
        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Manage.Home-Teleport")
                .replace("%home_name%", home)
                .replace("%target%", offlinePlayer.getName()));
    }

    @Override
    public void listHomes(Player player) {

        for (String homeList : this.getHomes(player)) {
            player.sendMessage(plugin.pluginUtils.setHexColor(plugin.fileManager.getLang().getString("Homes-Format").replace("%home_name%", homeList)));
        }
    }

    @Override
    public void listHomesByAdmin(OfflinePlayer offlinePlayer, CommandSender sender) {

        List<String> getHomes = (plugin.mySQLQuery.getHomes(offlinePlayer));

        for (String homeList : getHomes) {
            sender.sendMessage(plugin.pluginUtils.setHexColor(plugin.fileManager.getLang().getString("Manage.Homes-Format").replace("%home_name%", homeList)));

        }
    }

    @Override
    public void setNewName(Player player, String home, String name) {

        plugin.mySQLQuery.setNewName(player.getUniqueId(), home, name);
        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Home-Renamed").replace("%old_name%", home).replace("%new_name%", name));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Renamed-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String offlineName, CommandSender sender) {

        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {

            try {
                if (offlinePlayer.getName().equalsIgnoreCase(offlineName)) return offlinePlayer;
            } catch (NullPointerException e) {
                plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Unknown-Player"));
            }
        }

        return null;
    }

    @Override
    public String getWorld(OfflinePlayer offlinePlayer, String home) {
        return plugin.mySQLQuery.getWorld(offlinePlayer, home);
    }

    @Override
    public double getX(OfflinePlayer offlinePlayer, String home) {
        return plugin.mySQLQuery.getX(offlinePlayer, home);
    }

    @Override
    public double getY(OfflinePlayer offlinePlayer, String home) {
        return plugin.mySQLQuery.getY(offlinePlayer, home);
    }

    @Override
    public double getZ(OfflinePlayer offlinePlayer, String home) {
        return plugin.mySQLQuery.getZ(offlinePlayer, home);
    }

    @Override
    public float getPitch(OfflinePlayer offlinePlayer, String home) {
        return plugin.mySQLQuery.getPitch(offlinePlayer, home);
    }

    @Override
    public float getYaw(OfflinePlayer offlinePlayer, String home) {
        return plugin.mySQLQuery.getYaw(offlinePlayer, home);
    }

    @Override
    public List<String> getHomes(OfflinePlayer offlinePlayer) {

        return plugin.mySQLQuery.getHomes(offlinePlayer);
    }
}
