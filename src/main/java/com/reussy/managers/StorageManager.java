package com.reussy.managers;

import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StorageManager {

	private final ExodusHomes plugin;
	UUID uuid;
	File idFile;
	FileConfiguration idYaml;

	public StorageManager(UUID uuid, ExodusHomes plugin) {

		this.plugin = plugin;
		idFile = new File(plugin.getDataFolder() + File.separator + "storage" + File.separator + uuid + ".yml");
		idYaml = YamlConfiguration.loadConfiguration(idFile);
	}

	public void createPlayerFile(Player player) { //Create a file for each player

		if(!idFile.exists()) {

			try {
				new BukkitRunnable() {
					@lombok.SneakyThrows
					@Override
					public void run() {

						Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCreating new file in storage for &e" + player.getName()));
						idFile.createNewFile();
						idYaml.set("Information" + ".UUID", player.getUniqueId().toString());
						idYaml.set("Information" + ".PlayerName", player.getName());
						idYaml.createSection("Homes");
						idYaml.save(idFile);
						Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccess!"));
					}
				}.runTaskAsynchronously(plugin);
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
		Reader stream = new InputStreamReader(plugin.getResource(File.separator + "storage" + File.separator + uuid + ".yml"), StandardCharsets.UTF_8);
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
