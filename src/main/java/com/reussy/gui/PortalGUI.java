package com.reussy.gui;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
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
import java.util.Objects;

public class PortalGUI implements HolderGUI {

    private final ExodusHomes plugin;
    private final Player player;

    public PortalGUI(ExodusHomes plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void onClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        List<String> getHome = plugin.databaseManager.getHomes(player);

        if (Objects.requireNonNull(e.getCurrentItem()).getType() ==
                XMaterial.valueOf(inventoryFileManager.getString("Static-Contents.Homes.Material", inventoryFileManager.portalYAML)).parseMaterial()) {

            switch (e.getClick()) {

                case LEFT:
                    plugin.databaseManager.goHome(player, getHome.get(e.getSlot()));
                    player.closeInventory();
                    break;

                case RIGHT:
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.getOpenInventory().close();
                            plugin.databaseManager.deleteHome(player, getHome.get(e.getSlot()));
                            player.openInventory(new PortalGUI(plugin, player).getInventory());
                        }
                    }.runTaskLater(plugin, 20L);
                    break;
            }
        }
    }

    @Override
    public void setItems(Player player, Inventory inventory) {

        List<String> getHomes = plugin.databaseManager.getHomes(player);
        boolean hasHome = plugin.databaseManager.hasHome(player);
        int slot = 0;

        if (!hasHome) {
            List<String> emptyLore = new ArrayList<>();
            for (String getLore : inventoryFileManager.getPortalYAML().getStringList("Static-Contents.Empty-Homes.Lore")) {
                emptyLore.add(plugin.setHexColor(getLore));
            }

            ItemStack emptyItem = itemBuilder.createItem(player, XMaterial.valueOf(inventoryFileManager.getString("Static-Contents.Empty-Homes.Material", inventoryFileManager.portalYAML)),
                    inventoryFileManager.getPortalYAML().getInt("Static-Contents.Empty-Homes.Amount"),
                    inventoryFileManager.getString("Static-Contents.Empty-Homes.Name", inventoryFileManager.portalYAML), emptyLore);

            inventory.setItem(22, emptyItem);

        }

        for (String getHome : getHomes) {

            String homeWorld = plugin.databaseManager.getWorld(player, getHome);
            double homeX = plugin.databaseManager.getX(player, getHome);
            double homeY = plugin.databaseManager.getY(player, getHome);
            double homeZ = plugin.databaseManager.getZ(player, getHome);

            List<String> homeLore = new ArrayList<>();
            for (String getLore : inventoryFileManager.portalYAML.getStringList("Static-Contents.Homes.Lore")) {
                homeLore.add(plugin.setHexColor(getLore)
                        .replace("%home_x%", String.valueOf(homeX))
                        .replace("%home_y%", String.valueOf(homeY))
                        .replace("%home_z%", String.valueOf(homeZ))
                        .replace("%home_world%", homeWorld)
                        .replace("%home_name%", getHome));
            }

            ItemStack home = itemBuilder.createItem(player, XMaterial.valueOf(inventoryFileManager.getString("Static-Contents.Homes.Material", inventoryFileManager.getPortalYAML())), slot + 1,
                    inventoryFileManager.getString("Static-Contents.Homes.Name", inventoryFileManager.portalYAML).replace("%home_x%", String.valueOf(homeX))
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
            List<String> itemLore = getContents.getStringList(getItem + ".Lore");

            ItemStack newItem = itemBuilder.createItem(player, itemMaterial, itemAmount, itemName, itemLore);

            inventory.setItem(itemSlot, newItem);
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {

        Inventory inventory = Bukkit.createInventory(this, 54, inventoryFileManager.getString("Title", inventoryFileManager.portalYAML));
        setItems(player, inventory);

        return inventory;
    }
}
