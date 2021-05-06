package com.reussy.menus;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.utils.ItemBuilder;
import com.reussy.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MainGUI {

    FileManager FManager = new FileManager();
    //Essentials EssX = new Essentials();
    ItemBuilder createItem = new ItemBuilder();
    private ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);

    public void openGUI(Player P) {

        Inventory newMenu = Bukkit.createInventory(null, 54, "Home Menu");

        ItemStack Homes = createItem.normalItem(XMaterial.BOOKSHELF, 1, "My Homes", null);

        newMenu.setItem(22, Homes);

        P.openInventory(newMenu);

    }
}
