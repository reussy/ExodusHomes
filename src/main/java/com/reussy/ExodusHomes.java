package com.reussy;

import com.reussy.commands.MainCommand;
import com.reussy.commands.ManageCommand;
import com.reussy.commands.PlayerCommand;
import com.reussy.events.InventoryClickListener;
import com.reussy.events.PlayerDataListener;
import com.reussy.filemanager.FileManager;
import com.reussy.sql.SQLMain;
import com.reussy.utils.DatabaseType;
import com.reussy.utils.SQLType;
import com.reussy.utils.YamlType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExodusHomes extends JavaPlugin {

	public boolean setFill = this.getConfig().getBoolean("Background.Enable");
	public ArrayList<String> playerCache = new ArrayList<>();
	DatabaseType type;
	private SQLMain connect;

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

		if(("MySQL".equalsIgnoreCase(getConfig().getString("Database-Type")))) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lDatabase Type: &6MySQL"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTrying to connect to the database..."));
			connect = new SQLMain(getConfig().getString("MySQL.host"), getConfig().getInt("MySQL.port"), getConfig().getString("MySQL.database"), getConfig().getString("MySQL.username"), getConfig().getString("MySQL.password"));
			connect.createTable();
			type = new SQLType();
		} else if(("YAML".equalsIgnoreCase(getConfig().getString("Database-Type")))) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lDatabase Type: &6YAML"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eRegistering events..."));
			Bukkit.getPluginManager().registerEvents(new PlayerDataListener(), this);
			type = new YamlType();
		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lDatabase Type: &6" + getConfig().getString("Database-Type")));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat kind of database doesn't exist. Try YAML or MySQL"));
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDisabling plugin for avoid issues!"));
			Bukkit.getPluginManager().disablePlugin(this);
		}

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ) Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bHooked into &6PlaceholderAPI"));

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

	public String setHexColor(String text) {

		final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");

		if(Bukkit.getVersion().contains("1.16")) {
			Matcher matcher = pattern.matcher(text);
			while(matcher.find()) {

				String setColor = text.substring(matcher.start(), matcher.end());
				text = text.replace(setColor, ChatColor.of(setColor) + "");
			}
		}

		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public void Commands() {

		this.getCommand("exodushomes").setExecutor(new MainCommand());
		this.getCommand("exodushomesmanage").setExecutor(new ManageCommand());
		this.getCommand("home").setExecutor(new PlayerCommand());
		this.getCommand("exodushomes").setTabCompleter(new MainCommand());
		this.getCommand("exodushomesmanage").setTabCompleter(new ManageCommand());
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

	public int getPermission(Player player) {

		if (player.isOp()) return -1;

		for(PermissionAttachmentInfo perms : player.getEffectivePermissions()) {

			String permission = perms.getPermission();

			if (player.hasPermission("homes.limit.unlimited")) return -1;

			if (permission.startsWith("homes.limit.")){

				return Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1));
			}
		}
		return 0;
	}
}