package com.reussy.commands;

import com.reussy.ExodusHomes;
import com.reussy.gui.OverviewGUI;
import com.reussy.gui.PortalGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PlayerCommand implements CommandExecutor, TabCompleter {

    private final ExodusHomes plugin;

    public PlayerCommand(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            plugin.pluginUtils.sendMessageWithoutPrefix(sender, plugin.fileManager.getLang().getString("No-Console"));
            return false;
        }

        if (plugin.getConfig().getBoolean("Permissions-System") && !sender.hasPermission("homes.command.player")) {
            plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Insufficient-Permission"));
            return false;
        }

        Player player = (Player) sender;

        if (plugin.getConfig().getBoolean("World-System.Enabled")
                && Objects.requireNonNull(plugin.getConfig().getString("World-System.Type")).equalsIgnoreCase("WHITELIST")
                && !plugin.getConfig().getStringList("World-System.Worlds").contains(player.getWorld().getName())) {

            plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Deny-World"));
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("home")) {

            if (args.length == 0) {

                if (plugin.getConfig().getBoolean("Portal-GUI-Instead")) {
                    player.openInventory(new PortalGUI(plugin, player).getInventory());
                } else {
                    player.openInventory(new OverviewGUI(plugin, player).getInventory());
                }
                plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Open-GUI"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
                return false;
            }

            if (args.length == 1 && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("deleteall") && !args[0].equalsIgnoreCase("gui")) {
                plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Few-Arguments").replace("%cmd%", "home"));
                return false;
            }

            if (args.length > 2 && !args[0].equalsIgnoreCase("rename")) {
                plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Many-Arguments"));
                return false;
            }
        }

        for (String disallowedNames : plugin.getConfig().getStringList("Blacklist-Names")) {

            if (args[0].equalsIgnoreCase("create")) {
                if (args[1].contains(disallowedNames)) {
                    plugin.pluginUtils.sendMessageWithPrefix(sender, plugin.fileManager.getLang().getString("Name-Not-Allowed"));
                    return false;
                }
            }
        }

        switch (args[0]) {

            case "help":

                for (String helpPlayer : plugin.fileManager.getLang().getStringList("Help.Player")) {

                    plugin.pluginUtils.sendMessageWithoutPrefix(sender, helpPlayer);
                }
                plugin.pluginUtils.sendSound(player, player.getLocation(), "BLOCK_LAVA_POP", 1, 3);

                break;

            case "gui":

                if (plugin.getConfig().getBoolean("Portal-GUI-Instead")) {
                    player.openInventory(new PortalGUI(plugin, player).getInventory());
                } else {
                    player.openInventory(new OverviewGUI(plugin, player).getInventory());
                }
                plugin.pluginUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Open-GUI"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));

                break;

            case "create":
                if (plugin.getDatabaseManager().getHomes(player).contains(args[1])) {
                    plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Has-Home"));
                    return false;
                }

                if (!plugin.pluginUtils.getPerm(player)) {
                    plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Limit-Reached"));
                    return false;
                }

                if (plugin.getConfig().getBoolean("Economy-System.Enabled") && plugin.economyManager.isEnoughMoney(player, plugin.getConfig().getInt("Economy-System.Create-Home")))
                    return false;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getDatabaseManager().createHome(player, args[1]);
                    }
                }.runTaskAsynchronously(plugin);

                break;

            case "rename":

                if (!plugin.getDatabaseManager().getHomes(player).contains(args[1])) {

                    plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("No-Home").replace("%home_name%", args[1]));
                    return false;
                }

                if (plugin.getConfig().getBoolean("Economy-System.Enabled")
                        && plugin.economyManager.isEnoughMoney(player, plugin.getConfig().getInt("Economy-System.Rename-Home"))) {
                    return false;
                }

                plugin.getDatabaseManager().setNewName(player, args[1], args[2]);

                break;

            case "delete":

                if (!plugin.getDatabaseManager().hasHome(player)) {

                    plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Homes-Empty"));
                    return false;
                }

                if (!plugin.getDatabaseManager().getHomes(player).contains(args[1])) {

                    plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("No-Home").replace("%home_name%", args[1]));
                    return false;
                }

                if (plugin.getConfig().getBoolean("Economy-System.Enabled") && plugin.economyManager.isEnoughMoney(player, plugin.getConfig().getInt("Economy-System.Delete-Home")))
                    return false;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getDatabaseManager().deleteHome(player, args[1]);
                    }
                }.runTaskAsynchronously(plugin);
                break;

            case "deleteall":

                if (!plugin.databaseManager.hasHome(player)) {

                    plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Homes-Empty"));
                    return false;
                }

                if (plugin.getConfig().getBoolean("Economy-System.Enabled") && plugin.economyManager.isEnoughMoney(player, plugin.getConfig().getInt("Economy-System.Delete-All-Homes")))
                    return false;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getDatabaseManager().deleteAll(player);
                    }
                }.runTaskAsynchronously(plugin);
                break;

            case "go":

                if (!plugin.getDatabaseManager().hasHome(player)) {

                    plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Homes-Empty"));

                    return false;
                }

                if (!plugin.getDatabaseManager().getHomes(player).contains(args[1])) {

                    plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("No-Home").replace("%home_name%", args[1]));
                    return false;
                }

                if (plugin.getConfig().getBoolean("Economy-System.Enabled") && plugin.economyManager.isEnoughMoney(player, plugin.getConfig().getInt("Economy-System.Teleport-Home")))
                    return false;

                plugin.getDatabaseManager().goHome(player, args[1]);
                break;

            case "list":

                if (!plugin.getDatabaseManager().hasHome(player)) {

                    plugin.pluginUtils.sendMessageWithPrefix(player, plugin.fileManager.getLang().getString("Homes-Empty"));

                    return false;
                }

                plugin.getDatabaseManager().listHomes(player);
                break;

            default:
                sender.sendMessage(plugin.pluginUtils.setHexColor("&bExodus Homes &8- &a&o" + plugin.getDescription().getVersion()));
                sender.sendMessage(plugin.pluginUtils.setHexColor("&7Use &6/home help &7for commands!"));
                break;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("home")) {
            if (args.length == 1) {
                if (plugin.getConfig().getBoolean("Permissions-System")
                        && player.hasPermission("homes.command.player")
                        || !plugin.getConfig().getBoolean("Permissions-System")
                        && !player.hasPermission("homes.command.player")
                        || player.isOp()) {

                    plugin.playerCommands.add("help");
                    plugin.playerCommands.add("gui");
                    plugin.playerCommands.add("create");
                    plugin.playerCommands.add("rename");
                    plugin.playerCommands.add("delete");
                    plugin.playerCommands.add("deleteall");
                    plugin.playerCommands.add("go");
                    plugin.playerCommands.add("list");
                }
                return plugin.playerCommands;
            } else if (!args[0].equalsIgnoreCase("help")
                    && !args[0].equalsIgnoreCase("gui")
                    && !args[0].equalsIgnoreCase("list")
                    && !args[0].equalsIgnoreCase("deleteall") && args.length == 2) {

                if (!plugin.databaseManager.hasHome(player)) return null;

                return plugin.databaseManager.getHomes(player);
            } else {
                return null;
            }
        }
        return null;
    }
}