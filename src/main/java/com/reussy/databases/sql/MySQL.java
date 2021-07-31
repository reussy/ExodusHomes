package com.reussy.databases.sql;

import com.reussy.ExodusHomes;
import com.reussy.databases.DatabaseManager;
import com.reussy.utils.TeleportTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class MySQL implements DatabaseManager {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);

    @Override
    public boolean hasHome(OfflinePlayer offlinePlayer) {

        return plugin.sqlManager.hasHomes(offlinePlayer);
    }

    @Override
    public void createHome(Player player, String home) {

        String world = player.getWorld().getName();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        float pitch = player.getLocation().getPitch();
        float yaw = player.getLocation().getYaw();

        plugin.sqlManager.createHomes(player.getUniqueId(), player, world, home, x, y, z, pitch, yaw);
        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getMessage("Home-Created").replace("%home_name%", home));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Create-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public void deleteHome(Player player, String home) {

        plugin.sqlManager.deleteHomes(player.getUniqueId(), home);
        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getMessage("Home-Deleted").replace("%home_name%", home));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteHomeByAdmin(OfflinePlayer player, CommandSender sender, String home) {

        plugin.sqlManager.deleteHomes(player.getUniqueId(), home);
        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getMessage("Manage.Home-Admin-Deleted").replace("%home_name%", home)
                .replace("%target%", player.getName()));
        plugin.pluginUtils.sendSound(((Player) sender), ((Player) sender).getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteAll(Player player) {


        plugin.sqlManager.deleteAll(player.getUniqueId());
        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getMessage("Homes-Deleted"));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public void deleteAllByAdmin(OfflinePlayer offlinePlayer, CommandSender sender) {


        plugin.sqlManager.deleteAll(offlinePlayer.getUniqueId());
        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getMessage("Manage.Homes-Admin-Deleted").replace("%target%", offlinePlayer.getName()));
        plugin.pluginUtils.sendSound(((Player) sender), ((Player) sender).getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void goHome(Player player, String home) {


        if (plugin.getConfig().getBoolean("World-System.Enabled") && plugin.getConfig().getBoolean("World-System.Per-World-Home")) {

            String worldName = plugin.getDatabaseManager().getWorld(player, home);
            World worldHome = Bukkit.getWorld(worldName);
            World playerHome = player.getWorld();

            if (playerHome != worldHome) {

                plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getMessage("Not-Same-World").replace("%home_name%", home));
                return;
            }
        }

        Location homeLocation = new Location(Bukkit.getWorld(this.getWorld(player, home)), this.getX(player, home), this.getY(player, home), this.getZ(player, home), this.getYaw(player, home), this.getPitch(player, home));
        homeLocation.add(0.5D, 0.0D, 0.5D);
        int time = plugin.getConfig().getInt("Teleport-Delay.Time");
        TeleportTask teleportTask = new TeleportTask(plugin, time, player, homeLocation, home);
        teleportTask.runTask();

    }

    @Override
    public void goHomeByAdmin(OfflinePlayer offlinePlayer, CommandSender sender, String home) {


        Location Home = new Location(Bukkit.getWorld(this.getWorld(offlinePlayer, home)), this.getX(offlinePlayer, home), this.getY(offlinePlayer, home), this.getZ(offlinePlayer, home), this.getYaw(offlinePlayer, home), this.getPitch(offlinePlayer, home));
        Home.add(0.5D, 0.0D, 0.5D);
        ((Player) sender).teleport(Home);
        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getMessage("Manage.Home-Teleport")
                .replace("%home_name%", home)
                .replace("%target%", offlinePlayer.getName()));
    }

    @Override
    public void listHomes(Player player) {


        for (String homeList : this.getHomes(player)) {
            player.sendMessage(plugin.pluginUtils.setHexColor(plugin.fileManager.getMessage("Homes-Format").replace("%home_name%", homeList)));
        }
    }

    @Override
    public void listHomesByAdmin(OfflinePlayer offlinePlayer, CommandSender sender) {

        List<String> getHomes = (plugin.sqlManager.getHomes(offlinePlayer));


        for (String homeList : getHomes) {
            sender.sendMessage(plugin.pluginUtils.setHexColor(plugin.fileManager.getMessage("Manage.Homes-Format").replace("%home_name%", homeList)));

        }
    }

    @Override
    public void setNewName(Player player, String home, String name) {


        plugin.sqlManager.setNewName(player.getUniqueId(), home, name);
        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getMessage("Home-Renamed").replace("%old_name%", home).replace("%new_name%", name));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Renamed-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public String getPlayer(String offlinePlayer) {

        return plugin.sqlManager.getPlayer(offlinePlayer);
    }

    @Override
    public UUID getUUID(String offlinePlayer) {

        return plugin.sqlManager.getUUID(offlinePlayer);
    }

    @Override
    public String getWorld(OfflinePlayer offlinePlayer, String home) {
        return plugin.sqlManager.getWorld(offlinePlayer, home);
    }

    @Override
    public double getX(OfflinePlayer offlinePlayer, String home) {
        return plugin.sqlManager.getX(offlinePlayer, home);
    }

    @Override
    public double getY(OfflinePlayer offlinePlayer, String home) {
        return plugin.sqlManager.getY(offlinePlayer, home);
    }

    @Override
    public double getZ(OfflinePlayer offlinePlayer, String home) {
        return plugin.sqlManager.getZ(offlinePlayer, home);
    }

    @Override
    public float getPitch(OfflinePlayer offlinePlayer, String home) {
        return plugin.sqlManager.getPitch(offlinePlayer, home);
    }

    @Override
    public float getYaw(OfflinePlayer offlinePlayer, String home) {
        return plugin.sqlManager.getYaw(offlinePlayer, home);
    }

    @Override
    public List<String> getHomes(@Nullable OfflinePlayer offlinePlayer) {

        return plugin.sqlManager.getHomes(offlinePlayer);
    }
}
