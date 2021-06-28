package com.reussy.managers;

import com.cryptomorin.xseries.XSound;
import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class EssentialsStorageManager {

    private final ExodusHomes plugin;
    private final Player player;
    private final UUID uuid;
    File essentialsFolder;
    File storageFolder;
    File playerFile;
    FileConfiguration playerYaml;

    public EssentialsStorageManager(ExodusHomes plugin, Player player, UUID uuid) {
        this.plugin = plugin;
        this.player = player;
        this.uuid = uuid;
    }

    /*
    public EssentialsStorageManager() {

        if (Bukkit.getPluginManager().getPlugin("Essentials") == null) {

            player.sendMessage(plugin.setHexColor("&c&nEssentialsX is not installed..."));
            return;
        }

        if (!("YAML").equalsIgnoreCase(plugin.getConfig().getString("Database-Type"))) {

            player.sendMessage(plugin.setHexColor("&c&nMySQL database not support this feature!"));
            return;
        }

        if (!playerFile.exists()) {

            player.sendMessage(plugin.setHexColor("&cThere is no file created for " + player.getName()));
            return;
        }

        StorageManager storageManager = new StorageManager(uuid, plugin);
        ConfigurationSection essentialsSection = getPlayerYaml().getConfigurationSection("homes");

        if (essentialsSection.getKeys(false).isEmpty()) {

            player.sendMessage(plugin.setHexColor("&cThere is no home to import from " + player.getName()));
            return;
        }

        player.sendMessage(plugin.setHexColor("&e&oStarting to import EssentialsX into ExodusHomes..."));
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

            (player).playSound(player.getLocation(), XSound.UI_BUTTON_CLICK.parseSound(), 1.5F, 1);
        }
        player.sendMessage(plugin.setHexColor("&aImport completed successfully!"));
        player.sendMessage(plugin.setHexColor("&b&l" + essentialsSection.getKeys(false).size() + " &ahomes have been imported for " + player.getName()));
    }
     */

    boolean isEssentials() {
        return Bukkit.getPluginManager().getPlugin("Essentials") != null;
    }

    boolean databaseType() {
        return ("YAML").equalsIgnoreCase(plugin.getConfig().getString("Database-Type"));
    }

    boolean hasFile() {
        return playerFile.exists();
    }

    boolean noHomes() {
        ConfigurationSection essentialsSection = getPlayerYaml().getConfigurationSection("homes");
        assert essentialsSection != null;
        Set<String> getHome = essentialsSection.getKeys(false);
        return !getHome.isEmpty();
    }

    public void importPerPlayer() {

        essentialsFolder = new File("plugins/Essentials/userdata");
        playerFile = new File(essentialsFolder + File.separator + uuid + ".yml");
        playerYaml = YamlConfiguration.loadConfiguration(playerFile);

        if (!isEssentials()) {

            player.sendMessage(plugin.setHexColor("&cEssentials is not installed..."));
            return;
        }

        if (!databaseType()) {

            player.sendMessage(plugin.setHexColor("&cMySQL database not support this feature!"));
            return;
        }

        if (!hasFile()) {

            player.sendMessage(plugin.setHexColor("&cThere is no file created for &e" + player.getName()));
            return;
        }

        if (!noHomes()) {

            player.sendMessage(plugin.setHexColor("&cThere is no home to import from &e" + player.getName()));
            return;
        }

        StorageManager storageManager = new StorageManager(uuid, plugin);
        ConfigurationSection essentialsSection = getPlayerYaml().getConfigurationSection("homes");

        player.sendMessage(plugin.setHexColor("&7&oStarting to import &6Essentials &7&ointo &bExodusHomes &7&ofor &e" + player.getName()));
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

            assert XSound.UI_BUTTON_CLICK.parseSound() != null;
            player.playSound(player.getLocation(), XSound.UI_BUTTON_CLICK.parseSound(), 1.5F, 1);
        }

        player.sendMessage(plugin.setHexColor("&aImport completed successfully!"));
        player.sendMessage(plugin.setHexColor("&b" + essentialsSection.getKeys(false).size() + " &7homes have been imported for &e" + player.getName()));

    }

    public void importEachPlayer() {

        essentialsFolder = new File("plugins/Essentials/userdata");
        storageFolder = new File("plugins/ExodusHomes/storage");

        playerFile = new File(essentialsFolder + File.separator + uuid + ".yml");
        playerYaml = YamlConfiguration.loadConfiguration(playerFile);

        if (!isEssentials()) {

            player.sendMessage(plugin.setHexColor("&cEssentials is not installed..."));
            return;
        }

        if (!databaseType()) {

            player.sendMessage(plugin.setHexColor("&cMySQL database not support this feature!"));
            return;
        }

        if (!hasFile()) {

            player.sendMessage(plugin.setHexColor("&cThere is no file created for &e" + player.getName()));
            return;
        }

        if (!noHomes()) {

            player.sendMessage(plugin.setHexColor("&cThere is no home to import from &e" + player.getName()));
            return;
        }

        StorageManager storageManager = new StorageManager(uuid, plugin);
        ConfigurationSection essentialsSection = getPlayerYaml().getConfigurationSection("homes");
        File[] storageFile = storageFolder.listFiles();
        assert storageFile != null;
        int storageLength = storageFile.length;

        player.sendMessage(plugin.setHexColor("&7&oStarting to import &6Essentials &7&ointo &bExodusHomes &7&ofor &eeach player..."));

        for (File file : Objects.requireNonNull(essentialsFolder.listFiles())) {
            assert essentialsSection != null;
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

                assert XSound.UI_BUTTON_CLICK.parseSound() != null;
                player.playSound(player.getLocation(), XSound.UI_BUTTON_CLICK.parseSound(), 1.5F, 1);
                storageLength++;
            }
        }
    }

    public File getFiles() {

        essentialsFolder = new File("plugins/Essentials/userdata");

        for (File file : Objects.requireNonNull(essentialsFolder.listFiles())) {

            if (file.getName().endsWith(".yml")) {

                if (file.getName().equalsIgnoreCase(uuid + ".yml")) {
                    return file;
                }
            }
        }
        return null;
    }

    public List<String> getUUID() {

        List<String> playerUUID = new ArrayList<>();

        for (File file : Objects.requireNonNull(essentialsFolder.listFiles())) {

            if (file.getName().endsWith(".yml")) {

                playerUUID.add(file.getName().replace(".yml", ""));
            }
        }
        return playerUUID;
    }

    public FileConfiguration getPlayerYaml() {

        return playerYaml;
    }
}
