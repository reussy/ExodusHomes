package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.gui.MainGUI;
import com.reussy.sql.SQLData;
import com.reussy.utils.ParticleDisplay;
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
		boolean hasHome = sqlData.hasHomes(plugin.getSQL(), player.getUniqueId());

		if(cmd.getName().equalsIgnoreCase("home")) {

			if(args.length == 0) {

				mainGUI.GUI(player);
				player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Open-GUI")).parseSound(), 1, 1);
				return false;
			}
		}

		List<String> getHomes = (sqlData.getHomes(plugin.getSQL(), player.getUniqueId()));
		String world = player.getWorld().getName();
		double x = player.getLocation().getBlockX();
		double y = player.getLocation().getBlockY();
		double z = player.getLocation().getBlockZ();
		float pitch = player.getLocation().getPitch();
		float yaw = player.getLocation().getYaw();

		switch(args[0]) {

			case "set":

				if(plugin.getConfig().getString("Database-Type").equalsIgnoreCase("MySQL")) {

					if(getHomes.contains(args[1])) {

						player.sendMessage(plugin.setColor(fileManager.getLang().getString("Has-Home")
								.replace("%prefix%", fileManager.PX)));

						return false;
					}

					sqlData.createHomes(plugin.getSQL(), player.getUniqueId(), player, world, args[1], x, y, z, pitch, yaw);
				}

				player.sendMessage(plugin.setColor(fileManager.getLang().getString("Home-Created")
						.replace("%prefix%", fileManager.PX).replace("%home_name%", args[1])));
				player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Create-Home")).parseSound(), 1, 1);
				XParticle.circle(5, 10, ParticleDisplay.display(player.getLocation(), Particle.valueOf(plugin.getConfig().getString("Particles.Create-Home"))));


				break;

			case "delete":

				if(plugin.getConfig().getString("Database-Type").equalsIgnoreCase("MySQL")) {

					if(!hasHome) {

						player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Empty")
								.replace("%prefix%", fileManager.PX)));

						return false;
					}

					if(!getHomes.contains(args[1])) {

						player.sendMessage(plugin.setColor(fileManager.getLang().getString("No-Home")
								.replace("%prefix%", fileManager.PX).replace("%home_name%", args[1])));

						return false;
					}

					if(getHomes.contains(args[1])) {

						sqlData.deleteHomes(plugin.getSQL(), player.getUniqueId(), args[1]);
						player.sendMessage(plugin.setColor(fileManager.getLang().getString("Home-Deleted")
								.replace("%prefix%", fileManager.PX).replace("%home_name%", args[1])));
						player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")).parseSound(), 1, 1);

					}
				}

				break;

			case "go":

				String getWorld = sqlData.getWorld(plugin.getSQL(), player.getUniqueId(), args[1]);
				World World = Bukkit.getWorld(getWorld);
				double getX = sqlData.getX(plugin.getSQL(), player.getUniqueId(), args[1]);
				double getY = sqlData.getY(plugin.getSQL(), player.getUniqueId(), args[1]);
				double getZ = sqlData.getZ(plugin.getSQL(), player.getUniqueId(), args[1]);
				float getPitch = sqlData.getPitch(plugin.getSQL(), player.getUniqueId(), args[1]);
				float getYaw = sqlData.getYaw(plugin.getSQL(), player.getUniqueId(), args[1]);
				Location Home = new Location(World, getX, getY, getZ, getYaw, getPitch);

				if(!hasHome) {

					player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Empty")
							.replace("%prefix%", fileManager.PX)));

					return false;
				}

				if(!getHomes.contains(args[1])) {

					player.sendMessage(plugin.setColor(fileManager.getLang().getString("No-Home")
							.replace("%prefix%", fileManager.PX).replace("%home_name%", args[1])));

					return false;
				}

				if(getHomes.contains(args[1])) {

					player.teleport(Home);
					assert World != null;
					player.sendMessage(plugin.setColor(fileManager.getLang().getString("Home-Teleport").replace("%home_name%", args[1])));
					player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Teleport-Home")).parseSound(), 1, 1);
					XParticle.circle(5, 10, ParticleDisplay.display(player.getLocation(), Particle.valueOf(plugin.getConfig().getString("Particles.Teleport-Home"))));
				}

				break;

			case "list":

				if(!hasHome) {

					player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Empty")
							.replace("%prefix%", fileManager.PX)));

					return false;
				}

				for(String homes : getHomes) {

					player.sendMessage(plugin.setColor(fileManager.getLang().getString("Homes-Format")
							.replace("%prefix%", fileManager.PX).replace("%home_name%", homes)));
				}

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

					subcommands.add("set");
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
