package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import org.bukkit.ChatColor;
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
    FileManager FManager = new FileManager();
    List<String> subcommands = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!sender.hasPermission("homes.command.admin")) {

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', FManager.getLanguage().getString("Insufficient-Permission")
                    .replace("%prefix%", FManager.PX)));

            return false;
        }

        if (cmd.getName().equalsIgnoreCase("home-admin")) {

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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("homes-admin")) {
            if (args.length == 1) {
                if (player.hasPermission("homes.command.admin")) {

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
