package com.reussy.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface HolderGUI extends InventoryHolder {

    void onClick(InventoryClickEvent e);

    void setItems(Player player, Inventory inventory);
}
