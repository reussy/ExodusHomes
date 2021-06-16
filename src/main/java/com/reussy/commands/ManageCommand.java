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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ManageCommand implements CommandExecutor, TabCompleter {

	private final ExodusHomes plugin;

	public ManageCommand(ExodusHomes plugin) {
		this.plugin = plugin;
	}

	FileManager fileManager = new FileManager();
	MessageUtils messageUtils = new MessageUtils();
	List<String> subcommands = new ArrayList<>();


	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

		if(!(sender instanceof Player)) {

			messageUtils.sendMessage(sender, fileManager.getMessage("No-Console"));

			return false;
		}

		if(plugin.getConfig().getBoolean("Permissions-System") && !sender.hasPermission("homes.command.manage")) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Insufficient-Permission"));

			return false;
		}

		if(cmd.getName().equalsIgnoreCase("ehm")) {

			if(args.length == 0) {

				sender.sendMessage(plugin.setHexColor("&bExodus Homes &8&l- &7" + plugin.getDescription().getVersion()));
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
			if(!args[0].equalsIgnoreCase("help")) {
				if(Bukkit.getPlayer(args[1]) == null) {

					messageUtils.sendMessage(sender, fileManager.getMessage("Unknown-Player").replace("%target%", args[1]));
					return false;
				}
			}
		}

		switch(args[0]) {

			case "help":

				for(String manageHelp : fileManager.getLang().getStringList("Help.Manage")) {
					sender.sendMessage(plugin.setHexColor(manageHelp));
				}

				break;

			case "import":

				Player player = Bukkit.getPlayer(args[1]);

				if(player == null) {

					sender.sendMessage(plugin.setHexColor("&cThis player does not exist or is not connected!"));
					return false;
				}


				try {
					new BukkitRunnable() {
						@Override
						public void run() {
							EssentialsStorageManager essentialsStorage = new EssentialsStorageManager(player.getUniqueId(), player, sender);
						}
					}.runTaskLaterAsynchronously(plugin, 20L);
				} catch(NullPointerException e) {
					sender.sendMessage("Cannot import homes! See the console and report this please.");
					e.printStackTrace();
				}
				break;

			case "list":

				if(!plugin.databaseType.hasHome(Bukkit.getPlayer(args[1]))) {

					messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", Bukkit.getPlayer(args[1]).getName()));

					return false;
				}

				plugin.databaseType().listHomesByAdmin(Bukkit.getPlayer(args[1]), sender);
				break;

			case "go":

				if(!plugin.databaseType.hasHome(Bukkit.getPlayer(args[1]))) {

					messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", Bukkit.getPlayer(args[1]).getName()));

					return false;
				}

				if(!plugin.databaseType.getHomes(Bukkit.getPlayer(args[1])).contains(args[2])) {

					messageUtils.sendMessage(sender, fileManager.getLang().getString("Manage.No-Home")
							.replace("%home_name%", args[2])
							.replace("%target%", Bukkit.getPlayer(args[1]).getName()));
					return false;
				}

				plugin.databaseType().goHomeByAdmin(Bukkit.getPlayer(args[1]), sender, args[2]);
				break;

			case "delete":

				if(!plugin.databaseType.hasHome(Bukkit.getPlayer(args[1]))) {

					messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", Bukkit.getPlayer(args[1]).getName()));

					return false;
				}

				if(!plugin.databaseType.getHomes(Bukkit.getPlayer(args[1])).contains(args[2])) {

					messageUtils.sendMessage(sender, fileManager.getLang().getString("Manage.No-Home")
							.replace("%home_name%", args[2])
							.replace("%target%", Bukkit.getPlayer(args[1]).getName()));
					return false;
				}

				new BukkitRunnable() {
					@Override
					public void run() {
						plugin.databaseType().deleteHomeByAdmin(Bukkit.getPlayer(args[1]), sender, args[2]);
					}
				}.runTaskAsynchronously(plugin);
				break;

			case "deleteall":

				if(!plugin.databaseType.hasHome(Bukkit.getPlayer(args[1]))) {

					messageUtils.sendMessage(sender, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", Bukkit.getPlayer(args[1]).getName()));

					return false;
				}

				new BukkitRunnable() {
					@Override
					public void run() {
						plugin.databaseType().deleteAllByAdmin(Bukkit.getPlayer(args[1]), sender);
					}
				}.runTaskAsynchronously(plugin);
				break;

			default:
				sender.sendMessage(plugin.setHexColor("&bExodus Homes &8&l- &7" + plugin.getDescription().getVersion()));
				sender.sendMessage(plugin.setHexColor("&eCreated by &breussy"));
				sender.sendMessage(plugin.setHexColor("&eUse &6/ehm help &efor commands!"));
				break;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {

		if(cmd.getName().equalsIgnoreCase("ehm")) {
			if(plugin.getConfig().getBoolean("Permissions-System") && sender.hasPermission("homes.command.manage") || !plugin.getConfig().getBoolean("Permissions-System") && !sender.hasPermission("homes.command.manage")) {
				if(args.length == 1) {

					subcommands.add("help");
					subcommands.add("import");
					subcommands.add("go");
					subcommands.add("list");
					subcommands.add("delete");
					subcommands.add("deleteall");

					return subcommands;
				} else if(args.length == 2) {
					List<String> onlinePlayers = new ArrayList<>();

					for(Player p : Bukkit.getOnlinePlayers()) {

						if(p == null) return null;

						onlinePlayers.add(p.getName());
					}
					return onlinePlayers;
				} else if(args.length == 3) {
					Player player = Bukkit.getPlayer(args[1]);

					if(player == null) return null;

					return plugin.databaseType().getHomes(player);
				}
			}
		}

		return null;
	}
}
