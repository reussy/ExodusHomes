package com.reussy.mysql;

import com.reussy.ExodusHomes;
import com.reussy.managers.DatabaseManager;
import com.reussy.managers.FileManager;
import com.reussy.utils.PluginUtils;
import com.reussy.utils.TeleportTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MySQL implements DatabaseManager {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
    MySQLData mySQLData = new MySQLData();
    PluginUtils pluginUtils = new PluginUtils();

    @Override
    public boolean hasHome(@Nullable Player player) {

        assert player != null;
        return mySQLData.hasHomes(plugin.getConnection(), player.getUniqueId());
    }

    @Override
    public void createHome(Player player, String home) {

        FileManager fileManager = new FileManager(plugin);
        String world = player.getWorld().getName();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        float pitch = player.getLocation().getPitch();
        float yaw = player.getLocation().getYaw();

        mySQLData.createHomes(plugin.getConnection(), player.getUniqueId(), player, world, home, x, y, z, pitch, yaw);
        pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Home-Created").replace("%home_name%", home));
        pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Create-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public void deleteHome(Player player, String home) {

        FileManager fileManager = new FileManager(plugin);
        mySQLData.deleteHomes(plugin.getConnection(), player.getUniqueId(), home);
        pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Home-Deleted").replace("%home_name%", home));
        pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteHomeByAdmin(Player player, CommandSender sender, String home) {

        FileManager fileManager = new FileManager(plugin);
        mySQLData.deleteHomes(plugin.getConnection(), player.getUniqueId(), home);
        pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Manage.Home-Admin-Deleted").replace("%home_name%", home)
                .replace("%target%", player.getName()));
        pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteAll(Player player) {

        FileManager fileManager = new FileManager(plugin);
        mySQLData.deleteAll(plugin.getConnection(), player.getUniqueId());
        pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Homes-Deleted"));
        pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public void deleteAllByAdmin(Player player, CommandSender sender) {

        FileManager fileManager = new FileManager(plugin);
        mySQLData.deleteAll(plugin.getConnection(), player.getUniqueId());
        pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Manage.Homes-Admin-Deleted").replace("%target%", player.getName()));
        pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void goHome(Player player, String home) {

        FileManager fileManager = new FileManager(plugin);
        if (plugin.getConfig().getBoolean("World-System.Enabled") && plugin.getConfig().getBoolean("World-System.Per-World-Home")) {

            String worldName = plugin.getDatabaseManager().getWorld(player, home);
            World worldHome = Bukkit.getWorld(worldName);
            World playerHome = player.getWorld();

            if (playerHome != worldHome) {

                pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Not-Same-World").replace("%home_name%", home));
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
    public void goHomeByAdmin(Player player, CommandSender sender, String home) {

        FileManager fileManager = new FileManager(plugin);
        Location Home = new Location(Bukkit.getWorld(this.getWorld(player, home)), this.getX(player, home), this.getY(player, home), this.getZ(player, home), this.getYaw(player, home), this.getPitch(player, home));
        Home.add(0.5D, 0.0D, 0.5D);
        player.teleport(Home);
        pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Manage.Home-Teleport")
                .replace("%home_name%", home)
                .replace("%target%", player.getName()));
    }

    @Override
    public void listHomes(Player player) {

        FileManager fileManager = new FileManager(plugin);
        for (String homeList : this.getHomes(player)) {
            player.sendMessage(plugin.setHexColor(fileManager.getMessage("Homes-Format").replace("%home_name%", homeList)));
        }
    }

    @Override
    public void listHomesByAdmin(Player player, CommandSender sender) {

        List<String> getHomes = (mySQLData.getHomes(plugin.getConnection(), player.getUniqueId()));
        FileManager fileManager = new FileManager(plugin);

        for (String homeList : getHomes) {
            sender.sendMessage(plugin.setHexColor(fileManager.getMessage("Manage.Homes-Format").replace("%home_name%", homeList)));

        }
    }

    @Override
    public void setNewName(Player player, String home, String name) {

        FileManager fileManager = new FileManager(plugin);
        mySQLData.setNewName(plugin.getConnection(), player.getUniqueId(), home, name);
        pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Home-Renamed").replace("%old_name%", home).replace("%new_name%", name));
        pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Renamed-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public String getWorld(Player player, String home) {
        return mySQLData.getWorld(plugin.getConnection(), player.getUniqueId(), home);
    }

    @Override
    public double getX(Player player, String home) {
        return mySQLData.getX(plugin.getConnection(), player.getUniqueId(), home);
    }

    @Override
    public double getY(Player player, String home) {
        return mySQLData.getY(plugin.getConnection(), player.getUniqueId(), home);
    }

    @Override
    public double getZ(Player player, String home) {
        return mySQLData.getZ(plugin.getConnection(), player.getUniqueId(), home);
    }

    @Override
    public float getPitch(Player player, String home) {
        return mySQLData.getPitch(plugin.getConnection(), player.getUniqueId(), home);
    }

    @Override
    public float getYaw(Player player, String home) {
        return mySQLData.getYaw(plugin.getConnection(), player.getUniqueId(), home);
    }

    @Override
    public List<String> getHomes(@Nullable Player player) {

        assert player != null;
        return mySQLData.getHomes(plugin.getConnection(), player.getUniqueId());
    }
}
