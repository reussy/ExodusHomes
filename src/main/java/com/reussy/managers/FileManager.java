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

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	File configFile = new File(plugin.getDataFolder(), "config.yml");
	File langFile = new File(plugin.getDataFolder(), "lang.yml");
	public FileConfiguration langYaml = YamlConfiguration.loadConfiguration(langFile);
	File guiFile = new File(plugin.getDataFolder(), "gui.yml");
	public FileConfiguration guiYaml = YamlConfiguration.loadConfiguration(guiFile);

	public void generateFolder() {

		if("YAML".equalsIgnoreCase(plugin.getConfig().getString("Database-Type"))) {
			File idFile = new File(plugin.getDataFolder() + File.separator + "storage");
			if(!idFile.exists()) idFile.mkdirs();
		}
	}

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

	public String getMessage(String message) {

		return this.getLang().getString(message);
	}

	public String getText(String text) {

		return plugin.setHexColor(this.getGui().getString(text));
	}
}