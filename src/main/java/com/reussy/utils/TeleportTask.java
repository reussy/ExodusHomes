package com.reussy.utils;

import com.cryptomorin.xseries.XSound;
import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class TeleportTask {

	private final Player player;
	private final Location location;
	private final String home;
	FileManager fileManager = new FileManager();
	int teleportTask;
	int time;
	private ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);

	public TeleportTask(ExodusHomes plugin, int time, Player player, Location location, String home) {
		this.plugin = plugin;
		this.time = time;
		this.player = player;
		this.location = location;
		this.home = home;

	}

	public void runTask() {
		if(!plugin.playerCache.contains(player.getName())) {
			plugin.playerCache.add(player.getName());
		} else {
			player.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Already-Teleporting").replace("%prefix%", fileManager.PX)));
			return;
		}

		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		teleportTask = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
			if(time == 0) {

				Bukkit.getScheduler().cancelTask(teleportTask);
				player.teleport(location);
				plugin.playerCache.remove(player.getName());
				player.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Home-Teleport").replace("%prefix%", fileManager.PX).replace("%home_name%", home)));
				player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Teleport-Home")).parseSound(), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
				player.spawnParticle(Particle.valueOf(plugin.getConfig().getString("Particles.Teleport-Home")), player.getLocation(), 10);

			} else {

				player.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Teleport-Delay")
						.replace("%prefix%", fileManager.PX)
						.replace("%seconds%", String.valueOf(time))));
				time--;
			}
		}, 0L, 20);
	}
}
