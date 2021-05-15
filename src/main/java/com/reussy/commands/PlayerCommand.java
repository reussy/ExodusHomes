package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.gui.MainGUI;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommand implements CommandExecutor, TabCompleter {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();
	MainGUI mainGUI = new MainGUI();
	List<String> subcommands = new ArrayList<>();
	int teleportTask;
	int time = plugin.getConfig().getInt("Teleport-Delay.Time");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {

			sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("No-Console")
					.replace("%prefix%", fileManager.PX)));

			return false;
		}

		if(!sender.hasPermission("homes.command.player")) {

			sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Insufficient-Permission")
					.replace("%prefix%", fileManager.PX)));

			return false;
		}

		Player player = (Player) sender;
		int max_amount = 6;
		int amount = plugin.databaseType().getHomes(player).size();

		if(cmd.getName().equalsIgnoreCase("home")) {

			if(args.length == 0) {

				mainGUI.GUI(player);
				player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("Sounds.Open-GUI")), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
				return false;
			}

			if(args.length == 1 && !args[0].equalsIgnoreCase("list")) {

				sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Few-Arguments")
						.replace("%prefix%", fileManager.PX)));
				return false;
			}

			if(args.length > 2) {

				sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Many-Arguments")
						.replace("%prefix%", fileManager.PX)));
				return false;
			}

			if(!sender.hasPermission("homes.create." + amount)) {

				sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Insufficient-Permission")
						.replace("%prefix%", fileManager.PX)));
				return false;
			}
		}

		for(String allowedWorlds : plugin.getConfig().getStringList("Whitelist-Worlds")) {

			if(!((Player) sender).getWorld().getName().contains(allowedWorlds)) {
				sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Deny-World")
						.replace("%prefix%", fileManager.PX)));
				return false;
			}
		}

		for(String disallowedNames : plugin.getConfig().getStringList("Blacklist-Names")) {

			if(args[0].equalsIgnoreCase("create")) {
				if(args[1].contains(disallowedNames)) {
					sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Name-Not-Allowed")
							.replace("%prefix%", fileManager.PX)));
					return false;
				}
			}
		}

		switch(args[0]) {

			case "create":

				plugin.databaseType().createHome(player, args[1]);

				break;

			case "delete":

				plugin.databaseType().deleteHome(player, args[1]);

				break;

			case "go":

				plugin.databaseType().goHome(player, args[1]);

				break;

			case "list":

				plugin.databaseType().listHomes(player);

				break;

			default:
				for(String Help : fileManager.getLang().getStringList("Help-Player")) {
					sender.sendMessage(plugin.setHexColor(Help));
				}
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		Player player = (Player) sender;
		List<String> getHomes = plugin.databaseType().getHomes(player);

		if(getHomes.isEmpty()) return null;

		do {
			if(command.getName().equalsIgnoreCase("home")) {
				if(args.length == 1) {
					if(player.hasPermission("homes.command.player")) {

						subcommands.add("create");
						subcommands.add("delete");
						subcommands.add("go");
						subcommands.add("list");
						return subcommands;
					}
				} else if(args.length == 2) {
					return getHomes;
				} else if(args.length > 2) {
					return null;
				}
			}
		} while(!getHomes.isEmpty());
		return null;
	}
}
