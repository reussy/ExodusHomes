package com.reussy.events;

import com.reussy.ExodusHomes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinPlayerEvent implements Listener {

	private ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		Player P = e.getPlayer();


	}
}
