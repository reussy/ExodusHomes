package com.reussy.databases.yaml;

import com.cryptomorin.xseries.XSound;
import com.reussy.ExodusHomes;
import com.reussy.databases.DatabaseManager;
import com.reussy.managers.StorageManager;
import com.reussy.utils.TeleportTask;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Yaml implements DatabaseManager {

    private final ExodusHomes plugin;

    /**
     * @param plugin main class
     */
    public Yaml(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasHome(OfflinePlayer player) {
        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        if (section == null) return false;
        Set<String> key = section.getKeys(false);
        return !key.isEmpty();
    }

    @Override
    public void createHome(Player player, String home) {


        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        storageManager.getFile().set("Homes." + home + ".World", player.getWorld().getName());
        storageManager.getFile().set("Homes." + home + ".X", player.getLocation().getBlockX());
        storageManager.getFile().set("Homes." + home + ".Y", player.getLocation().getBlockY());
        storageManager.getFile().set("Homes." + home + ".Z", player.getLocation().getBlockZ());
        storageManager.getFile().set("Homes." + home + ".Pitch", player.getLocation().getPitch());
        storageManager.getFile().set("Homes." + home + ".Yaw", player.getLocation().getYaw());
        storageManager.saveFile();

        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getMessage("Home-Created").replace("%home_name%", home));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Create-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteHome(Player player, String home) {


        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);

        storageManager.getFile().set("Homes." + home, null);
        storageManager.getFile().set(home, null);
        storageManager.saveFile();
        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getMessage("Home-Deleted").replace("%home_name%", home));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteHomeByAdmin(OfflinePlayer offlinePlayer, CommandSender sender, String home) {


        StorageManager storageManager = new StorageManager(offlinePlayer.getUniqueId(), plugin);

        storageManager.getFile().set("Homes." + home, null);
        storageManager.getFile().set(home, null);
        storageManager.saveFile();
        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getMessage("Manage.Homes-Admin-Delete").replace("%home_name%", home).replace("%target%", offlinePlayer.getName()));
        plugin.pluginUtils.sendSound(((Player) sender), ((Player) sender).getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

    }

    @Override
    public void deleteAll(Player player) {

        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");

        assert section != null;
        for (String home : section.getKeys(false)) {

            storageManager.getFile().set("Homes." + home, null);
            storageManager.getFile().set(home, null);
            storageManager.saveFile();
        }
        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getMessage("Homes-Deleted"));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
    }

    @Override
    public void deleteAllByAdmin(OfflinePlayer offlinePlayer, CommandSender sender) {


        StorageManager storageManager = new StorageManager(offlinePlayer.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");

        assert section != null;
        for (String home : section.getKeys(false)) {

            storageManager.getFile().set("Homes." + home, null);
            storageManager.getFile().set(home, null);
            storageManager.saveFile();
        }
        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getMessage("Manage.Homes-Admin-Delete").replace("%target%", offlinePlayer.getName()));
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

        Location homeLocation = new Location(Bukkit.getWorld(this.getWorld(offlinePlayer, home)), this.getX(offlinePlayer, home), this.getY(offlinePlayer, home), this.getZ(offlinePlayer, home), this.getYaw(offlinePlayer, home), this.getPitch(offlinePlayer, home));
        homeLocation.add(0.5D, 0.0D, 0.5D);
        ((Player) sender).teleport(homeLocation);
        plugin.pluginUtils.sendMessageWithPrefix(sender, Objects.requireNonNull(plugin.fileManager.getLang().getString("Manage.Home-Teleport"))
                .replace("%home_name%", home).replace("%target%", offlinePlayer.getName()));
    }

    @Override
    public void listHomes(Player player) {
        List<String> getHomes = (this.getHomes(player));

        for (String homeList : getHomes)
            player.sendMessage(plugin.pluginUtils.setHexColor(plugin.fileManager.getMessage("Homes-Format").replace("%home_name%", homeList)));

    }

    @Override
    public void listHomesByAdmin(OfflinePlayer offlinePlayer, CommandSender sender) {

        List<String> getHomes = (this.getHomes(offlinePlayer));

        for (String homeList : getHomes)
            sender.sendMessage(plugin.pluginUtils.setHexColor(plugin.fileManager.getMessage("Manage.Homes-Format").replace("%home_name%", homeList)));
    }

    @Override
    public void setNewName(Player player, String home, String name) {

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDatabase YAML doesn't support this feature!"));
        assert XSound.BLOCK_ANVIL_FALL.parseSound() != null;
        player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_FALL.parseSound(), 0.5F, 0.5F);
    }

    @Override
    public String getPlayer(String offlinePlayer) {

        return null;
    }

    @Override
    public UUID getUUID(String offlinePlayer) {
        return null;
    }

    @Override
    public String getWorld(OfflinePlayer offlinePlayer, String home) {
        StorageManager storageManager = new StorageManager(offlinePlayer.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getString(home + ".World");
    }

    @Override
    public double getX(OfflinePlayer offlinePlayer, String home) {
        StorageManager storageManager = new StorageManager(offlinePlayer.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getInt(home + ".X");
    }

    @Override
    public double getY(OfflinePlayer offlinePlayer, String home) {
        StorageManager storageManager = new StorageManager(offlinePlayer.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getInt(home + ".Y");
    }

    @Override
    public double getZ(OfflinePlayer offlinePlayer, String home) {
        StorageManager storageManager = new StorageManager(offlinePlayer.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getInt(home + ".Z");
    }

    @Override
    public float getPitch(OfflinePlayer offlinePlayer, String home) {
        StorageManager storageManager = new StorageManager(offlinePlayer.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getInt(home + ".Pitch");
    }

    @Override
    public float getYaw(OfflinePlayer offlinePlayer, String home) {
        StorageManager storageManager = new StorageManager(offlinePlayer.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getInt(home + ".Yaw");
    }

    @Override
    public List<String> getHomes(@Nullable OfflinePlayer offlinePlayer) {
        StorageManager storageManager = new StorageManager(offlinePlayer.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");

        if (section == null) {
            return null;
        } else {

            return new ArrayList<>(section.getKeys(false));
        }
    }
}
