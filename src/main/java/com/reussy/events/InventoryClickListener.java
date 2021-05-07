package com.reussy.events;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.gui.HomesGUI;
import com.reussy.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
    FileManager fileManager = new FileManager();
    HomesGUI homesGUI = new HomesGUI();

    @EventHandler
    public void onClick(InventoryClickEvent e){

        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equalsIgnoreCase(plugin.setColor(fileManager.getGui().getString("MainGUI.Title")))){

            if (e.getCurrentItem() == null) return;

            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == XMaterial.AIR.parseMaterial()) return;

            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.setColor(fileManager.getGui().getString("MainGUI.Items.Homes.Name")))){

                homesGUI.GUI(player);
            }
        }
    }
}
