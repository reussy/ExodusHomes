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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OverviewGUI implements HolderGUI {

    private final ExodusHomes plugin;
    private final Player player;
    private final List<String> itemLore = new ArrayList<>();

    public OverviewGUI(ExodusHomes plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void onClick(InventoryClickEvent e) {

        InventoryFileManager inventoryFileManager = new InventoryFileManager(plugin);
        Player player = (Player) e.getWhoClicked();

        if (e.getSlot() == inventoryFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Slot"))
            player.openInventory(new PortalGUI(plugin, player).getInventory());
    }

    @Override
    public void setItems(Player player, Inventory inventory) {

        InventoryFileManager inventoryFileManager = new InventoryFileManager(plugin);
        itemBuilder.setBackground(player, inventory, 0, inventoryFileManager.getOverviewYAML().getInt("Size"));

        List<String> portalLore = new ArrayList<>();
        for (String getLore : inventoryFileManager.getOverviewYAML().getStringList("Static-Contents.Portal-Homes.Lore")) {
            portalLore.add(plugin.setHexColor(getLore));
        }

        ItemStack portalItem = itemBuilder.createItem(player, XMaterial.valueOf(inventoryFileManager.getString("Static-Contents.Portal-Homes.Material", inventoryFileManager.getOverviewYAML())),
                inventoryFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Amount"),
                inventoryFileManager.getString("Static-Contents.Portal-Homes.Name", inventoryFileManager.getOverviewYAML()), portalLore);

        inventory.setItem(inventoryFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Slot"), portalItem);

        ConfigurationSection getContents = inventoryFileManager.configurationSection("Contents", inventoryFileManager.getOverviewYAML());
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
                    || Objects.requireNonNull(XMaterial.valueOf(getContents.getString(getItem + ".Material")).parseMaterial()).isItem()) {
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
        Inventory inventory = Bukkit.createInventory(this, inventoryFileManager.getOverviewYAML().getInt("Size"),
                inventoryFileManager.getString("Title", inventoryFileManager.getOverviewYAML()));
        setItems(player, inventory);

        return inventory;
    }
}
