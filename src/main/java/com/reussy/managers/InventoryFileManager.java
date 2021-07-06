package com.reussy.managers;

import com.reussy.ExodusHomes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class InventoryFileManager {

    private final ExodusHomes plugin;
    FileConfiguration overviewYAML;
    FileConfiguration portalYAML;
    File overviewGUI;
    File portalGUI;

    public InventoryFileManager(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    public void generateOverview() {

        overviewGUI = new File(plugin.getDataFolder() + File.separator + "menus" + File.separator + "overview.yml");
        if (!overviewGUI.exists()) {

            plugin.saveResource("menus/overview.yml", false);

        }
    }

    public FileConfiguration getOverviewYAML() {

        overviewGUI = new File(plugin.getDataFolder() + File.separator + "menus" + File.separator + "overview.yml");
        overviewYAML = YamlConfiguration.loadConfiguration(overviewGUI);
        if (overviewGUI == null)
            reloadOverview();
        return overviewYAML;
    }

    public void reloadOverview() {

        if (overviewGUI == null) {
            overviewGUI = new File(plugin.getDataFolder() + File.separator + "menus" + File.separator + "overview.yml");
        }

        overviewGUI = new File(plugin.getDataFolder() + File.separator + "menus" + File.separator + "overview.yml");
        overviewYAML = YamlConfiguration.loadConfiguration(overviewGUI);
        Reader defConfigStream;
        defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource("menus/overview.yml")), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        overviewYAML.setDefaults(defConfig);

    }

    public void generatePortal() {

        portalGUI = new File(plugin.getDataFolder() + File.separator + "menus" + File.separator + "portal.yml");
        if (!portalGUI.exists()) {

            plugin.saveResource("menus/portal.yml", false);

        }
    }

    public FileConfiguration getPortalYAML() {

        portalGUI = new File(plugin.getDataFolder() + File.separator + "menus" + File.separator + "portal.yml");
        portalYAML = YamlConfiguration.loadConfiguration(portalGUI);
        if (portalGUI == null)
            reloadPortal();
        return portalYAML;
    }

    public void reloadPortal() {

        if (portalGUI == null) {
            portalGUI = new File(plugin.getDataFolder() + File.separator + "menus" + File.separator + "portal.yml");
        }

        portalGUI = new File(plugin.getDataFolder() + File.separator + "menus" + File.separator + "portal.yml");
        portalYAML = YamlConfiguration.loadConfiguration(portalGUI);
        Reader defConfigStream;
        defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource("menus/portal.yml")), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        portalYAML.setDefaults(defConfig);

    }

    public String getString(String string, FileConfiguration fileConfiguration) {

        return plugin.setHexColor(fileConfiguration.getString(string));
    }

    public ConfigurationSection configurationSection(String section, FileConfiguration fileConfiguration) {

        return fileConfiguration.getConfigurationSection(section);
    }
}
