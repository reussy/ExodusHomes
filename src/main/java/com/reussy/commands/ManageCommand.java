package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.managers.FileManager;
import com.reussy.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ManageCommand implements CommandExecutor, TabCompleter {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();
	MessageUtils messageUtils = new MessageUtils();
	List<String> subcommands = new ArrayList<>();


	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

		if(!(sender instanceof Player)) {

			messageUtils.sendMessage(sender, fileManager.getMessage("No-Console"));

			return false;
		}

		if(!sender.hasPermission("homes.command.player")) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Insufficient-Permission"));

			return false;
		}

		if(cmd.getName().equalsIgnoreCase("ehm")) {

			if(args.length == 0) {

				sender.sendMessage(plugin.setHexColor("&bExodusHomes &8&l- &7" + plugin.getDescription().getVersion()));
				sender.sendMessage(plugin.setHexColor("&eCreated by &breussy"));
				sender.sendMessage(plugin.setHexColor("&eUse &6/ehm help &efor commands!"));

				return false;
			}

			if(args.length == 1 && !args[0].equalsIgnoreCase("help")) {

				messageUtils.sendMessage(sender, fileManager.getMessage("Few-Arguments").replace("%cmd%", "ehm"));

				return false;
			}

			if(args.length == 2 && args[0].equalsIgnoreCase("go") || args.length == 2 && args[0].equalsIgnoreCase("delete")) {

				messageUtils.sendMessage(sender, fileManager.getMessage("Few-Arguments").replace("%cmd%", "ehm"));

				return false;
			}
		}

		switch(args[0]) {

			case "help":

				sender.sendMessage(plugin.setHexColor("&8--------------------------------------"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor("&r                   &6&oManage Commands"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm help &8- &7Show this message"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm list <player> &8- &7List of home's of player"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm go <player> <home> &8- &7Teleport to other player home"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm delete <player> <home> &8- &7Delete a homes for other player"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm deleteall <player> &8- &7Delete all homes for other player"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor("&8--------------------------------------"));

				break;

			case "go":
				try {
					plugin.databaseType().goHomeByAdmin(Bukkit.getPlayer(args[1]), sender, args[2]);
				} catch(NullPointerException e) {
					messageUtils.sendMessage(sender, fileManager.getMessage("Unknown-Player").replace("%target%", args[1]));
				}
				break;

			case "delete":
				try {
					plugin.databaseType().deleteHomeByAdmin(Bukkit.getPlayer(args[1]), sender, args[2]);
				} catch(NullPointerException e) {
					messageUtils.sendMessage(sender, fileManager.getMessage("Unknown-Player").replace("%target%", args[1]));
				}
				break;

			case "deleteall":
				try {
					plugin.databaseType().deleteAllByAdmin(Bukkit.getPlayer(args[1]), sender);
				} catch(NullPointerException e) {
					messageUtils.sendMessage(sender, fileManager.getMessage("Unknown-Player").replace("%target%", args[1]));
				}
				break;

			case "list":
				try {
					plugin.databaseType().listHomesByAdmin(Bukkit.getPlayer(args[1]), sender);
				} catch(NullPointerException e) {
					messageUtils.sendMessage(sender, fileManager.getMessage("Unknown-Player").replace("%target%", args[1]));
				}
				break;
			default:
				sender.sendMessage(plugin.setHexColor("&bExodusHomes &8&l- &7" + plugin.getDescription().getVersion()));
				sender.sendMessage(plugin.setHexColor("&eCreated by &breussy"));
				sender.sendMessage(plugin.setHexColor("&eUse &6/ehm help &efor commands!"));
				break;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {

		if(cmd.getName().equalsIgnoreCase("ehm")) {
			if(args.length == 1) {

				subcommands.add("help");
				subcommands.add("go");
				subcommands.add("list");
				subcommands.add("delete");
				subcommands.add("deleteall");


				return subcommands;
			} else if(args.length == 2) {
				List<String> onlinePlayers = new ArrayList<>();

				for(Player p : Bukkit.getOnlinePlayers()) {

					onlinePlayers.add(p.getName());
				}
				return onlinePlayers;
			} else if(args.length == 3) {
				Player player = Bukkit.getPlayer(args[1]);

				return plugin.databaseType().getHomes(player);
			}
		}

		return null;
	}
}
