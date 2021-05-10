package com.reussy.filemanager;

import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StorageManager {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	UUID id;
	File idFile;
	FileConfiguration idYaml;

	public StorageManager(UUID id) {

		idFile = new File(plugin.getDataFolder() + File.separator + "storage" + File.separator + id + ".yml");
		idYaml = YamlConfiguration.loadConfiguration(idFile);
	}

	public void createPlayerFile(Player player) { //Create a file for each player

		if(!idFile.exists()) {

			try {
				idFile.createNewFile();
				idYaml.set(player.getName() + ".UUID", player.getUniqueId().toString());
				idYaml.save(idFile);
				Bukkit.getConsoleSender().sendMessage("Creating new file in storage for " + player.getName());

			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public FileConfiguration getFile() {

		if(idFile == null)
			reloadFile();
		return idYaml;
	}

	public void reloadFile() {

		idYaml = YamlConfiguration.loadConfiguration(idFile);
		Reader stream = new InputStreamReader(plugin.getResource(File.separator + "storage" + File.separator + id + ".yml"), StandardCharsets.UTF_8);
		YamlConfiguration defStream = YamlConfiguration.loadConfiguration(stream);
		idYaml.setDefaults(defStream);
	}

	public void saveFile() {

		try {
			idYaml.save(idFile);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
