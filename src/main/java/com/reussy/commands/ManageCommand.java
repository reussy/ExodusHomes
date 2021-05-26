package com.reussy.commands;

import com.reussy.ExodusHomes;
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

	public ExodusHomes plugin;
	List<String> subcommands = new ArrayList<>();

	public ManageCommand(ExodusHomes plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

		if(!(sender instanceof Player)) {

			plugin.messageUtils.sendMessage(sender, plugin.fileManager.getMessage("No-Console"));

			return false;
		}

		if(!sender.hasPermission("homes.command.player")) {

			plugin.messageUtils.sendMessage(sender, plugin.fileManager.getMessage("Insufficient-Permission"));

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

				plugin.messageUtils.sendMessage(sender, plugin.fileManager.getMessage("Few-Arguments").replace("%cmd%", "ehm"));

				return false;
			}

			if(args.length == 2 && args[0].equalsIgnoreCase("go")) {

				plugin.messageUtils.sendMessage(sender, plugin.fileManager.getMessage("Few-Arguments"));

				return false;
			}

			if(Bukkit.getPlayer(args[1]) == null || !Bukkit.getPlayer(args[1]).isOnline()) {

				plugin.messageUtils.sendMessage(sender, plugin.fileManager.getMessage("Unknown-Player"));
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
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm go <player> <home> &8- &7Teleport to player home"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm delete <player> &8- &7Delete a homes for a player"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm deleteall <player> &8- &7Delete all homes for a player"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm list <player> &8- &7List of home's of player"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor("&8--------------------------------------"));

				break;

			case "go":
				if(args[2] == null) return false;
				plugin.databaseType().goHomeByAdmin(Bukkit.getPlayer(args[1]), sender, args[2]);
				break;

			case "delete":
				if(args[2] == null) return false;
				plugin.databaseType().deleteHomeByAdmin(Bukkit.getPlayer(args[1]), sender, args[2]);
				break;

			case "deleteall":
				plugin.databaseType().deleteAllByAdmin(Bukkit.getPlayer(args[1]), sender);
				break;

			case "list":
				plugin.databaseType().listHomesByAdmin(Bukkit.getPlayer(args[1]), sender);
				break;
			default:
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {

		if(cmd.getName().equalsIgnoreCase("exodushomesmanage")) {
			if(args.length == 1) {

				subcommands.add("help");
				subcommands.add("delete");
				subcommands.add("deleteall");
			}
		}

		return null;
	}
}
