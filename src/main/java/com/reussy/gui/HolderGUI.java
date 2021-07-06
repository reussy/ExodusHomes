package com.reussy.gui;

import com.reussy.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface HolderGUI extends InventoryHolder {
    ItemBuilder itemBuilder = new ItemBuilder();

    void onClick(InventoryClickEvent e);

    void setItems(Player player, Inventory inventory);
}
