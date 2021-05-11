package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();
	List<String> subcommands = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

		if(!sender.hasPermission("homes.command.admin")) {

			sender.sendMessage(plugin.setColor(fileManager.getLang().getString("Insufficient-Permission")
					.replace("%prefix%", fileManager.PX)));

			return false;
		}

		if(cmd.getName().equalsIgnoreCase("exodushomes")) {

			if(args.length == 0) {

				sender.sendMessage(plugin.setColor("&bExodusHomes &8&l- &7" + plugin.getDescription().getVersion()));
				sender.sendMessage(plugin.setColor("&eCreated by &breussy"));
				sender.sendMessage(plugin.setColor("&eUse &6/homes-admin help &efor commands!"));

				return false;
			}

			switch(args[0]) {

				case "help":

					for(String Help : fileManager.getLang().getStringList("Help-Administrator")) {
						sender.sendMessage(plugin.setColor(Help));
					}

					return false;

				case "reload":

					plugin.reloadConfig();
					fileManager.reloadLang();
					fileManager.reloadGui();

					sender.sendMessage(plugin.setColor(fileManager.getLang().getString("Reload-Message")
							.replace("%prefix%", fileManager.PX)));

					return false;

				case "manage":

					if(!(sender instanceof Player)) {

						sender.sendMessage(plugin.setColor(fileManager.getLang().getString("No-Console")
								.replace("%prefix%", fileManager.PX)));
					}

					return false;

				default:
			}
		}
		return false;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

		Player player = (Player) sender;

		if(command.getName().equalsIgnoreCase("exodushomes")) {
			if(args.length == 1) {
				if(player.hasPermission("homes.command.admin")) {

					subcommands.add("help");
					subcommands.add("reload");
					subcommands.add("manage");
				}
			}
			return subcommands;
		}
		return null;
	}
}
