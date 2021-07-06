package com.reussy.gui;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
import com.reussy.managers.InventoryFileManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PortalGUI implements HolderGUI {

    private final ExodusHomes plugin;
    private final Player player;
    private final List<String> itemLore = new ArrayList<>();

    public PortalGUI(ExodusHomes plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void onClick(InventoryClickEvent e) {

        InventoryFileManager inventoryFileManager = new InventoryFileManager(plugin);
        Player player = (Player) e.getWhoClicked();
        List<String> getHome = plugin.getDatabaseManager().getHomes(player);

        assert e.getCurrentItem() != null;

        if (e.getCurrentItem().getType() ==
                XMaterial.valueOf(inventoryFileManager.getString("Static-Contents.Homes.Material", inventoryFileManager.getPortalYAML())).parseMaterial()) {

            switch (e.getClick()) {

                case LEFT:
                    plugin.getDatabaseManager().goHome(player, getHome.get(e.getSlot()));
                    player.closeInventory();
                    break;

                case RIGHT:
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.getOpenInventory().close();
                            plugin.getDatabaseManager().deleteHome(player, getHome.get(e.getSlot()));
                            player.openInventory(new PortalGUI(plugin, player).getInventory());
                        }
                    }.runTaskLater(plugin, 20L);
                    break;
            }
        }
    }

    @Override
    public void setItems(Player player, Inventory inventory) {

        InventoryFileManager inventoryFileManager = new InventoryFileManager(plugin);
        List<String> getHomes = plugin.databaseManager.getHomes(player);
        boolean hasHome = plugin.databaseManager.hasHome(player);
        int slot = 0;

        if (!hasHome) {
            List<String> emptyLore = new ArrayList<>();
            for (String getLore : inventoryFileManager.getPortalYAML().getStringList("Static-Contents.Empty-Homes.Lore")) {
                emptyLore.add(plugin.setHexColor(getLore));
            }

            ItemStack emptyItem = itemBuilder.createItem(player, XMaterial.valueOf(inventoryFileManager.getString("Static-Contents.Empty-Homes.Material", inventoryFileManager.getPortalYAML())),
                    inventoryFileManager.getPortalYAML().getInt("Static-Contents.Empty-Homes.Amount"),
                    inventoryFileManager.getString("Static-Contents.Empty-Homes.Name", inventoryFileManager.getPortalYAML()), emptyLore);

            inventory.setItem(22, emptyItem);

        }

        for (String getHome : getHomes) {

            String homeWorld = plugin.getDatabaseManager().getWorld(player, getHome);
            double homeX = plugin.getDatabaseManager().getX(player, getHome);
            double homeY = plugin.getDatabaseManager().getY(player, getHome);
            double homeZ = plugin.getDatabaseManager().getZ(player, getHome);

            List<String> homeLore = new ArrayList<>();
            for (String getLore : inventoryFileManager.getPortalYAML().getStringList("Static-Contents.Homes.Lore")) {
                homeLore.add(plugin.setHexColor(getLore)
                        .replace("%home_x%", String.valueOf(homeX))
                        .replace("%home_y%", String.valueOf(homeY))
                        .replace("%home_z%", String.valueOf(homeZ))
                        .replace("%home_world%", homeWorld)
                        .replace("%home_name%", getHome));
            }

            ItemStack home = itemBuilder.createItem(player, XMaterial.valueOf(inventoryFileManager.getString("Static-Contents.Homes.Material", inventoryFileManager.getPortalYAML())), slot + 1,
                    inventoryFileManager.getString("Static-Contents.Homes.Name", inventoryFileManager.getPortalYAML()).replace("%home_x%", String.valueOf(homeX))
                            .replace("%home_y%", String.valueOf(homeY))
                            .replace("%home_z%", String.valueOf(homeZ))
                            .replace("%home_world%", homeWorld)
                            .replace("%home_name%", getHome), homeLore);

            inventory.setItem(slot, home);
            slot++;

            if (slot > 45) break;
        }

        ConfigurationSection getContents = inventoryFileManager.configurationSection("Contents", inventoryFileManager.getPortalYAML());
        for (String getItem : getContents.getKeys(false)) {

            XMaterial itemMaterial = XMaterial.valueOf(getContents.getString(getItem + ".Material"));
            int itemAmount = getContents.getInt(getItem + ".Amount");
            int itemSlot = getContents.getInt(getItem + ".Slot");
            String itemName = getContents.getString(getItem + ".Name");
            for (String lore : getContents.getStringList(getItem + ".Lore")) {
                itemLore.add(plugin.setHexColor(lore));
            }

            ItemStack newItem = itemBuilder.createItem(player, itemMaterial, itemAmount, itemName, itemLore);

            if (XMaterial.valueOf(getContents.getString(getItem + ".Material")).isSupported()
                    || XMaterial.valueOf(getContents.getString(getItem + ".Material")).parseMaterial().isItem()) {
                inventory.setItem(itemSlot, newItem);
            } else {
                inventory.setItem(itemSlot, XMaterial.STONE.parseItem());
                Bukkit.getConsoleSender().sendMessage(plugin.setHexColor("&4[ExodusHomesDEBUG] &e" + itemMaterial + " &cis invalid material for your server version or is invalid!"));
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {

        InventoryFileManager inventoryFileManager = new InventoryFileManager(plugin);
        Inventory inventory = Bukkit.createInventory(this, 54, inventoryFileManager.getString("Title", inventoryFileManager.getOverviewYAML()));
        setItems(player, inventory);

        return inventory;
    }
}
