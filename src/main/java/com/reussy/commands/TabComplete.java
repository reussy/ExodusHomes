package com.reussy.commands;

import com.reussy.ExodusHomes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sql.SQLData;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
    SQLData sqlData = new SQLData();
    List<String> subcommands = new ArrayList<>();

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        Player player = (Player) sender;
        List<String> getHomes = sqlData.getHomes(plugin.getSQL(), player.getUniqueId());

        if (command.getName().equalsIgnoreCase("homes-admin")) {
            if (args.length == 1) {
                if (!subcommands.isEmpty()) {
                    if (player.hasPermission("homes.command.admin")) {

                        subcommands.add("help");
                        subcommands.add("reload");
                        subcommands.add("manage");
                    }
                }
            } return subcommands;
        } else if (command.getName().equalsIgnoreCase("home")) {
            if (args.length == 1) {
                if (sender.hasPermission("homes.command.homes")) {

                    subcommands.add("set");
                    subcommands.add("delete");
                    subcommands.add("go");
                    subcommands.add("list");
                }
            } else if (args[2].equalsIgnoreCase("delete") || args[2].equalsIgnoreCase("go")) return getHomes;
        }
        return null;

    }
}
