package com.reussy;

import com.reussy.commands.MainCommand;
import com.reussy.commands.PlayerCommand;
import com.reussy.events.InventoryClickListener;
import com.reussy.events.PlayerDataListener;
import com.reussy.filemanager.FileManager;
import com.reussy.sql.SQLMain;
import com.reussy.utils.DatabaseType;
import com.reussy.utils.SQLType;
import com.reussy.utils.YamlType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

public final class ExodusHomes extends JavaPlugin {

	public boolean setFill = this.getConfig().getBoolean("Background.Fill");
	DatabaseType type;
	private SQLMain connect;

	public String setColor(String text) {

		return ChatColor.translateAlternateColorCodes('&', text);
	}

	@Override
	public void onEnable() {

		Commands();
		Events();
		try {
			Files();
		} catch(IOException e) {
			e.printStackTrace();
		}

		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lExodusHomes &8| &aEnabled "));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fAuthor: &a" + this.getDescription().getAuthors()));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fServer Version: &a" + Bukkit.getBukkitVersion()));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fPlugin Version: &a" + this.getDescription().getVersion()));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));

		if((getConfig().getString("Database-Type")).equalsIgnoreCase("MySQL")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lDatabase Type: &6MySQL"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTrying to connect to the database..."));
			connect = new SQLMain(getConfig().getString("MySQL.host"), getConfig().getInt("MySQL.port"), getConfig().getString("MySQL.database"), getConfig().getString("MySQL.username"), getConfig().getString("MySQL.password"));
			connect.createTable();
			type = new SQLType();
		} else if((getConfig().getString("Database-Type")).equalsIgnoreCase("YAML")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lDatabase Type: &6YAML"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eRegistering events..."));
			Bukkit.getPluginManager().registerEvents(new PlayerDataListener(), this);
			type = new YamlType();
		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lDatabase Type: " + getConfig().getString("Database-Type")));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat kind of database doesn't exist. Try YAML or MySQL"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDisabling plugin for avoid issues!"));
			Bukkit.getPluginManager().disablePlugin(this);
		}

		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));

	}

	@Override
	public void onDisable() {

		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lExodusHomes &8| &cDisabled "));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));
	}

	public void Commands() {

		this.getCommand("exodushomes").setExecutor(new MainCommand());
		this.getCommand("home").setExecutor(new PlayerCommand());
		this.getCommand("exodushomes").setTabCompleter(new MainCommand());
		this.getCommand("home").setTabCompleter(new PlayerCommand());

	}

	public void Events() {

		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
	}

	public void Files() throws IOException {

		FileManager fileManager = new FileManager();
		fileManager.generateFolder();
		fileManager.generateConfig();
		fileManager.generateLang();
		fileManager.generateGui();
	}

	public Connection getSQL() {

		return this.connect.getConnection();
	}

	public DatabaseType databaseType() {

		return type;
	}
}