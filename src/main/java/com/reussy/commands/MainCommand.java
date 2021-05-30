package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.managers.EssentialsStorageManager;
import com.reussy.managers.FileManager;
import com.reussy.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
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

				sender.sendMessage(plugin.setHexColor("&bExodusHomes &8&l- &7" + plugin.getDescription().getVersion()));
				sender.sendMessage(plugin.setHexColor("&eCreated by &breussy"));
				sender.sendMessage(plugin.setHexColor("&eUse &6/eh help &efor commands!"));

				return false;
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("import")) {

				messageUtils.sendMessage(sender, fileManager.getMessage("Few-Arguments").replace("%cmd%", "eh"));
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

			case "import":

				Player player = Bukkit.getPlayer(args[1]);

				if(player == null) {

					sender.sendMessage(plugin.setHexColor("&cThis player does not exist or is not connected!"));
					return false;
				}

				EssentialsStorageManager essentialsStorage = new EssentialsStorageManager(player.getUniqueId());

				try {
					new BukkitRunnable() {
						@Override
						public void run() {
							essentialsStorage.importHomes(player.getUniqueId(), player, sender);
						}
					}.runTaskLaterAsynchronously(plugin, 20L);
				} catch(NullPointerException e) {
					sender.sendMessage("Cannot import homes! See the console and report this please.");
					e.printStackTrace();
				}

			default:
				sender.sendMessage(plugin.setHexColor("&bExodusHomes &8&l- &7" + plugin.getDescription().getVersion()));
				sender.sendMessage(plugin.setHexColor("&eCreated by &breussy"));
				sender.sendMessage(plugin.setHexColor("&eUse &6/eh help &efor commands!"));
				break;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		Player player = (Player) sender;

		if(command.getName().equalsIgnoreCase("exodushomes")) {
			if(args.length == 1) {
				if(player.hasPermission("homes.command.admin")) {

					subcommands.add("help");
					subcommands.add("reload");
					subcommands.add("import");
				}
			}
			return subcommands;
		}
		return null;
	}
}
