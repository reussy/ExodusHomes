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
	public File configFile = new File(plugin.getDataFolder(), "config.yml");
	private File langFile = new File(plugin.getDataFolder(), "lang.yml");
	public FileConfiguration langYaml = YamlConfiguration.loadConfiguration(langFile);
	public String PX = ChatColor.translateAlternateColorCodes('&', this.getLang().getString("Plugin-Prefix"));
	private File guiFile = new File(plugin.getDataFolder(), "gui.yml");
	public FileConfiguration guiYaml = YamlConfiguration.loadConfiguration(guiFile);
	private File StorageFile = new File(plugin.getDataFolder(), "storage.yml");
	public FileConfiguration Storage = YamlConfiguration.loadConfiguration(StorageFile);

	public void generateConfig() {

		if(!configFile.exists()) {

			plugin.saveResource("config.yml", false);
		}
	}

	public void generateLang() {

		if(!langFile.exists()) {

			plugin.saveResource("lang.yml", false);
		}
	}

	public FileConfiguration getLang() {

		if(langYaml == null)
			reloadLang();
		return langYaml;
	}

	public void reloadLang() {

		if(langYaml == null) {

			langFile = new File(plugin.getDataFolder(), "lang.yml");
			langYaml = YamlConfiguration.loadConfiguration(langFile);
			Reader stream = new InputStreamReader(plugin.getResource("lang.yml"), StandardCharsets.UTF_8);
			YamlConfiguration defStream = YamlConfiguration.loadConfiguration(stream);
			langYaml.setDefaults(defStream);
		}
	}

	public void registerLang() {

		langFile = new File(plugin.getDataFolder(), "lang.yml");
		if(!langFile.exists()) {

			getLang().options().copyDefaults(true);
			plugin.saveDefaultConfig();
			saveLang();
		}
	}

	public void saveLang() {

		try {
			langYaml.save(langFile);
			plugin.saveDefaultConfig();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void generateGui() {

		if(!guiFile.exists()) {

			plugin.saveResource("gui.yml", false);
		}
	}

	public FileConfiguration getGui() {
		if(guiYaml == null)
			reloadGui();
		return guiYaml;
	}

	public void reloadGui() {

		if(guiYaml == null) {

			guiFile = new File(plugin.getDataFolder(), "gui.yml");
			guiYaml = YamlConfiguration.loadConfiguration(guiFile);
			Reader stream = new InputStreamReader(plugin.getResource("gui.yml"), StandardCharsets.UTF_8);
			YamlConfiguration defStream = YamlConfiguration.loadConfiguration(stream);
			guiYaml.setDefaults(defStream);
		}
	}

	public void registerGui() {

		guiFile = new File(plugin.getDataFolder(), "gui.yml");
		if(!guiFile.exists()) {

			getGui().options().copyDefaults(true);
			plugin.saveDefaultConfig();
			saveGui();
		}
	}

	public void saveGui() {

		try {
			guiYaml.save(guiFile);
			plugin.saveDefaultConfig();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void generateStorage() {

		if(!StorageFile.exists()) {

			plugin.saveResource("storage.yml", false);
		}
	}

	public FileConfiguration getStorage() {

		if(Storage == null) reloadStorage();

		return Storage;

	}

	public void reloadStorage() {

		if(Storage == null) StorageFile = new File(plugin.getDataFolder(), "storage.yml");

		Storage = YamlConfiguration.loadConfiguration(StorageFile);

		Reader defConfigStream = new InputStreamReader(plugin.getResource("storage.yml"), StandardCharsets.UTF_8);

		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		Storage.setDefaults(defConfig);
	}

	public void registerStorage() {

		StorageFile = new File(plugin.getDataFolder(), "storage.yml");

		if(!StorageFile.exists()) {

			getStorage().options().copyDefaults(true);
			plugin.saveDefaultConfig();
			saveStorage();
		}
	}

	public void saveStorage() {

		try {

			Storage.save(StorageFile);
			plugin.saveDefaultConfig();

		} catch(IOException e) {

			e.printStackTrace();

		}
	}
}
