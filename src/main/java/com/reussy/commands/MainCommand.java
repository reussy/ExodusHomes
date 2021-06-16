package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.managers.FileManager;
import com.reussy.utils.MessageUtils;
import de.jeff_media.updatechecker.UpdateChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

	private final ExodusHomes plugin;

	public MainCommand(ExodusHomes plugin) {
		this.plugin = plugin;
	}

	FileManager fileManager = new FileManager();
	MessageUtils messageUtils = new MessageUtils();
	List<String> subcommands = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!sender.hasPermission("homes.command.admin")) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Insufficient-Permission"));

			return false;
		}

		if(cmd.getName().equalsIgnoreCase("eh")) {

			if(args.length == 0) {

				sender.sendMessage(plugin.setHexColor("&bExodus Homes &8&l- &7" + plugin.getDescription().getVersion()));
				sender.sendMessage(plugin.setHexColor("&eCreated by &breussy"));
				sender.sendMessage(plugin.setHexColor("&eUse &6/eh help &efor commands!"));

				return false;
			}
		}

		switch(args[0]) {

			case "help":

				sender.sendMessage(plugin.setHexColor("&8--------------------------------------"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor("&r                   &c&oAdmin Commands"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/eh help &8- &7Show this message"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/eh reload &8- &7Reload Configuration Files"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/eh update &8- &7Check for updates"));
				sender.sendMessage(plugin.setHexColor(" &8&l! &b/ehm &8- &7Manage Homes for Players"));
				sender.sendMessage(plugin.setHexColor("&r"));
				sender.sendMessage(plugin.setHexColor("&8--------------------------------------"));

				break;

			case "reload":

				plugin.reloadConfig();
				fileManager.reloadLang();
				fileManager.reloadGui();
				messageUtils.sendMessage(sender, fileManager.getMessage("Reload-Message"));

				return false;

			case "update":

				UpdateChecker.getInstance().checkNow(sender);
				break;

			default:
				sender.sendMessage(plugin.setHexColor("&bExodus Homes &8&l- &7" + plugin.getDescription().getVersion()));
				sender.sendMessage(plugin.setHexColor("&eCreated by &breussy"));
				sender.sendMessage(plugin.setHexColor("&eUse &6/eh help &efor commands!"));
				break;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {

		if(command.getName().equalsIgnoreCase("eh")) {
			if(args.length == 1) {
				if(sender.hasPermission("homes.command.admin")) {

					subcommands.add("help");
					subcommands.add("reload");
					subcommands.add("update");
				}
			}
			return subcommands;
		}
		return null;
	}
}
