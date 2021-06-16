package com.reussy.commands;

import com.cryptomorin.xseries.XSound;
import com.reussy.ExodusHomes;
import com.reussy.gui.MainGUI;
import com.reussy.managers.FileManager;
import com.reussy.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommand implements CommandExecutor, TabCompleter {

	private final ExodusHomes plugin;

	public PlayerCommand(ExodusHomes plugin) {
		this.plugin = plugin;
	}

	FileManager fileManager = new FileManager();
	MessageUtils messageUtils = new MessageUtils();
	MainGUI mainGUI = new MainGUI();
	List<String> subcommands = new ArrayList<>();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

		if(!(sender instanceof Player)) {

			messageUtils.sendMessage(sender, fileManager.getMessage("No-Console"));

			return false;
		}

		if(plugin.usePermissions && !sender.hasPermission("homes.command.player")) {


			messageUtils.sendMessage(sender, fileManager.getMessage("Insufficient-Permission"));

			return false;
		}

		Player player = (Player) sender;

		if(!plugin.getConfig().getStringList("Whitelist-Worlds").contains(player.getWorld().getName())) {

			messageUtils.sendMessage(sender, fileManager.getMessage("Deny-World"));
			return false;
		}

		if(cmd.getName().equalsIgnoreCase("home")) {

			if(args.length == 0) {

				mainGUI.GUI(player);
				player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Open-GUI")).parseSound(), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
				return false;
			}

			if(args.length == 1 && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("deleteall") && !args[0].equalsIgnoreCase("gui")) {

				messageUtils.sendMessage(sender, fileManager.getMessage("Few-Arguments").replace("%cmd%", "home"));

				return false;
			}

			if(args.length > 2 && !args[0].equalsIgnoreCase("rename")) {

				messageUtils.sendMessage(sender, fileManager.getMessage("Many-Arguments"));
				return false;
			}
		}

		for(String disallowedNames : plugin.getConfig().getStringList("Blacklist-Names")) {

			if(args[0].equalsIgnoreCase("create")) {
				if(args[1].contains(disallowedNames)) {
					messageUtils.sendMessage(sender, fileManager.getMessage("Name-Not-Allowed"));
					return false;
				}
			}
		}

		switch(args[0]) {

			case "help":

				for(String helpPlayer : fileManager.getLang().getStringList("Help.Player")) {

					sender.sendMessage(plugin.setHexColor(helpPlayer));
				}
				player.playSound(player.getLocation(), XSound.BLOCK_LAVA_POP.parseSound(), 2, 2.5F);

				break;

			case "gui":

				mainGUI.GUI(player);
				player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Open-GUI")).parseSound(), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

				break;

			case "create":

				if(plugin.databaseType.getHomes(player).contains(args[1])) {

					messageUtils.sendMessage(player, fileManager.getMessage("Has-Home"));
					return false;
				}

				if(!plugin.getPerm(player)) {

					messageUtils.sendMessage(player, fileManager.getMessage("Limit-Reached"));
					return false;
				}

				new BukkitRunnable() {
					@Override
					public void run() {
						plugin.databaseType().createHome(player, args[1]);
					}
				}.runTaskAsynchronously(plugin);

				break;

			case "rename":

				if(!plugin.databaseType.getHomes(player).contains(args[1])) {

					messageUtils.sendMessage(player, fileManager.getMessage("No-Home").replace("%home_name%", args[1]));
					return false;
				}

				plugin.databaseType.setNewName(player, args[1], args[2]);

				break;

			case "delete":

				if(!plugin.databaseType.hasHome(player)) {

					messageUtils.sendMessage(player, fileManager.getMessage("Homes-Empty"));
					return false;
				}

				if(!plugin.databaseType.getHomes(player).contains(args[1])) {

					messageUtils.sendMessage(player, fileManager.getMessage("No-Home").replace("%home_name%", args[1]));
					return false;
				}

				new BukkitRunnable() {
					@Override
					public void run() {
						plugin.databaseType().deleteHome(player, args[1]);
					}
				}.runTaskAsynchronously(plugin);
				break;

			case "deleteall":

				if(!plugin.databaseType.hasHome(player)) {

					messageUtils.sendMessage(player, fileManager.getMessage("Homes-Empty"));
					return false;
				}

				new BukkitRunnable() {
					@Override
					public void run() {
						plugin.databaseType().deleteAll(player);
					}
				}.runTaskAsynchronously(plugin);
				break;

			case "go":

				if(!plugin.databaseType.hasHome(player)) {

					messageUtils.sendMessage(player, fileManager.getMessage("Homes-Empty"));

					return false;
				}

				if(!plugin.databaseType.getHomes(player).contains(args[1])) {

					messageUtils.sendMessage(player, fileManager.getMessage("No-Home").replace("%home_name%", args[1]));
					return false;
				}

				plugin.databaseType().goHome(player, args[1]);
				break;

			case "list":

				if(!plugin.databaseType.hasHome(player)) {

					messageUtils.sendMessage(player, fileManager.getMessage("Homes-Empty"));

					return false;
				}

				plugin.databaseType().listHomes(player);
				break;

			default:
				sender.sendMessage(plugin.setHexColor("&bExodus Homes &8&l- &7" + plugin.getDescription().getVersion()));
				sender.sendMessage(plugin.setHexColor("&eCreated by &breussy"));
				sender.sendMessage(plugin.setHexColor("&eUse &6/home help &efor commands!"));
				break;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {

		Player player = (Player) sender;
		List<String> getHomes = plugin.databaseType().getHomes(player);
		if(command.getName().equalsIgnoreCase("home")) {
			if(args.length == 1) {
				if(plugin.usePermissions && player.hasPermission("homes.command.player") || !plugin.usePermissions && !player.hasPermission("homes.command.player")) {

					subcommands.add("help");
					subcommands.add("gui");
					subcommands.add("create");
					subcommands.add("rename");
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
