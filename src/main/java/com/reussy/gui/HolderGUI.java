package com.reussy.gui;

import com.reussy.managers.FileManager;
import com.reussy.managers.InventoryFileManager;
import com.reussy.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface HolderGUI extends InventoryHolder {
    FileManager fileManager = new FileManager();
    InventoryFileManager inventoryFileManager = new InventoryFileManager();
    ItemBuilder itemBuilder = new ItemBuilder();

    void onClick(InventoryClickEvent e);

    void setItems(Player player, Inventory inventory);
}
