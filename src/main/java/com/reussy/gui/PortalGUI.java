package com.reussy.gui;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
import com.reussy.managers.MenusFileManager;
import org.bukkit.Bukkit;
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

    /**
     * @param plugin main class
     * @param player player in gui
     */
    public PortalGUI(ExodusHomes plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void onClick(InventoryClickEvent e) {

        MenusFileManager menusFileManager = new MenusFileManager(plugin);
        Player player = (Player) e.getWhoClicked();
        List<String> getHome = plugin.getDatabaseManager().getHomes(player);

        assert e.getCurrentItem() != null;

        if (e.getCurrentItem().getType() ==
                XMaterial.valueOf(menusFileManager.getString("Static-Contents.Homes.Material", menusFileManager.getPortalYAML())).parseMaterial()) {

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

        List<String> getHomes = plugin.databaseManager.getHomes(player);
        boolean hasHome = plugin.databaseManager.hasHome(player);
        int slot = 0;

        if (!hasHome) {

            XMaterial material = XMaterial.valueOf(plugin.menusFileManager.getPortalYAML().getString("Static-Contents.Empty-Homes.Material"));
            String headValue = plugin.menusFileManager.getPortalYAML().getString("Static-Contents.Empty-Homes.Value");
            int amount = plugin.menusFileManager.getPortalYAML().getInt("Static-Contents.Empty-Homes.Amount");
            String displayName = plugin.menusFileManager.getPortalYAML().getString("Static-Contents.Empty-Homes.Name");
            List<String> emptyLore = new ArrayList<>(plugin.menusFileManager.getPortalYAML().getStringList("Static-Contents.Empty-Homes.Lore"));

            ItemStack emptyItem = plugin.itemBuilder.createItem(player, false, material, amount, displayName, emptyLore, headValue);
            inventory.setItem(22, emptyItem);

        }

        for (String getHome : getHomes) {

            String homeWorld = plugin.getDatabaseManager().getWorld(player, getHome);
            double homeX = plugin.getDatabaseManager().getX(player, getHome);
            double homeY = plugin.getDatabaseManager().getY(player, getHome);
            double homeZ = plugin.getDatabaseManager().getZ(player, getHome);
            XMaterial material = XMaterial.valueOf(plugin.menusFileManager.getPortalYAML().getString("Static-Contents.Homes.Material"));
            String headValue = plugin.menusFileManager.getPortalYAML().getString("Static-Contents.Homes.Value");
            String displayName = Objects.requireNonNull(plugin.menusFileManager.getPortalYAML().getString("Static-Contents.Homes.Name"))
                    .replace("%home_x%", String.valueOf(homeX))
                    .replace("%home_y%", String.valueOf(homeY))
                    .replace("%home_z%", String.valueOf(homeZ))
                    .replace("%home_world%", homeWorld)
                    .replace("%home_name%", getHome);
            List<String> homeLore = new ArrayList<>(plugin.menusFileManager.getPortalYAML().getStringList("Static-Contents.Homes.Lore"
                    .replace("%home_x%", String.valueOf(homeX))
                    .replace("%home_y%", String.valueOf(homeY))
                    .replace("%home_z%", String.valueOf(homeZ))
                    .replace("%home_world%", homeWorld)
                    .replace("%home_name%", getHome)));
            ItemStack home = plugin.itemBuilder.createItem(player, false, material, slot + 1,
                    displayName, homeLore, headValue);

            inventory.setItem(slot, home);
            slot++;

            if (slot > 45) break;
        }

        plugin.itemBuilder.setContents(inventory, player, plugin.menusFileManager.getPortalYAML());
    }

    @NotNull
    @Override
    public Inventory getInventory() {

        Inventory inventory = Bukkit.createInventory(this, 54, Objects.requireNonNull(plugin.menusFileManager.getPortalYAML().getString("Title")));
        setItems(player, inventory);

        return inventory;
    }
}
