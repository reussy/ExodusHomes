package com.reussy.managers.yaml;

import com.cryptomorin.xseries.XSound;
import com.reussy.ExodusHomes;
import com.reussy.managers.DatabaseManager;
import com.reussy.managers.StorageManager;
import com.reussy.utils.TeleportTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Yaml implements DatabaseManager {

    private final ExodusHomes plugin;

    /**
     * @param plugin main class
     */
    public Yaml(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasHome(Player player) {
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
    public void deleteHomeByAdmin(Player player, CommandSender sender, String home) {


        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);

        storageManager.getFile().set("Homes." + home, null);
        storageManager.getFile().set(home, null);
        storageManager.saveFile();
        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getMessage("Manage.Homes-Admin-Delete").replace("%home_name%", home).replace("%target%", player.getName()));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

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
    public void deleteAllByAdmin(Player player, CommandSender sender) {


        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");

        assert section != null;
        for (String home : section.getKeys(false)) {

            storageManager.getFile().set("Homes." + home, null);
            storageManager.getFile().set(home, null);
            storageManager.saveFile();
        }
        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getMessage("Manage.Homes-Admin-Delete").replace("%target%", player.getName()));
        plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Delete-Home"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
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
    public void goHomeByAdmin(Player player, CommandSender sender, String home) {


        Location homeLocation = new Location(Bukkit.getWorld(this.getWorld(player, home)), this.getX(player, home), this.getY(player, home), this.getZ(player, home), this.getYaw(player, home), this.getPitch(player, home));
        homeLocation.add(0.5D, 0.0D, 0.5D);
        ((Player) sender).teleport(homeLocation);
        plugin.pluginUtils.sendMessageWithPrefix(sender, Objects.requireNonNull(plugin.fileManager.getLang().getString("Manage.Home-Teleport"))
                .replace("%home_name%", home).replace("%target%", player.getName()));
    }

    @Override
    public void listHomes(Player player) {


        List<String> getHomes = (this.getHomes(player));

        for (String homeList : getHomes)
            player.sendMessage(plugin.setHexColor(plugin.fileManager.getMessage("Homes-Format").replace("%home_name%", homeList)));

    }

    @Override
    public void listHomesByAdmin(Player player, CommandSender sender) {


        List<String> getHomes = (this.getHomes(player));

        for (String homeList : getHomes)
            sender.sendMessage(plugin.setHexColor(plugin.fileManager.getMessage("Manage.Homes-Format").replace("%home_name%", homeList)));
    }

    @Override
    public void setNewName(Player player, String home, String name) {

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDatabase YAML doesn't support this feature!"));
        assert XSound.BLOCK_ANVIL_FALL.parseSound() != null;
        player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_FALL.parseSound(), 0.5F, 0.5F);

		/*
		* Old code for future update
		StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
		ConfigurationSection getSection = storageManager.getFile().getConfigurationSection("Homes");
		ConfigurationSection getHome = getSection.getConfigurationSection(home);

		getHome.set("Homes.", name);
		storageManager.saveFile();
		plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getMessage("Home-Renamed").replace("%old_name%", home).replace("%new_name%", name));
		player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Renamed-Home")).parseSound(), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
		 */
    }

    @Override
    public String getWorld(Player player, String home) {
        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getString(home + ".World");
    }

    @Override
    public double getX(Player player, String home) {
        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getInt(home + ".X");
    }

    @Override
    public double getY(Player player, String home) {
        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getInt(home + ".Y");
    }

    @Override
    public double getZ(Player player, String home) {
        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getInt(home + ".Z");
    }

    @Override
    public float getPitch(Player player, String home) {
        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getInt(home + ".Pitch");
    }

    @Override
    public float getYaw(Player player, String home) {
        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
        assert section != null;
        return section.getInt(home + ".Yaw");
    }

    @Override
    public List<String> getHomes(Player player) {
        StorageManager storageManager = new StorageManager(player.getUniqueId(), plugin);
        ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");

        if (section == null) {
            return null;
        } else {

            return new ArrayList<>(section.getKeys(false));
        }
    }
}
