package com.reussy.utils;

import com.cryptomorin.xseries.XSound;
import com.reussy.ExodusHomes;
import com.reussy.managers.DatabaseManager;
import com.reussy.managers.FileManager;
import com.reussy.mysql.MySQLData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MySQL implements DatabaseManager {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
    MySQLData mySQLData = new MySQLData();
    FileManager fileManager = new FileManager();
    MessageUtils messageUtils = new MessageUtils();

    @Override
    public boolean hasHome(@Nullable Player player) {

        assert player != null;
        return mySQLData.hasHomes(plugin.getConnection(), player.getUniqueId());
    }

    @Override
    public void createHome(Player player, String home) {

        String world = player.getWorld().getName();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        float pitch = player.getLocation().getPitch();
        float yaw = player.getLocation().getYaw();

        mySQLData.createHomes(plugin.getConnection(), player.getUniqueId(), player, world, home, x, y, z, pitch, yaw);
        messageUtils.sendMessage(player, fileManager.getMessage("Home-Created").replace("%home_name%", home));
        player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Create-Home")).parseSound(), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public void deleteHome(Player player, String home) {

        mySQLData.deleteHomes(plugin.getConnection(), player.getUniqueId(), home);
        messageUtils.sendMessage(player, fileManager.getMessage("Home-Deleted").replace("%home_name%", home));
        player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteHomeByAdmin(Player player, CommandSender sender, String home) {

        mySQLData.deleteHomes(plugin.getConnection(), player.getUniqueId(), home);
        messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Home-Admin-Deleted").replace("%home_name%", home)
                .replace("%target%", player.getName()));
        ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteAll(Player player) {

        mySQLData.deleteAll(plugin.getConnection(), player.getUniqueId());
        messageUtils.sendMessage(player, fileManager.getMessage("Homes-Deleted"));
        player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public void deleteAllByAdmin(Player player, CommandSender sender) {

        mySQLData.deleteAll(plugin.getConnection(), player.getUniqueId());
        messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Homes-Admin-Deleted").replace("%target%", player.getName()));
        ((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void goHome(Player player, String home) {

        Location homeLocation = new Location(Bukkit.getWorld(this.getWorld(player, home)), this.getX(player, home), this.getY(player, home), this.getZ(player, home), this.getYaw(player, home), this.getPitch(player, home));
        homeLocation.add(0.5D, 0.0D, 0.5D);
        int time = plugin.getConfig().getInt("Teleport-Delay.Time");
        TeleportTask teleportTask = new TeleportTask(plugin, time, player, homeLocation, home);
        teleportTask.runTask();

    }

    @Override
    public void goHomeByAdmin(Player player, CommandSender sender, String home) {

        Location Home = new Location(Bukkit.getWorld(this.getWorld(player, home)), this.getX(player, home), this.getY(player, home), this.getZ(player, home), this.getYaw(player, home), this.getPitch(player, home));
        Home.add(0.5D, 0.0D, 0.5D);
        player.teleport(Home);
        messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Home-Teleport")
                .replace("%home_name%", home)
                .replace("%target%", player.getName()));
    }

    @Override
    public void listHomes(Player player) {

        for (String homeList : this.getHomes(player)) {
            player.sendMessage(plugin.setHexColor(fileManager.getMessage("Homes-Format").replace("%home_name%", homeList)));
        }
    }

    @Override
    public void listHomesByAdmin(Player player, CommandSender sender) {

        List<String> getHomes = (mySQLData.getHomes(plugin.getConnection(), player.getUniqueId()));

        for (String homeList : getHomes) {
            sender.sendMessage(plugin.setHexColor(fileManager.getMessage("Manage.Homes-Format").replace("%home_name%", homeList)));

        }
    }

    @Override
    public void setNewName(Player player, String home, String name) {

        mySQLData.setNewName(plugin.getConnection(), player.getUniqueId(), home, name);
        messageUtils.sendMessage(player, fileManager.getMessage("Home-Renamed").replace("%old_name%", home).replace("%new_name%", name));
        player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Renamed-Home")).parseSound(), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
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
