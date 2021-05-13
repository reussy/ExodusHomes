package com.reussy.utils;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.sql.SQLData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.List;

public class SQLType implements DatabaseType {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	SQLData sqlData = new SQLData();
	FileManager fileManager = new FileManager();
	int time = plugin.getConfig().getInt("Teleport-Delay.Time");

	@Override
	public boolean hasHome(Player player) {
		return !sqlData.hasHomes(plugin.getSQL(), player.getUniqueId());
	}

	@Override
	public void createHome(Player player, String home) {

		List<String> getHomes = this.getHomes(player);
		String world = player.getWorld().getName();
		double x = player.getLocation().getBlockX();
		double y = player.getLocation().getBlockY();
		double z = player.getLocation().getBlockZ();
		float pitch = player.getLocation().getPitch();
		float yaw = player.getLocation().getYaw();

		if(getHomes.contains(home)) {

			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Has-Home")
					.replace("%prefix%", fileManager.PX)));
		} else {

			sqlData.createHomes(plugin.getSQL(), player.getUniqueId(), player, world, home, x, y, z, pitch, yaw);
			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Home-Created")
					.replace("%prefix%", fileManager.PX).replace("%home_name%", home)));
			player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Create-Home")).parseSound(), 1, 1);
			XParticle.circle(5, 10, ParticleDisplay.display(player.getLocation(), Particle.valueOf(plugin.getConfig().getString("Particles.Create-Home"))));
		}
	}

	@Override
	public void deleteHome(Player player, String home) {

		List<String> getHomes = (sqlData.getHomes(plugin.getSQL(), player.getUniqueId()));

		if(hasHome(player)) {

			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Empty")
					.replace("%prefix%", fileManager.PX)));

			return;
		}

		if(getHomes.contains(home)) {

			sqlData.deleteHomes(plugin.getSQL(), player.getUniqueId(), home);
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

		List<String> getHomes = (sqlData.getHomes(plugin.getSQL(), player.getUniqueId()));

		if(hasHome(player)) {

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

		if(hasHome(player)) {

			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Empty")
					.replace("%prefix%", fileManager.PX)));

			return;
		}

		List<String> getHomes = (sqlData.getHomes(plugin.getSQL(), player.getUniqueId()));

		for(String homeList : getHomes) {
			player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Format")
					.replace("%prefix%", fileManager.PX).replace("%home_name%", homeList)));
		}
	}

	@Override
	public String getWorld(Player player, String home) {
		return sqlData.getWorld(plugin.getSQL(), player.getUniqueId(), home);
	}

	@Override
	public double getX(Player player, String home) {
		return sqlData.getX(plugin.getSQL(), player.getUniqueId(), home);
	}

	@Override
	public double getY(Player player, String home) {
		return sqlData.getY(plugin.getSQL(), player.getUniqueId(), home);
	}

	@Override
	public double getZ(Player player, String home) {
		return sqlData.getZ(plugin.getSQL(), player.getUniqueId(), home);
	}

	@Override
	public float getPitch(Player player, String home) {
		return sqlData.getPitch(plugin.getSQL(), player.getUniqueId(), home);
	}

	@Override
	public float getYaw(Player player, String home) {
		return sqlData.getYaw(plugin.getSQL(), player.getUniqueId(), home);
	}

	@Override
	public List<String> getHomes(Player player) {

		return sqlData.getHomes(plugin.getSQL(), player.getUniqueId());
	}
}
