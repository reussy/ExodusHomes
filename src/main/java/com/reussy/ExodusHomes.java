package com.reussy;

import com.reussy.commands.MainCommand;
import com.reussy.commands.ManageCommand;
import com.reussy.commands.PlayerCommand;
import com.reussy.events.InventoryClickListener;
import com.reussy.events.PlayerCommandPreListener;
import com.reussy.events.PlayerDataListener;
import com.reussy.managers.DatabaseManager;
import com.reussy.managers.FileManager;
import com.reussy.managers.MenusFileManager;
import com.reussy.managers.yaml.Yaml;
import com.reussy.mysql.MySQL;
import com.reussy.mysql.MySQLConnector;
import com.reussy.utils.PlaceholdersBuilder;
import de.jeff_media.updatechecker.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExodusHomes extends JavaPlugin {

    public ExodusHomes plugin;
    public ArrayList<String> playerCache = new ArrayList<>();
    public List<String> adminCommands = new ArrayList<>();
    public List<String> manageCommands = new ArrayList<>();
    public List<String> playerCommands = new ArrayList<>();
    public DatabaseManager databaseManager;
    public Economy economy;
    private MySQLConnector mySQLConnector;

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bExodus Homes &8| &aEnabled "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fAuthor: &a" + this.getDescription().getAuthors()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fServer Version: &a" + Bukkit.getBukkitVersion()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&fPlugin Version: &a" + this.getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));

        Files();
        getDatabaseType();
        registerHooks();
        Events();
        Commands();
        UpdateChecker.init(this, 92900)
                .checkEveryXHours(1)
                .setDonationLink("https://www.buymeacoffee.com/reussy")
                .setFreeDownloadLink("https://rb.gy/qnegev")
                .setChangelogLink("https://rb.gy/a9grgr")
                .setNotifyOpsOnJoin(true)
                .checkNow();

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m------------------------------------------------"));

    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aExodus Homes disabled, goodbye!"));
    }

    public void Files() {

        FileManager fileManager = new FileManager(this);
        MenusFileManager menusFileManager = new MenusFileManager(this);
        fileManager.pluginFolders();
        fileManager.generateConfig();
        fileManager.generateLang();
        menusFileManager.generateOverview();
        menusFileManager.generatePortal();
    }

    public void getDatabaseType() {

        if (("MySQL".equalsIgnoreCase(getConfig().getString("Database-Type")))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDatabase Type: &fMySQL"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTrying to connect to the database..."));
            mySQLConnector = new MySQLConnector(getConfig().getString("MySQL.host"), getConfig().getInt("MySQL.port"), getConfig().getString("MySQL.database"), getConfig().getString("MySQL.username"), getConfig().getString("MySQL.password"), this.getConfig().getBoolean("MySQL.autoReconnect"));
            databaseManager = new MySQL();

        } else if (("YAML".equalsIgnoreCase(getConfig().getString("Database-Type")))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDatabase Type: &fYAML"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eRegistering events..."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
            Bukkit.getPluginManager().registerEvents(new PlayerDataListener(), this);
            databaseManager = new Yaml(this);

        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDatabase Type: &f" + getConfig().getString("Database-Type")));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat kind of database doesn't exist. Try YAML or MySQL"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDisabling plugin for avoid issues!"));
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void registerHooks() {

        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aHooked into &fEssentialsX"));
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholdersBuilder(this).register();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aHooked into &fPlaceholderAPI"));
        }

        if (setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aHooked into &fVault"));
        }
    }

    public void Commands() {

        this.getCommand("eh").setExecutor(new MainCommand(this));
        this.getCommand("ehm").setExecutor(new ManageCommand(this));
        this.getCommand("home").setExecutor(new PlayerCommand(this));
        this.getCommand("eh").setTabCompleter(new MainCommand(this));
        this.getCommand("ehm").setTabCompleter(new ManageCommand(this));
        this.getCommand("home").setTabCompleter(new PlayerCommand(this));

    }

    public void Events() {

        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreListener(this), this);
    }

    private boolean setupEconomy() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFailed to register Vault\n" +
                    "Some economy manager is needed, such as Essentials"));
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public Connection getConnection() {

        return mySQLConnector.getConnection();
    }

    public DatabaseManager getDatabaseManager() {

        return databaseManager;
    }

    public boolean getPerm(Player player) {

        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {

            String getPermission = permissionAttachmentInfo.getPermission();

            if (player.isOp()) return true;

            if (!this.getConfig().getBoolean("Permissions-System")) return true;

            if (getPermission.equalsIgnoreCase("homes.limit.*")) return true;

            if (getPermission.startsWith("homes.limit.")) {

                int homesLimit = Integer.parseInt(getPermission.substring(getPermission.lastIndexOf(".") + 1));

                if (this.databaseManager.getHomes(player).size() < homesLimit) return true;
            }
        }
        return false;
    }

    public String setHexColor(String text) {

        final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");

        if (Bukkit.getVersion().contains("1.16")) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {

                String setColor = text.substring(matcher.start(), matcher.end());
                text = text.replace(setColor, ChatColor.of(setColor) + "");
            }
        } else {
            return org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}