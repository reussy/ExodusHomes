package com.reussy.filemanager;

import com.reussy.ExodusHomes;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class FileManager {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
    public String PX = ChatColor.translateAlternateColorCodes('&', this.getLanguage().getString("Plugin-Prefix"));
    public final File Config = new File(plugin.getDataFolder(), "config.yml");
    private File LanguageFile = new File(plugin.getDataFolder(), "language.yml");
    public FileConfiguration Language = YamlConfiguration.loadConfiguration(LanguageFile);
    private File MenusFile = new File(plugin.getDataFolder(), "menus.yml");
    public FileConfiguration Menus = YamlConfiguration.loadConfiguration(MenusFile);
    private File StorageFile = new File(plugin.getDataFolder(), "storage.yml");
    public FileConfiguration Storage = YamlConfiguration.loadConfiguration(StorageFile);

    public void createConfig() throws IOException {

        if (!Config.exists()) plugin.saveResource("config.yml", false);
    }

    public void createMenus() {

        if (!MenusFile.exists()) {

            plugin.saveResource("menus.yml", false);
        }
    }

    public FileConfiguration getMenus() {

        if (Menus == null) reloadMenus();

        return Menus;

    }

    public void reloadMenus() {

        if (Menus == null) MenusFile = new File(plugin.getDataFolder(), "menus.yml");

        Menus = YamlConfiguration.loadConfiguration(MenusFile);

        Reader defConfigStream = new InputStreamReader(plugin.getResource("menus.yml"), StandardCharsets.UTF_8);

        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        Menus.setDefaults(defConfig);
    }

    public void registerMenus() {

        MenusFile = new File(plugin.getDataFolder(), "menus.yml");

        if (!MenusFile.exists()) {

            getMenus().options().copyDefaults(true);
            plugin.saveDefaultConfig();
            saveMenus();
        }
    }

    public void saveMenus() {

        try {

            Menus.save(MenusFile);
            plugin.saveDefaultConfig();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void createLanguage() {

        if (!LanguageFile.exists()) {

            plugin.saveResource("language.yml", false);
        }
    }

    public FileConfiguration getLanguage() {

        if (Language == null) reloadLanguage();

        return Language;

    }

    public void reloadLanguage() {

        if (Language == null) LanguageFile = new File(plugin.getDataFolder(), "language.yml");

        Language = YamlConfiguration.loadConfiguration(LanguageFile);

        Reader defConfigStream = new InputStreamReader(plugin.getResource("language.yml"), StandardCharsets.UTF_8);

        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        Language.setDefaults(defConfig);
    }

    public void registerLanguage() {

        LanguageFile = new File(plugin.getDataFolder(), "language.yml");

        if (!LanguageFile.exists()) {

            getLanguage().options().copyDefaults(true);
            plugin.saveDefaultConfig();
            saveLanguage();
        }
    }

    public void saveLanguage() {

        try {

            Language.save(LanguageFile);
            plugin.saveDefaultConfig();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void createStorage() {

        if (!StorageFile.exists()) {

            plugin.saveResource("storage.yml", false);
        }
    }

    public FileConfiguration getStorage() {

        if (Storage == null) reloadStorage();

        return Storage;

    }

    public void reloadStorage() {

        if (Storage == null) StorageFile = new File(plugin.getDataFolder(), "storage.yml");

        Storage = YamlConfiguration.loadConfiguration(StorageFile);

        Reader defConfigStream = new InputStreamReader(plugin.getResource("storage.yml"), StandardCharsets.UTF_8);

        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        Storage.setDefaults(defConfig);
    }

    public void registerStorage() {

        StorageFile = new File(plugin.getDataFolder(), "storage.yml");

        if (!StorageFile.exists()) {

            getStorage().options().copyDefaults(true);
            plugin.saveDefaultConfig();
            saveStorage();
        }
    }

    public void saveStorage() {

        try {

            Storage.save(StorageFile);
            plugin.saveDefaultConfig();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}
