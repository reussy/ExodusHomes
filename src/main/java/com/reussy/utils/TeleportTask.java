package com.reussy.utils;

import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class TeleportTask {

	private final ExodusHomes plugin;
	int teleportTask;
	int time;
	private Player player;
	private Location location;
	private String home;
	public TeleportTask(ExodusHomes plugin) {
		this.plugin = plugin;
	}

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
			plugin.messageUtils.sendMessage(player, plugin.fileManager.getMessage("Already-Teleporting"));
			return;
		}

		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		teleportTask = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
			if(time == 0) {

				Bukkit.getScheduler().cancelTask(teleportTask);
				player.teleport(location);
				plugin.playerCache.remove(player.getName());
				plugin.messageUtils.sendMessage(player, plugin.fileManager.getMessage("Home-Teleport").replace("%home_name%", home));
				player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Teleport-Home")).parseSound(), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
				XParticle.circle(2, 5, ParticleDisplay.display(player.getLocation(), Particle.valueOf(plugin.getConfig().getString("Particles.Teleport-Home"))));

			} else {

				plugin.messageUtils.sendMessage(player, plugin.fileManager.getMessage("Teleport-Delay")
						.replace("%seconds%", String.valueOf(time)));
				time--;
			}
		}, 0L, 20);
	}
}
