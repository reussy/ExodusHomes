package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.managers.FileManager;
import org.bukkit.Bukkit;
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
import java.util.UUID;

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

        FileManager fileManager = new FileManager(plugin);
        if (!(sender instanceof Player)) {

            plugin.pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("No-Console"));

            return false;
        }

        if (plugin.getConfig().getBoolean("Permissions-System") && !sender.hasPermission("homes.command.manage")) {

            plugin.pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Insufficient-Permission"));

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

                plugin.pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Few-Arguments").replace("%cmd%", "ehm"));

                return false;
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("go") || args.length == 2 && args[0].equalsIgnoreCase("delete")) {

                plugin.pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Few-Arguments").replace("%cmd%", "ehm"));

                return false;
            }

            String offlinePlayerName = plugin.databaseManager.getPlayer(args[1]);
            UUID offlineUUID = plugin.databaseManager.getUUID(offlinePlayerName);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(offlineUUID);

            switch (args[0]) {

                case "help":

                    for (String manageHelp : fileManager.getLang().getStringList("Help.Manage")) {
                        sender.sendMessage(plugin.pluginUtils.setHexColor(manageHelp));
                    }
                    break;

                case "import":

                    plugin.essentialsStorageManager.importPerPlayer(offlinePlayer.getUniqueId());
                    break;

                case "importall":

                    plugin.essentialsStorageManager.importEachPlayer();
                    break;

                case "list":

                    if (!plugin.databaseManager.hasHome(offlinePlayer)) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", args[1]));

                        return false;
                    }

                    plugin.databaseManager.listHomesByAdmin(offlinePlayer, sender);
                    break;

                case "go":

                    if (!plugin.databaseManager.hasHome(offlinePlayer)) {

                        plugin.pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", args[1]));

                        return false;
                    }

                    if (!plugin.databaseManager.getHomes(offlinePlayer).contains(args[2])) {

                        plugin.pluginUtils.sendMessageWithPrefix(player, Objects.requireNonNull(fileManager.getLang().getString("Manage.No-Home"))
                                .replace("%home_name%", args[2])
                                .replace("%target%", args[1]));
                        return false;
                    }

                    plugin.databaseManager.goHomeByAdmin(offlinePlayer, player, args[2]);
                    break;

                case "delete":

                    if (!plugin.databaseManager.hasHome(offlinePlayer)) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Manage.Homes-Empty")
                                .replace("%target%", args[1]));

                        return false;
                    }

                    if (!plugin.databaseManager.getHomes(offlinePlayer).contains(args[2])) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, Objects.requireNonNull(fileManager.getLang().getString("Manage.No-Home"))
                                .replace("%home_name%", args[2])
                                .replace("%target%", args[1]));
                        return false;
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            plugin.databaseManager.deleteHomeByAdmin(Bukkit.getPlayer(args[1]), sender, args[2]);
                        }
                    }.runTaskAsynchronously(plugin);
                    break;

                case "deleteall":

                    if (!plugin.databaseManager.hasHome(offlinePlayer)) {

                        plugin.pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", args[1]));

                        return false;
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            plugin.databaseManager.deleteAllByAdmin(offlinePlayer, sender);
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
                } else if (args.length == 2) {
                    List<String> onlinePlayers = new ArrayList<>();

                    for (Player p : Bukkit.getOnlinePlayers()) {

                        if (p == null) return null;

                        onlinePlayers.add(p.getName());
                    }
                    return onlinePlayers;
                } else if (args.length == 3) {

                    String offlinePlayerName = plugin.databaseManager.getPlayer(args[1]);
                    UUID offlineUUID = plugin.databaseManager.getUUID(offlinePlayerName);
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(offlineUUID);

                    if (!plugin.databaseManager.hasHome(offlinePlayer)) return null;

                    return plugin.databaseManager.getHomes(offlinePlayer);
                }
            }
        }

        return null;
    }
}