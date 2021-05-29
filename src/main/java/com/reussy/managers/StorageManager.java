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
import java.util.Objects;
import java.util.UUID;

public class StorageManager {

	private final ExodusHomes plugin;
	UUID uuid;
	File playerFile;
	FileConfiguration playerYaml;

	public StorageManager(UUID uuid, ExodusHomes plugin) {

		this.plugin = plugin;
		playerFile = new File(plugin.getDataFolder() + File.separator + "storage" + File.separator + uuid + ".yml");
		playerYaml = YamlConfiguration.loadConfiguration(playerFile);
	}

	public void createPlayerFile(Player player) { //Create a file for each player

		if(!playerFile.exists()) {

			try {
				new BukkitRunnable() {
					@lombok.SneakyThrows
					@Override
					public void run() {

						Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCreating new file in storage for &e" + player.getName()));
						playerFile.createNewFile();
						playerYaml.set("Information" + ".UUID", player.getUniqueId().toString());
						playerYaml.set("Information" + ".PlayerName", player.getName());
						playerYaml.createSection("Homes");
						playerYaml.save(playerFile);
						Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccess!"));
					}
				}.runTaskAsynchronously(plugin);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public FileConfiguration getFile() {

		if(playerFile == null)
			reloadFile();
		return playerYaml;
	}

	public void reloadFile() {

		playerYaml = YamlConfiguration.loadConfiguration(playerFile);
		Reader stream = new InputStreamReader(Objects.requireNonNull(plugin.getResource(File.separator + "storage" + File.separator + uuid + ".yml")), StandardCharsets.UTF_8);
		YamlConfiguration defStream = YamlConfiguration.loadConfiguration(stream);
		playerYaml.setDefaults(defStream);
	}

	public void saveFile() {

		try {
			playerYaml.save(playerFile);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
