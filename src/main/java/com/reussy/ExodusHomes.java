package com.reussy;

import com.reussy.commands.MainCommand;
import com.reussy.commands.ManageCommand;
import com.reussy.commands.PlayerCommand;
import com.reussy.databases.DatabaseManager;
import com.reussy.databases.sql.MySQL;
import com.reussy.databases.sql.SQLManager;
import com.reussy.databases.yaml.Yaml;
import com.reussy.events.InventoryClickListener;
import com.reussy.events.PlayerCommandPreListener;
import com.reussy.managers.FileManager;
import com.reussy.managers.MenusFileManager;
import com.reussy.managers.StorageManager;
import com.reussy.utils.ItemBuilder;
import com.reussy.utils.PlaceholdersBuilder;
import com.reussy.utils.PluginUtils;
import de.jeff_media.updatechecker.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ExodusHomes extends JavaPlugin {

    public FileManager fileManager;
    public MenusFileManager menusFileManager;
    public StorageManager storageManager;
    public ItemBuilder itemBuilder;
    public PluginUtils pluginUtils;
    public MySQL mySQL;
    public Yaml yaml;
    public List<String> playerCache;
    public List<String> adminCommands;
    public List<String> manageCommands;
    public List<String> playerCommands;
    public SQLManager sqlManager;
    public DatabaseManager databaseManager;
    public Economy economy;

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m+------------------------------------------------+"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &bExodus Homes &8| &aEnabled "));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &7Server Version: &a" + Bukkit.getServer().getBukkitVersion()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &7Plugin Version: &a" + this.getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &fThanks for download &8- &d" + this.getDescription().getAuthors()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));

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

        fileManager = new FileManager(this);
        menusFileManager = new MenusFileManager(this);
        itemBuilder = new ItemBuilder(this);
        pluginUtils = new PluginUtils(this);
        mySQL = new MySQL();
        yaml = new Yaml(this);
        playerCache = new ArrayList<>();
        adminCommands = new ArrayList<>();
        manageCommands = new ArrayList<>();
        playerCommands = new ArrayList<>();

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m+------------------------------------------------+"));

    }

    @Override
    public void onDisable() {

        if (("MySQL".equalsIgnoreCase(getConfig().getString("Database-Type")))) {
            sqlManager.onDisable();
        }
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

    public void initDatabase() {

        if (this.getConfig().getString("Database-Type").equalsIgnoreCase("MySQL")) {
            sqlManager = new SQLManager(this);
        }
    }

    public void getDatabaseType() {

        if (("MySQL".equalsIgnoreCase(getConfig().getString("Database-Type")))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &bDatabase Type: &fMySQL"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &eTrying to connect to the database..."));
            initDatabase();
            databaseManager = new MySQL();

        } else if (("YAML".equalsIgnoreCase(getConfig().getString("Database-Type")))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &bDatabase Type: &fYAML"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &eRegistering events..."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&r"));
            Bukkit.getPluginManager().registerEvents(new com.reussy.databases.yaml.PlayerJoinListener(), this);
            databaseManager = new Yaml(this);

        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &bDatabase Type: &f" + getConfig().getString("Database-Type")));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &cThat kind of database doesn't exist. Try YAML or MySQL"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &cDisabling plugin for avoid issues!"));
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void registerHooks() {

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholdersBuilder(this).register();
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8[&a+&8] &fPlaceholderAPI"));
        }

        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8[&a+&8] &fEssentialsX"));
        }

        if (setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &8[&a+&8] &fVault"));
        }
    }

    public void Commands() {

        Objects.requireNonNull(this.getCommand("eh")).setExecutor(new MainCommand(this));
        Objects.requireNonNull(this.getCommand("ehm")).setExecutor(new ManageCommand(this));
        Objects.requireNonNull(this.getCommand("home")).setExecutor(new PlayerCommand(this));
        Objects.requireNonNull(this.getCommand("eh")).setTabCompleter(new MainCommand(this));
        Objects.requireNonNull(this.getCommand("ehm")).setTabCompleter(new ManageCommand(this));
        Objects.requireNonNull(this.getCommand("home")).setTabCompleter(new PlayerCommand(this));

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
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &cFailed to register Vault"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &cSome economy manager is needed, such as Essentials"));
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public SQLManager getSqlManager() {

        return sqlManager;
    }

    public DatabaseManager getDatabaseManager() {

        return databaseManager;
    }
}