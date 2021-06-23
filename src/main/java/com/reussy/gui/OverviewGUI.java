package com.reussy.gui;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OverviewGUI implements HolderGUI {

    private final ExodusHomes plugin;
    private final Player player;

    public OverviewGUI(ExodusHomes plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void onClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (e.getSlot() == inventoryFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Slot"))
            player.openInventory(new PortalGUI(plugin, player).getInventory());
    }

    @Override
    public void setItems(Player player, Inventory inventory) {

        itemBuilder.setBackground(player, inventory, 0, inventoryFileManager.getOverviewYAML().getInt("Size"));

        List<String> portalLore = new ArrayList<>();
        for (String getLore : inventoryFileManager.getOverviewYAML().getStringList("Static-Contents.Portal-Homes.Lore")) {
            portalLore.add(plugin.setHexColor(getLore));
        }

        ItemStack portalItem = itemBuilder.createItem(player, XMaterial.valueOf(inventoryFileManager.getString("Static-Contents.Portal-Homes.Material", inventoryFileManager.overviewYAML)),
                inventoryFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Amount"),
                inventoryFileManager.getString("Static-Contents.Portal-Homes.Name", inventoryFileManager.overviewYAML), portalLore);

        inventory.setItem(inventoryFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Slot"), portalItem);

        ConfigurationSection getContents = inventoryFileManager.configurationSection("Contents", inventoryFileManager.overviewYAML);
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

        Inventory inventory = Bukkit.createInventory(this, inventoryFileManager.getOverviewYAML().getInt("Size"),
                inventoryFileManager.getString("Title", inventoryFileManager.getOverviewYAML()));
        setItems(player, inventory);

        return inventory;
    }
}