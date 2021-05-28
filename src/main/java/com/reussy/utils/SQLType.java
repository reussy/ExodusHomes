package com.reussy.utils;

import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import com.reussy.ExodusHomes;
import com.reussy.managers.FileManager;
import com.reussy.sql.SQLData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SQLType implements DatabaseType {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	SQLData sqlData = new SQLData();
	FileManager fileManager = new FileManager();
	MessageUtils messageUtils = new MessageUtils();

	@Override
	public boolean hasHome(Player player) {
		return sqlData.hasHomes(plugin.getSQL(), player.getUniqueId());
	}

	@Override
	public void createHome(Player player, String home) {

		List<String> getHomes = this.getHomes(player);
		int getLimit = plugin.getPermission(player);
		String world = player.getWorld().getName();
		double x = player.getLocation().getBlockX();
		double y = player.getLocation().getBlockY();
		double z = player.getLocation().getBlockZ();
		float pitch = player.getLocation().getPitch();
		float yaw = player.getLocation().getYaw();

		while(!getHomes.isEmpty()) {
			if(getLimit == getHomes.size()) {

				messageUtils.sendMessage(player, fileManager.getMessage("Limit-Reached"));

				return;
			}
		}

		if(hasHome(player) && getHomes.contains(home)) {

			messageUtils.sendMessage(player, fileManager.getMessage("Has-Home"));

		} else {

			sqlData.createHomes(plugin.getSQL(), player.getUniqueId(), player, world, home, x, y, z, pitch, yaw);
			messageUtils.sendMessage(player, fileManager.getMessage("Home-Created").replace("%home_name%", home));
			player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Create-Home")).parseSound(), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
			XParticle.circle(2, 5, ParticleDisplay.display(player.getLocation(), Particle.valueOf(plugin.getConfig().getString("Particles.Create-Home"))));
		}
	}

	@Override
	public void deleteHome(Player player, String home) {

		List<String> getHomes = (this.getHomes(player));

		if(hasHome(player)) {

			messageUtils.sendMessage(player, fileManager.getMessage("Homes-Empty"));

			return;
		}

		if(!getHomes.contains(home)) {

			messageUtils.sendMessage(player, fileManager.getMessage("No-Home").replace("%home_name%", home));
			return;
		}

		sqlData.deleteHomes(plugin.getSQL(), player.getUniqueId(), home);
		messageUtils.sendMessage(player, fileManager.getMessage("Home-Deleted").replace("%home_name%", home));
		player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

	}

	@Override
	public void deleteHomeByAdmin(Player player, CommandSender sender, String home) {

		List<String> getHomes = (this.getHomes(player));

		if(!player.isOnline()) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Unknown-Player"));
			return;
		}

		if(!hasHome(player)) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Homes-Empty".replace("%target%", player.getName())));

			return;
		}

		if(!getHomes.contains(home)) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Manage.No-Home").replace("%home_name%", home)
					.replace("%target%", player.getName()));
			return;
		}

		sqlData.deleteHomes(plugin.getSQL(), player.getUniqueId(), home);
		messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Home-Deleted").replace("%home_name%", home)
				.replace("%target%", player.getName()));
		((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));


	}

	@Override
	public void deleteAll(Player player) {

		if(hasHome(player)) {

			messageUtils.sendMessage(player, fileManager.getMessage("Homes-Empty"));

			return;
		}

		sqlData.deleteAll(plugin.getSQL(), player.getUniqueId());
		messageUtils.sendMessage(player, fileManager.getMessage("Homes-Deleted"));
		player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
	}

	@Override
	public void deleteAllByAdmin(Player player, CommandSender sender) {

		if(!player.isOnline()) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Unknown-Player"));
			return;
		}

		if(!hasHome(player)) {

			messageUtils.sendMessage(player, fileManager.getMessage("Manage.Homes-Empty".replace("%target%", player.getName())));

			return;
		}

		sqlData.deleteAll(plugin.getSQL(), player.getUniqueId());
		messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Homes-Deleted").replace("%target%", player.getName()));
		((Player) sender).playSound(((Player) sender).getLocation(), Sound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

	}

	@Override
	public void goHome(Player player, String home) {

		List<String> getHomes = (sqlData.getHomes(plugin.getSQL(), player.getUniqueId()));

		if(hasHome(player)) {

			messageUtils.sendMessage(player, fileManager.getMessage("Homes-Empty"));

			return;
		}

		if(!getHomes.contains(home)) {

			messageUtils.sendMessage(player, fileManager.getMessage("No-Home").replace("%home_name%", home));
			return;
		}

		Location Home = new Location(Bukkit.getWorld(this.getWorld(player, home)), this.getX(player, home), this.getY(player, home), this.getZ(player, home), this.getYaw(player, home), this.getPitch(player, home));
		Home.add(0.5D, 0.0D, 0.5D);
		int time = plugin.getConfig().getInt("Teleport-Delay.Time");
		TeleportTask teleportTask = new TeleportTask(plugin, time, player, Home, home);
		teleportTask.runTask();

	}

	@Override
	public void goHomeByAdmin(Player player, CommandSender sender, String home) {

		List<String> getHomes = (sqlData.getHomes(plugin.getSQL(), player.getUniqueId()));

		if(!player.isOnline()) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Unknown-Player"));
			return;
		}

		if(!hasHome(player)) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", player.getName()));

			return;
		}

		if(!getHomes.contains(home)) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Manage.No-Home").replace("%home_name%", home)
					.replace("%target%", player.getName()));
			return;
		}

		Location Home = new Location(Bukkit.getWorld(this.getWorld(player, home)), this.getX(player, home), this.getY(player, home), this.getZ(player, home), this.getYaw(player, home), this.getPitch(player, home));
		Home.add(0.5D, 0.0D, 0.5D);
		player.teleport(Home);
		messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Home-Teleport")
				.replace("%home_name%", home)
				.replace("%target%", player.getName()));
	}

	@Override
	public void listHomes(Player player) {

		if(!hasHome(player)) {

			messageUtils.sendMessage(player, fileManager.getMessage("Homes-Empty"));

			return;
		}

		List<String> getHomes = (sqlData.getHomes(plugin.getSQL(), player.getUniqueId()));

		for(String homeList : getHomes) {
			player.sendMessage(plugin.setHexColor(fileManager.getMessage("Homes-Format").replace("%home_name%", homeList)));
		}
	}

	@Override
	public void listHomesByAdmin(Player player, CommandSender sender) {

		if(!player.isOnline()) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Unknown-Player"));
			return;
		}

		if(!hasHome(player)) {

			messageUtils.sendMessage(player, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", player.getName()));

			return;
		}

		List<String> getHomes = (sqlData.getHomes(plugin.getSQL(), player.getUniqueId()));

		for(String homeList : getHomes) {
			sender.sendMessage(plugin.setHexColor(fileManager.getMessage("Manage.Homes-Format").replace("%home_name%", homeList)));

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
