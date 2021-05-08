package com.reussy;

import com.reussy.commands.MainCommand;
import com.reussy.commands.PlayerCommand;
import com.reussy.events.InventoryClickListener;
import com.reussy.filemanager.FileManager;
import com.reussy.sql.SQLMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Connection;

public final class ExodusHomes extends JavaPlugin {

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
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &b&lExodusHomes &8| &aEnabled "));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &fAuthor: &8reussy"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &fServer: &8" + Bukkit.getBukkitVersion()));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7> &fVersion: &8" + this.getDescription().getVersion()));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));

		if(getConfig().getString("Database-Type").equalsIgnoreCase("MySQL")) {

			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lExodusHomes &edetected that the database is enabled!"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Trying to connect to the database..."));
			connect = new SQLMain(getConfig().getString("MySQL.host"), getConfig().getInt("MySQL.port"), getConfig().getString("MySQL.database"), getConfig().getString("MySQL.username"), getConfig().getString("MySQL.password"));
			connect.createTable();

		}

		if(Bukkit.getPluginManager().getPlugin("EssentialsX") != null) {

			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lExodusHomes &8| hooked to EssentialsX"));
		}

		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));

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

		this.getCommand("home-admin").setExecutor(new MainCommand());
		this.getCommand("home").setExecutor(new PlayerCommand());
		this.getCommand("home-admin").setTabCompleter(new MainCommand());
		this.getCommand("home").setTabCompleter(new PlayerCommand());

	}

	public void Events() {

		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
	}

	public void Files() throws IOException {

		FileManager FManager = new FileManager();

		FManager.generateConfig();
		FManager.generateLang();
		FManager.generateGui();
		FManager.generateStorage();
	}

	public Connection getSQL() {

		return this.connect.getConnection();
	}
}