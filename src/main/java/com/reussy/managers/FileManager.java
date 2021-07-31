package com.reussy.managers;

import com.reussy.ExodusHomes;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FileManager {

    private final ExodusHomes plugin;
    File configFile;
    File langFile;
    FileConfiguration langYaml;

    public FileManager(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    public void pluginFolders() {

        if ("YAML".equalsIgnoreCase(plugin.getConfig().getString("Database-Type"))) {
            File storage = new File(plugin.getDataFolder() + File.separator + "storage");
            if (!storage.exists()) storage.mkdirs();
        }
        File menus = new File(plugin.getDataFolder() + File.separator + "menus");
        if (!menus.exists()) menus.mkdirs();

    }

    public void generateConfig() {

        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {

            plugin.saveResource("config.yml", false);
        }
    }

    public void generateLang() {

        langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {

            plugin.saveResource("lang.yml", false);
        }
    }

    public FileConfiguration getLang() {

        langFile = new File(plugin.getDataFolder(), "lang.yml");
        langYaml = YamlConfiguration.loadConfiguration(langFile);
        return langYaml;
    }

    public void reloadLang() {


        langFile = new File(plugin.getDataFolder(), "lang.yml");
        try {
            langYaml = YamlConfiguration.loadConfiguration(langFile);
            Reader defConfigStream;
            defConfigStream = new InputStreamReader(Objects.requireNonNull(plugin.getResource("lang.yml")), StandardCharsets.UTF_8);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            langYaml.setDefaults(defConfig);
            defConfigStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLang() {

        langFile = new File(plugin.getDataFolder(), "lang.yml");
        try {
            langYaml.save(langFile);
            plugin.saveDefaultConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String message) {

        return this.getLang().getString(message);
    }
}