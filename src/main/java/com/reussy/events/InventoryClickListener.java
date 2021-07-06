package com.reussy.events;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.gui.HolderGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (e.getCurrentItem() == null) return;

        assert e.getCurrentItem() != null;
        if (e.getCurrentItem().getType() == XMaterial.AIR.parseMaterial()) return;

        if (e.getClickedInventory() != null && e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof HolderGUI) {

            e.setCancelled(true);
            ((HolderGUI) e.getInventory().getHolder()).onClick(e);
        }
    }
}
