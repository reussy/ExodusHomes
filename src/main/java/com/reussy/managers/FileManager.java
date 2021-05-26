package com.reussy.managers;

import com.reussy.ExodusHomes;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class FileManager {

	private final ExodusHomes plugin;

	public FileManager(ExodusHomes plugin) {
		this.plugin = plugin;
	}

	public void generateFolder() {

		if("YAML".equalsIgnoreCase(plugin.getConfig().getString("Database-Type"))) {
			File idFile = new File(plugin.getDataFolder() + File.separator + "storage");
			if(!idFile.exists()) idFile.mkdirs();
		}
	}

	public void generateConfig() {

		File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(!configFile.exists()) {

			plugin.saveResource("config.yml", false);
		}
	}

	public void generateLang() {

		File langFile = new File(plugin.getDataFolder(), "lang.yml");
		if(!langFile.exists()) {

			plugin.saveResource("lang.yml", false);
		}
	}

	public FileConfiguration getLang() {

		File langFile = new File(plugin.getDataFolder(), "lang.yml");
		return YamlConfiguration.loadConfiguration(langFile);
	}

	public void reloadLang() {

		File langFile;
		FileConfiguration langYaml;

		langFile = new File(plugin.getDataFolder(), "lang.yml");
		langYaml = YamlConfiguration.loadConfiguration(langFile);
		YamlConfiguration defStream;
		try(Reader stream = new InputStreamReader(plugin.getResource("lang.yml"), StandardCharsets.UTF_8)) {
			defStream = YamlConfiguration.loadConfiguration(stream);
			langYaml.setDefaults(defStream);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void registerLang() {

		File langFile = new File(plugin.getDataFolder(), "lang.yml");
		if(!langFile.exists()) {

			getLang().options().copyDefaults(true);
			plugin.saveDefaultConfig();
			saveLang();
		}
	}

	public void saveLang() {

		File langFile = new File(plugin.getDataFolder(), "lang.yml");
		FileConfiguration langYaml = YamlConfiguration.loadConfiguration(langFile);
		try {
			langYaml.save(langFile);
			plugin.saveDefaultConfig();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void generateGui() {

		File guiFile = new File(plugin.getDataFolder(), "gui.yml");
		if(!guiFile.exists()) {

			plugin.saveResource("gui.yml", false);
		}
	}

	public FileConfiguration getGui() {
		File guiFile = new File(plugin.getDataFolder(), "gui.yml");
		return YamlConfiguration.loadConfiguration(guiFile);
	}

	public void reloadGui() {
		FileConfiguration guiYaml;
		File guiFile = new File(plugin.getDataFolder(), "gui.yml");
		guiYaml = YamlConfiguration.loadConfiguration(guiFile);
		Reader stream = new InputStreamReader(plugin.getResource("gui.yml"), StandardCharsets.UTF_8);
		YamlConfiguration defStream = YamlConfiguration.loadConfiguration(stream);
		guiYaml.setDefaults(defStream);
	}

	public void registerGui() {

		File guiFile = new File(plugin.getDataFolder(), "gui.yml");
		if(!guiFile.exists()) {

			getGui().options().copyDefaults(true);
			plugin.saveDefaultConfig();
			saveGui();
		}
	}

	public void saveGui() {
		File guiFile = new File(plugin.getDataFolder(), "gui.yml");
		FileConfiguration guiYaml = YamlConfiguration.loadConfiguration(guiFile);
		try {
			guiYaml.save(guiFile);
			plugin.saveDefaultConfig();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public String getMessage(String message) {

		return this.getLang().getString(message);
	}
}