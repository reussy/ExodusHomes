package com.reussy.utils;

import com.cryptomorin.xseries.XSound;
import com.reussy.ExodusHomes;
import com.reussy.managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class TeleportTask {

	private final ExodusHomes plugin;
	private final Player player;
	private final Location location;
	private final String home;
	int teleportTask;
	int time;
	FileManager fileManager = new FileManager();
	MessageUtils messageUtils = new MessageUtils();

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
			messageUtils.sendMessage(player, fileManager.getMessage("Already-Teleporting"));
			return;
		}

		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		teleportTask = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
			if(time == 0) {

				Bukkit.getScheduler().cancelTask(teleportTask);
				player.teleport(location);
				plugin.playerCache.remove(player.getName());
				messageUtils.sendMessage(player, fileManager.getMessage("Home-Teleport").replace("%home_name%", home));
				player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Teleport-Home")).parseSound(), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

			} else {

				player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Waiting-Teleport")).parseSound(), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
				messageUtils.sendMessage(player, fileManager.getMessage("Teleport-Delay")
						.replace("%seconds%", String.valueOf(time)));
				time--;
			}
		}, 0L, 20);
	}
}
