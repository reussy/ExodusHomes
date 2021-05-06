package com.reussy;

import com.reussy.commands.MainCommand;
import com.reussy.commands.PlayerCommand;
import com.reussy.filemanager.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import sql.SQLMain;

import java.io.IOException;
import java.sql.Connection;

public final class ExodusHomes extends JavaPlugin {

    private SQLMain connect;

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &b&lExodusHomes &8| &aEnabled "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &fAuthor: &8reussy"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &fServer: &8" + Bukkit.getBukkitVersion()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &fVersion: &8" + this.getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));

        if (getConfig().getString("Database-Type").equalsIgnoreCase("MySQL")) {

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lExodusHomes &edetected that the database is enabled!"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Trying to connect to the database..."));

            connect = new SQLMain(getConfig().getString("MySQL.host"), getConfig().getInt("MySQL.port"), getConfig().getString("MySQL.database"), getConfig().getString("MySQL.username"), getConfig().getString("MySQL.password"));
            connect.createTable();

        }

        if (Bukkit.getPluginManager().getPlugin("EssentialsX") != null) {

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lExodusHomes &8| hooked to EssentialsX"));
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));

        Commands();
        Events();
        try {
            Files();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &b&lExodusHomes &8| &cDisabled "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));
    }

    public void Commands() {

        this.getCommand("homes-admin").setExecutor(new MainCommand(this));
        this.getCommand("home").setExecutor(new PlayerCommand());

    }

    public void Events() {
    }

    public void Files() throws IOException {

        FileManager FManager = new FileManager();

        FManager.createConfig();
        FManager.createLanguage();
        FManager.createMenus();
        FManager.createStorage();
    }

    public Connection getSQL() {

        return this.connect.getConnection();
    }
}