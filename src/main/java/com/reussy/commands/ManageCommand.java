package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.managers.EssentialsStorageManager;
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

                player.sendMessage(plugin.pluginUtils.setHexColor("&bExodus Homes &8&l- &7" + plugin.getDescription().getVersion()));
                player.sendMessage(plugin.pluginUtils.setHexColor("&eCreated by &breussy"));
                player.sendMessage(plugin.pluginUtils.setHexColor("&eUse &6/ehm help &efor commands!"));

                return false;
            }

            if (args.length == 1 && !args[0].equalsIgnoreCase("help")) {

                plugin.pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Few-Arguments").replace("%cmd%", "ehm"));

                return false;
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("go") || args.length == 2 && args[0].equalsIgnoreCase("delete")) {

                plugin.pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Few-Arguments").replace("%cmd%", "ehm"));

                return false;
            }
        }

        String offlinePlayerName = plugin.sqlManager.getPlayer(args[1]);

        if (offlinePlayerName == null) {

            plugin.pluginUtils.sendMessageWithPrefix(player, fileManager.getMessage("Unknown-Player").replace("%target%", args[1]));
            return false;
        }

        UUID offlineUUID = plugin.sqlManager.getUUID(offlinePlayerName);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(offlineUUID);

        switch (args[0]) {

            case "help":

                for (String manageHelp : fileManager.getLang().getStringList("Help.Manage")) {
                    sender.sendMessage(plugin.pluginUtils.setHexColor(manageHelp));
                }

                break;

            case "import":

                try {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            EssentialsStorageManager essentialsStorageManager = new EssentialsStorageManager(plugin, player, player.getUniqueId());
                            essentialsStorageManager.importPerPlayer();
                        }
                    }.runTaskLaterAsynchronously(plugin, 20L);
                } catch (Exception e) {
                    sender.sendMessage("Cannot import homes! See the console and report this please.");
                    e.printStackTrace();
                }
                break;

            case "list":

                if (!plugin.databaseManager.hasHome(Bukkit.getPlayer(args[1]))) {

                    plugin.pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName()));

                    return false;
                }

                plugin.databaseManager.listHomesByAdmin(Bukkit.getPlayer(args[1]), sender);
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
                        plugin.databaseManager.deleteHomeByAdmin(offlinePlayer, sender, args[2]);
                    }
                }.runTaskAsynchronously(plugin);
                break;

            case "deleteall":

                if (!plugin.databaseManager.hasHome(offlinePlayer)) {

                    plugin.pluginUtils.sendMessageWithPrefix(sender, fileManager.getMessage("Manage.Homes-Empty").replace("%target%", Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName()));

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
                sender.sendMessage(plugin.pluginUtils.setHexColor("&bExodus Homes &8&l- &7" + plugin.getDescription().getVersion()));
                sender.sendMessage(plugin.pluginUtils.setHexColor("&eCreated by &breussy"));
                sender.sendMessage(plugin.pluginUtils.setHexColor("&eUse &6/ehm help &efor commands!"));
                break;
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
                    String offlinePlayerName = plugin.sqlManager.getPlayer(args[1]);
                    UUID offlineUUID = plugin.sqlManager.getUUID(offlinePlayerName);
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(offlineUUID);

                    if (!plugin.sqlManager.hasHomes(offlinePlayer)) return null;

                    return plugin.databaseManager.getHomes(offlinePlayer);
                }
            }
        }

        return null;
    }
}