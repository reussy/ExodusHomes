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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommand implements CommandExecutor, TabCompleter {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();
	MainGUI mainGUI = new MainGUI();
	List<String> subcommands = new ArrayList<>();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

		if(!(sender instanceof Player)) {

			sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("No-Console")
					.replace("%prefix%", fileManager.PX)));

			return false;
		}

		if(!sender.hasPermission("homes.command.player") || !sender.isOp()) {

			sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Insufficient-Permission")
					.replace("%prefix%", fileManager.PX)));

			return false;
		}

		Player player = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("home")) {

			if(args.length == 0) {

				mainGUI.GUI(player);
				player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("Sounds.Open-GUI")), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
				return false;
			}

			if(args.length == 1 && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("deleteall")) {

				sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Few-Arguments")
						.replace("%prefix%", fileManager.PX).replace("%cmd%", "home")));
				return false;
			}

			if(args.length > 2) {

				sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Many-Arguments")
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

			case "help":

				sender.sendMessage(plugin.setHexColor("&8--------------------------------------"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor("&r                   &e&oPlayer Commands"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/home help &8- &7Show this message"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/home &8- &7Open Main GUI"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/home create <name> &8- &7Create a home"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/home delete <home> &8- &7Delete a home"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/home deleteall &8- &7Delete all yor current home's"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/home go <home> &8- &7Teleport to home"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/home list &8- &7List of your home's"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor("&8--------------------------------------"));

				break;

			case "create":
				plugin.databaseType().createHome(player, args[1]);
				break;

			case "delete":
				plugin.databaseType().deleteHome(player, args[1]);
				break;

			case "deleteall":
				plugin.databaseType().deleteAll(player);
				break;

			case "go":
				plugin.databaseType().goHome(player, args[1]);
				break;

			case "list":
				plugin.databaseType().listHomes(player);
				break;

			default:
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {

		Player player = (Player) sender;
		List<String> getHomes = plugin.databaseType().getHomes(player);
		if(command.getName().equalsIgnoreCase("home")) {
			if(args.length == 1) {
				if(player.hasPermission("homes.command.player")) {

					subcommands.add("help");
					subcommands.add("create");
					subcommands.add("delete");
					subcommands.add("deleteall");
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
		return null;
	}
}
