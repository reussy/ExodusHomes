package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
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
	List<String> subcommands = new ArrayList<>();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

		if(!(sender instanceof Player)) {

			sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("No-Console")
					.replace("%prefix%", fileManager.PX)));

			return false;
		}

		if(!sender.hasPermission("homes.command.manage") || !sender.isOp()) {

			sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Insufficient-Permission")
					.replace("%prefix%", fileManager.PX)));

			return false;
		}

		if(cmd.getName().equalsIgnoreCase("exodushomesmanage")) {

			if(args.length == 0) {

				sender.sendMessage(plugin.setHexColor("&bExodusHomes &8&l- &7" + plugin.getDescription().getVersion()));
				sender.sendMessage(plugin.setHexColor("&eCreated by &breussy"));
				sender.sendMessage(plugin.setHexColor("&eUse &6/ehm help &efor commands!"));

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
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm deleteall <player> &8- &7Delete all homes a player!"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor("&8--------------------------------------"));

				break;

			case "delete":

				if(!args[1].equalsIgnoreCase(Bukkit.getPlayer(args[1]).getName())) {

					sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Few-Arguments")
							.replace("%prefix%", fileManager.PX)));

					return false;
				}

				plugin.databaseType().deleteHomeByAdmin(Bukkit.getPlayer(args[1]), args[2]);

				break;

			case "deleteall":

				if(!args[1].equalsIgnoreCase(Bukkit.getPlayer(args[1]).getName())) {

					sender.sendMessage(plugin.setHexColor(fileManager.getLang().getString("Few-Arguments")
							.replace("%prefix%", fileManager.PX)));

					return false;
				}

				plugin.databaseType().deleteAllByAdmin(Bukkit.getPlayer(args[1]));
				break;

			default:
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

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