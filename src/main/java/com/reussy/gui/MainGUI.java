package com.reussy.gui;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.utils.ItemBuilder;
import com.reussy.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class MainGUI {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
    FileManager fileManager = new FileManager();
    ItemBuilder itemBuilder = new ItemBuilder();

    public void GUI(Player player){

        int size = fileManager.getGui().getInt("MainGUI.Size");
        String title = plugin.setColor(fileManager.getGui().getString("MainGUI.Title"));

        Inventory gui = Bukkit.createInventory(null, size, title);

        ItemStack Homes = itemBuilder.createItem(player, XMaterial.valueOf(fileManager.getGui().getString("MainGUI.Items.Homes.Icon")), fileManager.getGui().getInt("MainGUI.Items.Homes.Amount")
                , plugin.setColor(fileManager.getGui().getString("MainGUI.Items.Homes.Name")), Collections.singletonList("test"));

        gui.setItem(23, Homes);

        player.openInventory(gui);

    }
}
