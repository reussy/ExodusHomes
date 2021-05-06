package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    FileManager FManager = new FileManager();
    private ExodusHomes plugin;

    public MainCommand(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("homes.command.main")) {

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("Insufficient-Permission")
                    .replace("%prefix%", FManager.PX)));

            return false;
        }

        if (cmd.getName().equalsIgnoreCase("homes-admin")) {

            if (args.length == 0) {

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bExodusHomes &8&l- &7" + plugin.getDescription().getVersion()));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eCreated by &breussy"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUse &6/homes-admin help &efor commands!"));

                return false;
            }

            switch (args[0]) {

                case "help":

                    for (String Help : FManager.getLanguage().getStringList("Help-Administrator")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Help));
                    }

                    return false;

                case "reload":

                    plugin.reloadConfig();
                    FManager.reloadLanguage();
                    FManager.reloadMenus();
                    FManager.reloadStorage();

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("Reload-Message")
                            .replace("%prefix%", FManager.PX)));

                    return false;

                case "manage":

                    if (!(sender instanceof Player)) {

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("No-Console")
                                .replace("%prefix%", FManager.PX)));
                    }

                    return false;

                default:
            }
        }
        return false;
    }
}
