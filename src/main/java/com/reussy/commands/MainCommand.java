package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.managers.FileManager;
import com.reussy.managers.MenusFileManager;
import de.jeff_media.updatechecker.UpdateChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final ExodusHomes plugin;

    /**
     * @param plugin main class
     */
    public MainCommand(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        FileManager fileManager = new FileManager(plugin);
        MenusFileManager menusFileManager = new MenusFileManager(plugin);
        if (plugin.getConfig().getBoolean("Permissions-System") && !sender.hasPermission("homes.command.admin")) {

            plugin.pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Insufficient-Permission"));

            return false;
        }

        if (cmd.getName().equalsIgnoreCase("eh")) {

            if (args.length == 0) {

                sender.sendMessage(plugin.pluginUtils.setHexColor("&bExodus Homes &8&l- &7" + plugin.getDescription().getVersion()));
                sender.sendMessage(plugin.pluginUtils.setHexColor("&eCreated by &breussy"));
                sender.sendMessage(plugin.pluginUtils.setHexColor("&eUse &6/eh help &efor commands!"));

                return false;
            }
        }

        switch (args[0]) {

            case "help":

                for (String adminHelp : fileManager.getLang().getStringList("Help.Administrator")) {

                    sender.sendMessage(plugin.pluginUtils.setHexColor(adminHelp));
                }

                break;

            case "reload":

                plugin.reloadConfig();
                fileManager.reloadLang();
                menusFileManager.reloadOverview();
                menusFileManager.reloadPortal();
                plugin.pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Reload-Message"));

                return false;

            case "update":

                UpdateChecker.getInstance().checkNow(sender);
                break;

            default:
                sender.sendMessage(plugin.pluginUtils.setHexColor("&bExodus Homes &8&l- &7" + plugin.getDescription().getVersion()));
                sender.sendMessage(plugin.pluginUtils.setHexColor("&eCreated by &breussy"));
                sender.sendMessage(plugin.pluginUtils.setHexColor("&eUse &6/eh help &efor commands!"));
                break;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {

        if (command.getName().equalsIgnoreCase("eh")) {
            if (args.length == 1) {
                if (plugin.getConfig().getBoolean("Permissions-System") && sender.hasPermission("homes.command.admin") || !plugin.getConfig().getBoolean("Permissions-System") && !sender.hasPermission("homes.command.admin")) {

                    plugin.adminCommands.add("help");
                    plugin.adminCommands.add("reload");
                    plugin.adminCommands.add("update");
                }
            }
            return plugin.adminCommands;
        }
        return null;
    }
}
