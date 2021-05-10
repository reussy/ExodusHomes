package com.reussy.events;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.StorageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class PlayerDataListener implements Listener {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);

	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws IOException {

		Player player = e.getPlayer();

		StorageManager storage = new StorageManager(player.getUniqueId());
		storage.createPlayerFile(player);
	}
}
