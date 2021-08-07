package com.reussy.managers;

import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class EssentialsStorageManager {

    private final ExodusHomes plugin;
    File essentialsFolder;
    File storageFolder;
    File playerFile;
    FileConfiguration playerYaml;

    /**
     * @param plugin main class
     */
    public EssentialsStorageManager(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    boolean checksImport(UUID uuid) {

        if (!playerFile.exists()) {

            Bukkit.getConsoleSender().sendMessage("[ExodusHomes] There is no file created for " + uuid);
            Bukkit.getConsoleSender().sendMessage("[ExodusHomes] Skipping...");
            return true;
        }

        ConfigurationSection essentialsSection = getPlayerYaml(uuid).getConfigurationSection("homes");
        if (essentialsSection == null || essentialsSection.getKeys(false).isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("[ExodusHomes] There is no home to import from " + uuid);
            Bukkit.getConsoleSender().sendMessage("[ExodusHomes] Skipping...");
            return true;
        }

        return false;
    }

    public void importPerPlayer(UUID uuid) {

        if (!("YAML").equalsIgnoreCase(plugin.getConfig().getString("Database-Type"))) {

            Bukkit.getConsoleSender().sendMessage("[ExodusHomes] MySQL database not support this feature!");
            return;
        }

        StorageManager storageManager = new StorageManager(uuid, plugin);
        essentialsFolder = new File("plugins/Essentials/userdata");
        playerFile = new File(essentialsFolder + File.separator + uuid + ".yml");
        playerYaml = YamlConfiguration.loadConfiguration(playerFile);
        ConfigurationSection essentialsSection = playerYaml.getConfigurationSection("homes");

        if (checksImport(uuid)) return;

        Bukkit.getConsoleSender().sendMessage("[ExodusHomes] Starting to import Essentials into Exodus Homes for " + uuid);
        assert essentialsSection != null;
        initImport(uuid, storageManager, essentialsSection);

    }

    public void importEachPlayer() {

        if (!("YAML").equalsIgnoreCase(plugin.getConfig().getString("Database-Type"))) {

            Bukkit.getConsoleSender().sendMessage("[ExodusHomes] MySQL database not support this feature!");
            return;
        }

        essentialsFolder = new File("plugins/Essentials/userdata");
        storageFolder = new File("plugins/ExodusHomes/storage");
        File[] storageFiles = Objects.requireNonNull(storageFolder.listFiles());

        for (File storageFile : storageFiles) {

            UUID uuid = UUID.fromString(storageFile.getName().replace(".yml", ""));
            StorageManager storageManager = new StorageManager(uuid, plugin);
            playerFile = new File(essentialsFolder + File.separator + storageFile.getName());
            playerYaml = YamlConfiguration.loadConfiguration(playerFile);

            if (checksImport(uuid)) continue;

            Bukkit.getConsoleSender().sendMessage("[ExodusHomes] Starting to import Essentials Homes into Exodus Homes for " + uuid);
            ConfigurationSection essentialsSection = getPlayerYaml(uuid).getConfigurationSection("homes");
            assert essentialsSection != null;
            initImport(uuid, storageManager, essentialsSection);

        }
    }

    private void initImport(UUID uuid, StorageManager storageManager, ConfigurationSection essentialsSection) {


        assert essentialsSection != null;
        new BukkitRunnable() {
            /**
             * When an object implementing interface {@code Runnable} is used
             * to create a thread, starting the thread causes the object's
             * {@code run} method to be called in that separately executing
             * thread.
             * <p>
             * The general contract of the method {@code run} is that it may
             * take any action whatsoever.
             *
             * @see Thread#run()
             */
            @Override
            public void run() {
                for (String essentialsHome : essentialsSection.getKeys(false)) {

                    String world = essentialsSection.getString(essentialsHome + ".world");
                    double x = essentialsSection.getDouble(essentialsHome + ".x");
                    double y = essentialsSection.getDouble(essentialsHome + ".y");
                    double z = essentialsSection.getDouble(essentialsHome + ".z");
                    float yaw = essentialsSection.getInt(essentialsHome + ".yaw");
                    float pitch = essentialsSection.getInt(essentialsHome + ".pitch");

                    storageManager.getFile().set("Homes." + essentialsHome + ".World", world);
                    storageManager.getFile().set("Homes." + essentialsHome + ".X", x);
                    storageManager.getFile().set("Homes." + essentialsHome + ".Y", y);
                    storageManager.getFile().set("Homes." + essentialsHome + ".Z", z);
                    storageManager.getFile().set("Homes." + essentialsHome + ".Pitch", pitch);
                    storageManager.getFile().set("Homes." + essentialsHome + ".Yaw", yaw);
                    storageManager.saveFile();

                    if (plugin.getConfig().getBoolean("Debug.On-Import")) {
                        Bukkit.getConsoleSender().sendMessage("[ExodusHomes] Essentials Home: " + essentialsHome + " imported into " + uuid);
                    }
                }

                Bukkit.getConsoleSender().sendMessage("[ExodusHomes] Finished!");
            }
        }.runTaskAsynchronously(plugin);
    }

    public FileConfiguration getPlayerYaml(UUID uuid) {

        playerFile = new File(essentialsFolder + File.separator + uuid + ".yml");
        playerYaml = YamlConfiguration.loadConfiguration(playerFile);
        return playerYaml;
    }
}
