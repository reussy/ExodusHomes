package com.reussy;

import com.reussy.commands.MainCommand;
import com.reussy.commands.ManageCommand;
import com.reussy.commands.PlayerCommand;
import com.reussy.events.InventoryClickListener;
import com.reussy.events.PlayerDataListener;
import com.reussy.managers.FileManager;
import com.reussy.sql.SQLMain;
import com.reussy.utils.DatabaseType;
import com.reussy.utils.SQLType;
import com.reussy.utils.YamlType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExodusHomes extends JavaPlugin {

	public ExodusHomes plugin;
	public DatabaseType type;
	public boolean setFill = this.getConfig().getBoolean("Background.Enable");
	public ArrayList<String> playerCache = new ArrayList<>();
	private SQLMain connect;

	@Override
	public void onEnable() {

		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lExodusHomes &8| &aEnabled "));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fAuthor: &a" + this.getDescription().getAuthors()));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fServer Version: &a" + Bukkit.getBukkitVersion()));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fPlugin Version: &a" + this.getDescription().getVersion()));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));

		plugin = this;
		Files();
		getDatabaseType();
		Events();
		Commands();

		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bHooked into &6PlaceholderAPI"));

		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));

	}

	@Override
	public void onDisable() {

		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aExodusHomes disabled, goodbye!"));
	}

	public void getDatabaseType() {

		if(("MySQL".equalsIgnoreCase(getConfig().getString("Database-Type")))) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lDatabase Type: &6MySQL"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTrying to connect to the database..."));
			connect = new SQLMain(getConfig().getString("MySQL.host"), getConfig().getInt("MySQL.port"), getConfig().getString("MySQL.database"), getConfig().getString("MySQL.username"), getConfig().getString("MySQL.password"));
			connect.createTable();
			type = new SQLType();

		} else if(("YAML".equalsIgnoreCase(getConfig().getString("Database-Type")))) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lDatabase Type: &6YAML"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eRegistering events..."));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
			Bukkit.getPluginManager().registerEvents(new PlayerDataListener(), this);
			type = new YamlType();

		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lDatabase Type: &6" + getConfig().getString("Database-Type")));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat kind of database doesn't exist. Try YAML or MySQL"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDisabling plugin for avoid issues!"));
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	public void Files() {

		FileManager fileManager = new FileManager();
		fileManager.generateFolder();
		fileManager.generateConfig();
		fileManager.generateLang();
		fileManager.generateGui();
	}

	public void Commands() {

		this.getCommand("eh").setExecutor(new MainCommand());
		this.getCommand("ehm").setExecutor(new ManageCommand());
		this.getCommand("home").setExecutor(new PlayerCommand());
		this.getCommand("eh").setTabCompleter(new MainCommand());
		this.getCommand("ehm").setTabCompleter(new ManageCommand());
		this.getCommand("home").setTabCompleter(new PlayerCommand());

	}

	public void Events() {

		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
	}

	public String setHexColor(String text) {

		final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");

		if(Bukkit.getVersion().contains("1.16")) {
			Matcher matcher = pattern.matcher(text);
			while(matcher.find()) {

				String setColor = text.substring(matcher.start(), matcher.end());
				text = text.replace(setColor, ChatColor.of(setColor) + "");
			}
		} else {

			return org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
		}

		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public Connection getSQL() {

		return this.connect.getConnection();
	}

	public DatabaseType databaseType() {

		return type;
	}

	public int getPermission(Player player) {

		for(PermissionAttachmentInfo perms : player.getEffectivePermissions()) {

			String permission = perms.getPermission();

			if(player.isOp()) return -1;

			if(permission.equalsIgnoreCase("homes.limit.unlimited")) return -1;

			if(permission.startsWith("homes.limit.")) {

				return Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1));
			} else {

				return 0;
			}
		}
		return 0;
	}
}