package com.reussy.utils;

import com.reussy.ExodusHomes;
import org.bukkit.command.CommandSender;

public class MessageUtils {

	private ExodusHomes plugin;

	public MessageUtils(ExodusHomes plugin) {
		this.plugin = plugin;
	}

	public void sendMessage(CommandSender sender, String message) {
		String messagePrefix = plugin.setHexColor(plugin.getConfig().getString("Plugin-Prefix"));
		try {
			sender.sendMessage(plugin.setHexColor(messagePrefix + " " + message));
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
	}
}
