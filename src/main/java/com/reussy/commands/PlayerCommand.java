package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.gui.MainGUI;
import com.reussy.sql.SQLData;
import com.reussy.utils.ParticleDisplay;
import com.reussy.utils.SQLType;
import com.reussy.utils.XParticle;
import com.reussy.utils.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommand implements CommandExecutor, TabCompleter {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();
	SQLData sqlData = new SQLData();
	MainGUI mainGUI = new MainGUI();
	List<String> subcommands = new ArrayList<>();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

		if(!(sender instanceof Player)) {

			sender.sendMessage(plugin.setColor(fileManager.getLang().getString("No-Console")
					.replace("%prefix%", fileManager.PX)));

			return false;
		}

		if(!sender.hasPermission("homes.command.player")) {

			sender.sendMessage(plugin.setColor(fileManager.getLang().getString("Insufficient-Permission")
					.replace("%prefix%", fileManager.PX)));

			return false;
		}

		Player player = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("home")) {

			if(args.length == 0) {

				mainGUI.GUI(player);
				player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Open-GUI")).parseSound(), 1, 1);
				return false;
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

				plugin.databaseType().getHomes(player);

				break;

			default:
				for(String Help : fileManager.getLang().getStringList("Help-Player")) {
					sender.sendMessage(plugin.setColor(Help));
				}
		}

		return false;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

		Player player = (Player) sender;
		List<String> getHomes = sqlData.getHomes(plugin.getSQL(), player.getUniqueId());

		if(command.getName().equalsIgnoreCase("home")) {
			if(args.length == 1) {
				if(player.hasPermission("homes.command.player")) {

					subcommands.add("create");
					subcommands.add("delete");
					subcommands.add("go");
					subcommands.add("list");
					return subcommands;
				}
			} else {

				return getHomes;
			}
		}
		return null;
	}
}
