package com.reussy.commands;

import com.cryptomorin.xseries.messages.ActionBar;
import com.reussy.ExodusHomes;
import com.reussy.managers.EssentialsStorageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageCommand implements CommandExecutor, TabCompleter {

    private final ExodusHomes plugin;

    /**
     * @param plugin main class
     */
    public ManageCommand(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        EssentialsStorageManager essentialsStorageManager = new EssentialsStorageManager(plugin);
        if (!(sender instanceof Player)) {

            plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("No-Console"));

            return false;
        }

        if (plugin.getConfig().getBoolean("Permissions-System") && !sender.hasPermission("homes.command.manage")) {

            plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Insufficient-Permission"));

            return false;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("ehm")) {

            if (args.length == 0) {
                sender.sendMessage(plugin.pluginUtils.setHexColor("&bExodus Homes &8- &a&o" + plugin.getDescription().getVersion()));
                sender.sendMessage(plugin.pluginUtils.setHexColor("&7Use &6/emh help &7for commands!"));
                return false;
            }

            if (args.length == 1 && !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("importall")) {

                plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Few-Arguments").replace("%cmd%", "ehm"));

                return false;
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("go") || args.length == 2 && args[0].equalsIgnoreCase("delete")) {

                plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Few-Arguments").replace("%cmd%", "ehm"));

                return false;
            }
            switch (args[0]) {

                case "help":

                    for (String manageHelp : plugin.fileManager.getLang().getStringList("Help.Manage")) {
                        sender.sendMessage(plugin.pluginUtils.setHexColor(manageHelp));
                    }
                    break;

                case "import":

                    if (plugin.databaseManager.getOfflinePlayer(args[1], sender) == null) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Unknown-Player")
                                .replace("%target%", args[1]));
                        return false;
                    }

                    ActionBar.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', "&c&lSee the console"));
                    plugin.pluginUtils.sendSound(player, player.getLocation(), "ENTITY_ARROW_HIT", 2, 4);
                    essentialsStorageManager.importPerPlayer(plugin.databaseManager.getOfflinePlayer(args[1], sender).getUniqueId());
                    break;

                case "importall":

                    ActionBar.sendActionBar(player, ChatColor.translateAlternateColorCodes('&', "&c&lSee the console"));
                    plugin.pluginUtils.sendSound(player, player.getLocation(), "ENTITY_ARROW_HIT", 2, 4);
                    essentialsStorageManager.importEachPlayer();
                    break;

                case "list":

                    if (plugin.databaseManager.getOfflinePlayer(args[1], sender) == null) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Unknown-Player")
                                .replace("%target%", args[1]));
                        return false;
                    }

                    if (!plugin.databaseManager.hasHome(plugin.databaseManager.getOfflinePlayer(args[1], sender))) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Manage.Homes-Empty").replace("%target%", args[1]));

                        return false;
                    }

                    plugin.databaseManager.listHomesByAdmin(plugin.databaseManager.getOfflinePlayer(args[1], sender), sender);
                    break;

                case "go":

                    if (plugin.databaseManager.getOfflinePlayer(args[1], sender) == null) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Unknown-Player")
                                .replace("%target%", args[1]));
                        return false;
                    }

                    if (!plugin.databaseManager.hasHome(plugin.databaseManager.getOfflinePlayer(args[1], sender))) {

                        plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Manage.Homes-Empty").replace("%target%", args[1]));

                        return false;
                    }

                    if (!plugin.databaseManager.getHomes(plugin.databaseManager.getOfflinePlayer(args[1], sender)).contains(args[2])) {

                        plugin.pluginUtils.sendMessageWithPrefix(player, Objects.requireNonNull(plugin.fileManager.getLang().getString("Manage.No-Home"))
                                .replace("%home_name%", args[2])
                                .replace("%target%", args[1]));
                        return false;
                    }

                    plugin.databaseManager.goHomeByAdmin(plugin.databaseManager.getOfflinePlayer(args[1], sender), player, args[2]);
                    break;

                case "delete":

                    if (plugin.databaseManager.getOfflinePlayer(args[1], sender) == null) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Unknown-Player")
                                .replace("%target%", args[1]));
                        return false;
                    }

                    if (!plugin.databaseManager.hasHome(plugin.databaseManager.getOfflinePlayer(args[1], sender))) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Manage.Homes-Empty")
                                .replace("%target%", args[1]));

                        return false;
                    }

                    if (!plugin.databaseManager.getHomes(plugin.databaseManager.getOfflinePlayer(args[1], sender)).contains(args[2])) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, Objects.requireNonNull(plugin.fileManager.getLang().getString("Manage.No-Home"))
                                .replace("%home_name%", args[2])
                                .replace("%target%", args[1]));
                        return false;
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            plugin.databaseManager.deleteHomeByAdmin(plugin.databaseManager.getOfflinePlayer(args[1], sender), sender, args[2]);
                        }
                    }.runTaskAsynchronously(plugin);
                    break;

                case "deleteall":

                    if (!plugin.databaseManager.hasHome(plugin.databaseManager.getOfflinePlayer(args[1], sender))) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Manage.Homes-Empty").replace("%target%", args[1]));

                        return false;
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            plugin.databaseManager.deleteAllByAdmin(plugin.databaseManager.getOfflinePlayer(args[1], sender), sender);
                        }
                    }.runTaskAsynchronously(plugin);
                    break;

                default:
                    sender.sendMessage(plugin.pluginUtils.setHexColor("&bExodus Homes &8- &a&o" + plugin.getDescription().getVersion()));
                    sender.sendMessage(plugin.pluginUtils.setHexColor("&7Use &6/emh help &7for commands!"));
                    break;
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("ehm")) {
            if (plugin.getConfig().getBoolean("Permissions-System") && sender.hasPermission("homes.command.manage") || !plugin.getConfig().getBoolean("Permissions-System") && !sender.hasPermission("homes.command.manage")) {
                if (args.length == 1) {

                    plugin.manageCommands.add("help");
                    plugin.manageCommands.add("import");
                    plugin.manageCommands.add("importall");
                    plugin.manageCommands.add("go");
                    plugin.manageCommands.add("list");
                    plugin.manageCommands.add("delete");
                    plugin.manageCommands.add("deleteall");

                    return plugin.manageCommands;
                } else if (!args[0].equalsIgnoreCase("help")
                        && !args[0].equalsIgnoreCase("importall")
                        && !args[0].equalsIgnoreCase("deleteall")
                        && args.length == 2) {

                    List<String> playerNames = new ArrayList<>();

                    if (plugin.getConfig().getBoolean("Tab-Complete.Fetch-Online-Players")) {

                        for (Player player : Bukkit.getOnlinePlayers()) {

                            playerNames.add(player.getName());
                        }
                    } else {
                        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {

                            if (player.isOnline()) continue;
                            playerNames.add(player.getName());
                        }
                    }
                    return playerNames;

                } else if (!args[0].equalsIgnoreCase("help")
                        && !args[0].equalsIgnoreCase("importall")
                        && !args[0].equalsIgnoreCase("deleteall")
                        && args.length == 3) {

                    try {

                        OfflinePlayer offlinePlayer = plugin.databaseManager.getOfflinePlayer(args[1], sender);

                        if (offlinePlayer == null) return null;

                        if (!plugin.databaseManager.hasHome(offlinePlayer)) return null;

                        return plugin.databaseManager.getHomes(offlinePlayer);

                    } catch (IllegalArgumentException e) {
                        e.getCause();
                    }
                }
            }
        }

        return null;
    }
}