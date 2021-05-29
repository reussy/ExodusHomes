package com.reussy.managers;

import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EssentialsStorageManager {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	private final List<HomesManager> homesManager = new ArrayList<>();
	File essentialsFolder;
	File playerFile;
	FileConfiguration playerYaml;


	public EssentialsStorageManager(UUID uuid) {

		essentialsFolder = Bukkit.getServer().getPluginManager().getPlugin("Essentials").getDataFolder();
		playerFile = new File(essentialsFolder, File.separator + "userdata/" + uuid + ".yml");
		playerYaml = YamlConfiguration.loadConfiguration(playerFile);

	}

	public FileConfiguration getPlayerYaml() {

		return playerYaml;
	}

	public void importHomes(UUID uuid, Player player, CommandSender sender) {

		if(playerFile == null) {

			sender.sendMessage(plugin.setHexColor("&cThere is no file created for " + player.getName()));
			return;
		}

		StorageManager storageManager = new StorageManager(uuid, plugin);
		ConfigurationSection essentialsSection = getPlayerYaml().getConfigurationSection("homes");

		if(essentialsSection == null) {

			sender.sendMessage(plugin.setHexColor("&cThere is no file created for " + player.getName()));
			return;
		}


		sender.sendMessage(plugin.setHexColor("&e&oStarting to import EssentialsX into ExodusHomes..."));
		for(String essentialsHome : essentialsSection.getKeys(false)) {

			if(essentialsHome.isEmpty()) {

				sender.sendMessage(plugin.setHexColor("&cThere is no home to import from " + player.getName()));
				return;
			}


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

		}
		sender.sendMessage(plugin.setHexColor("&aImport completed successfully!"));
		sender.sendMessage(plugin.setHexColor("&b" + essentialsSection.getKeys(false).size() + " &ahomes have been imported for " + player.getName()));
	}
}
