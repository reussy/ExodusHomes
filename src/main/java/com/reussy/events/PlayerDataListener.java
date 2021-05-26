package com.reussy.events;

import com.reussy.ExodusHomes;
import com.reussy.managers.StorageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerDataListener implements Listener {

	private ExodusHomes plugin;

	public PlayerDataListener(ExodusHomes plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Player player = e.getPlayer();
		StorageManager storage = new StorageManager(player.getUniqueId(), plugin);
		storage.createPlayerFile(player);
	}

}
