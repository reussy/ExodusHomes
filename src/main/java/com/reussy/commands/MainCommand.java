package com.reussy.commands;

import com.reussy.ExodusHomes;
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

        if (plugin.getConfig().getBoolean("Permissions-System") && !sender.hasPermission("homes.command.admin")) {

            plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getMessage("Insufficient-Permission"));

            return false;
        }

        if (cmd.getName().equalsIgnoreCase("eh")) {

            if (args.length == 0) {
                sender.sendMessage(plugin.pluginUtils.setHexColor("&bExodus Homes &8- &a&o" + plugin.getDescription().getVersion()));
                sender.sendMessage(plugin.pluginUtils.setHexColor("&7Use &6/eh help &7for commands!"));
                return false;
            }
        }

        switch (args[0]) {

            case "help":

                for (String adminHelp : plugin.fileManager.getLang().getStringList("Help.Administrator")) {

                    sender.sendMessage(plugin.pluginUtils.setHexColor(adminHelp));
                }

                break;

            case "reload":

                plugin.reloadConfig();
                plugin.fileManager.reloadLang();
                plugin.menusFileManager.reloadOverview();
                plugin.menusFileManager.reloadPortal();
                plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getMessage("Reload-Message"));
                return false;

            case "update":

                UpdateChecker.getInstance().checkNow(sender);
                break;

            default:
                sender.sendMessage(plugin.pluginUtils.setHexColor("&bExodus Homes &8- &a&o" + plugin.getDescription().getVersion()));
                sender.sendMessage(plugin.pluginUtils.setHexColor("&7Use &6/eh help &7for commands!"));
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
