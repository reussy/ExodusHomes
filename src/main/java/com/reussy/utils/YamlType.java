package com.reussy.utils;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.filemanager.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class YamlType implements DatabaseType {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();
	int time = plugin.getConfig().getInt("Teleport-Delay.Time");

	@Override
	public boolean hasHome(Player player) {
		StorageManager storageManager = new StorageManager(player.getUniqueId());
		ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
		assert section != null;
		Set<String> key = section.getKeys(false);
		return !key.isEmpty();
	}

	@Override
	public void createHome(Player player, String home) {

		StorageManager storageManager = new StorageManager(player.getUniqueId());
		List<String> getHomes = this.getHomes(player);

		if(getHomes.contains(home)) {

			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Has-Home")
					.replace("%prefix%", fileManager.PX)));

		} else {

			storageManager.getFile().set("Homes." + home + ".World", player.getWorld().getName());
			storageManager.getFile().set("Homes." + home + ".X", player.getLocation().getBlockX());
			storageManager.getFile().set("Homes." + home + ".Y", player.getLocation().getBlockY());
			storageManager.getFile().set("Homes." + home + ".Z", player.getLocation().getBlockZ());
			storageManager.getFile().set("Homes." + home + ".Pitch", player.getLocation().getPitch());
			storageManager.getFile().set("Homes." + home + ".Yaw", player.getLocation().getYaw());
			storageManager.saveFile();

			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Home-Created")
					.replace("%prefix%", fileManager.PX).replace("%home_name%", home)));
			player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Create-Home")).parseSound(), 1, 1);
			XParticle.circle(5, 10, ParticleDisplay.display(player.getLocation(), Particle.valueOf(plugin.getConfig().getString("Particles.Create-Home"))));
		}
	}

	@Override
	public void deleteHome(Player player, String home) {

		List<String> getHomes = (this.getHomes(player));
		StorageManager storageManager = new StorageManager(player.getUniqueId());


		if(!hasHome(player)) {

			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Empty")
					.replace("%prefix%", fileManager.PX)));

			return;
		}

		if(getHomes.contains(home)) {

			storageManager.getFile().set("Homes." + home, null);
			storageManager.getFile().set(home, null);
			storageManager.saveFile();
			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Home-Deleted")
					.replace("%prefix%", fileManager.PX).replace("%home_name%", home)));
			player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")).parseSound(), 1, 1);

		} else {

			player.sendMessage(plugin.setColor(fileManager.getLang().getString("No-Home")
					.replace("%prefix%", fileManager.PX).replace("%home_name%", home)));
		}

	}

	@Override
	public void goHome(Player player, String home) {

		List<String> getHomes = this.getHomes(player);

		if(!hasHome(player)) {

			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Empty")
					.replace("%prefix%", fileManager.PX)));

			return;
		}

		if(!getHomes.contains(home)) {

			player.sendMessage(plugin.setColor(fileManager.getLang().getString("No-Home")
					.replace("%prefix%", fileManager.PX).replace("%home_name%", home)));
			return;
		}

		Location Home = new Location(Bukkit.getWorld(this.getWorld(player, home)), this.getX(player, home), this.getY(player, home), this.getZ(player, home), this.getYaw(player, home), this.getPitch(player, home));
		Home.add(0.5D, 0.0D, 0.5D);
		TeleportTask teleportTask = new TeleportTask(plugin, time, player, Home, home);

		if(getHomes.contains(home)) {
			teleportTask.runTask();
		}
	}

	@Override
	public void listHomes(Player player) {

		if(!hasHome(player)) {

			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Empty")
					.replace("%prefix%", fileManager.PX)));

			return;
		}

		List<String> getHomes = (this.getHomes(player));

		for(String homeList : getHomes) {
			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Format")
					.replace("%prefix%", fileManager.PX).replace("%home_name%", homeList)));
		}

	}

	@Override
	public String getWorld(Player player, String home) {
		StorageManager storageManager = new StorageManager(player.getUniqueId());
		ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
		assert section != null;
		return section.getString(home + ".World");
	}

	@Override
	public double getX(Player player, String home) {
		StorageManager storageManager = new StorageManager(player.getUniqueId());
		ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
		assert section != null;
		return section.getInt(home + ".X");
	}

	@Override
	public double getY(Player player, String home) {
		StorageManager storageManager = new StorageManager(player.getUniqueId());
		ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
		assert section != null;
		return section.getInt(home + ".Y");
	}

	@Override
	public double getZ(Player player, String home) {
		StorageManager storageManager = new StorageManager(player.getUniqueId());
		ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
		assert section != null;
		return section.getInt(home + ".Z");
	}

	@Override
	public float getPitch(Player player, String home) {
		StorageManager storageManager = new StorageManager(player.getUniqueId());
		ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
		assert section != null;
		return section.getInt(home + ".Pitch");
	}

	@Override
	public float getYaw(Player player, String home) {
		StorageManager storageManager = new StorageManager(player.getUniqueId());
		ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
		assert section != null;
		return section.getInt(home + ".Yaw");
	}

	@Override
	public List<String> getHomes(Player player) {
		StorageManager storageManager = new StorageManager(player.getUniqueId());
		ConfigurationSection section = storageManager.getFile().getConfigurationSection("Homes");
		assert section != null;
		return new ArrayList<>(section.getKeys(false));
	}
}
