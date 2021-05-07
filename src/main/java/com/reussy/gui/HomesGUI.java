package com.reussy.gui;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.sql.SQLData;
import com.reussy.utils.ItemBuilder;
import com.reussy.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HomesGUI {

    private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
    FileManager fileManager = new FileManager();
    SQLData sqlData = new SQLData();
    ItemBuilder itemBuilder = new ItemBuilder();

    public void GUI(Player player){

        int slot = 0;
        int size = fileManager.getGui().getInt("HomeGUI.Size");
        String title = ChatColor.translateAlternateColorCodes('&', fileManager.getGui().getString("HomeGUI..Title"));
        List<String> getHomes = sqlData.getHomes(plugin.getSQL(), player.getUniqueId());

        Inventory gui = Bukkit.createInventory(null, size, title);

        for (String homes : getHomes){

            ItemStack home = itemBuilder.createItem(player, XMaterial.valueOf(fileManager.getGui().getString("HomeGUI.Items.Homes.Icon")), fileManager.getGui().getInt("HomeGUI.Items.Homes.Amount"),
                    ChatColor.translateAlternateColorCodes('&', fileManager.getGui().getString("HomeGUI.Items.Homes.Name")), null);
            gui.setItem(slot, home);

            slot++;

            player.openInventory(gui);
        }
    }
}
