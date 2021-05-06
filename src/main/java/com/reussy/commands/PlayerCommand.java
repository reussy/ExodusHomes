package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.menus.MainGUI;
import com.reussy.utils.ParticleDisplay;
import com.reussy.utils.XParticle;
import com.reussy.utils.XSound;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sql.SQLData;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommand implements CommandExecutor, TabCompleter {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
    FileManager FManager = new FileManager();
    MainGUI getGUI = new MainGUI();
    SQLData sqlData = new SQLData();
    List<String> subcommands = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("No-Console")
                    .replace("%prefix%", FManager.PX)));

            return false;
        }

        if (!sender.hasPermission("homes.command.player")) {

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("Insufficient-Permission")
                    .replace("%prefix%", FManager.PX)));

            return false;
        }

        Player player = (Player) sender;
        boolean hasHome = sqlData.hasHomes(plugin.getSQL(), player.getUniqueId());

        if (cmd.getName().equalsIgnoreCase("home")) {

            if (args.length == 0) {

                getGUI.openGUI(player);
                player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Open-GUI")).parseSound(), 1, 1);
                return false;
            }
        }

        List<String> getHomes = (sqlData.getHomes(plugin.getSQL(), player.getUniqueId()));
        String world = player.getWorld().getName();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        float pitch = player.getLocation().getPitch();
        float yaw = player.getLocation().getYaw();

        switch (args[0]) {

            case "set":

                if (plugin.getConfig().getString("Database-Type").equalsIgnoreCase("MySQL")) {

                    if (getHomes.contains(args[1])) {

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("Has-Home")
                                .replace("%prefix%", FManager.PX)));

                        return false;
                    }

                    sqlData.createHomes(plugin.getSQL(), player.getUniqueId(), player, world, args[1], x, y, z, pitch, yaw);
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("Home-Created")
                        .replace("%prefix%", FManager.PX).replace("%player_home%", args[1])));
                player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Create-Home")).parseSound(), 1, 1);
                XParticle.circle(2, 5, ParticleDisplay.display(player.getLocation(), Particle.valueOf(plugin.getConfig().getString("Particles.Create-Home"))));


                break;

            case "delete":

                if (plugin.getConfig().getString("Database-Type").equalsIgnoreCase("MySQL")) {

                    if (!getHomes.contains(args[1])) {

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("No-Home")
                                .replace("%prefix%", FManager.PX).replace("%player_home%", args[1])));

                        return false;
                    }

                    if (getHomes.contains(args[1])) {

                        sqlData.deleteHomes(plugin.getSQL(), player.getUniqueId(), args[1]);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("Home-Deleted")
                                .replace("%prefix%", FManager.PX).replace("%player_home%", args[1])));
                        player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Delete-Home")).parseSound(), 1, 1);

                    }
                }

                break;

            case "go":

                if (hasHome) {

                    if (!getHomes.contains(args[1])) {

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("No-Home")
                                .replace("%prefix%", FManager.PX)));

                        return false;
                    }

                    String getWorld = sqlData.getWorld(plugin.getSQL(), player.getUniqueId());
                    World World = Bukkit.getWorld(getWorld);
                    double getX = sqlData.getX(plugin.getSQL(), player.getUniqueId());
                    double getY = sqlData.getY(plugin.getSQL(), player.getUniqueId());
                    double getZ = sqlData.getZ(plugin.getSQL(), player.getUniqueId());
                    float getPitch = sqlData.getPitch(plugin.getSQL(), player.getUniqueId());
                    float getYaw = sqlData.getYaw(plugin.getSQL(), player.getUniqueId());
                    Location Home = new Location(World, getX, getY, getZ, getYaw, getPitch);

                    if (getHomes.contains(args[1])) {

                        player.teleport(Home);
                        assert World != null;
                        player.sendMessage(ChatColor.AQUA + "Teleported successfully to " + ChatColor.GRAY + World.getName() + ", " + getX + ", " + getY + ", " + getZ + ", " + getYaw + ", " + getPitch);
                        player.playSound(player.getLocation(), XSound.valueOf(plugin.getConfig().getString("Sounds.Teleport-Home")).parseSound(), 1, 1);
                        XParticle.circle(2, 5, ParticleDisplay.display(player.getLocation(), Particle.valueOf(plugin.getConfig().getString("Particles.Teleport-Home"))));
                    }
                }

                break;

            case "list":

                if (getHomes.isEmpty()){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("Homes-Empty")
                            .replace("%prefix%", FManager.PX)));
                }

                for (String homes : getHomes){

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("Homes-Format")
                            .replace("%prefix%", FManager.PX).replace("%player_homes%", homes)));
                }

                break;

            case "help":

                for (String Help : FManager.getLanguage().getStringList("Help-Player")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Help));
                }
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        Player player = (Player) sender;
        List<String> getHomes = sqlData.getHomes(plugin.getSQL(), player.getUniqueId());

        if (command.getName().equalsIgnoreCase("home")){
            if (args.length == 1){
                if (player.hasPermission("homes.command.player")){

                    subcommands.add("set");
                    subcommands.add("delete");
                    subcommands.add("go");
                    subcommands.add("list");
                }
            }

            return subcommands;
        }

        return null;
    }
}
