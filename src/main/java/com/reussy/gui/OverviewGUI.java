package com.reussy.gui;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
import com.reussy.managers.MenusFileManager;
import org.bukkit.Bukkit;
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

    /**
     * @param plugin main class
     * @param player player in gui
     */
    public OverviewGUI(ExodusHomes plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void onClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (e.getSlot() == plugin.menusFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Slot"))
            player.openInventory(new PortalGUI(plugin, player).getInventory());
    }

    @Override
    public void setItems(Player player, Inventory inventory) {

        MenusFileManager menusFileManager = new MenusFileManager(plugin);
        plugin.itemBuilder.setBackground(player, inventory, 0, menusFileManager.getOverviewYAML().getInt("Size"));

        XMaterial material = XMaterial.valueOf(plugin.menusFileManager.getOverviewYAML().getString("Static-Contents.Portal-Homes.Material"));
        String headValue = plugin.menusFileManager.getOverviewYAML().getString("Static-Contents.Portal-Homes.Value");
        String displayName = plugin.menusFileManager.getOverviewYAML().getString("Static-Contents.Portal-Homes.Name");
        int amount = plugin.menusFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Amount");
        List<String> portalLore = new ArrayList<>(plugin.menusFileManager.getOverviewYAML().getStringList("Static-Contents.Portal-Homes.Lore"));

        ItemStack portalItem = plugin.itemBuilder.createItem(player, false, material, amount, displayName, portalLore, headValue);

        inventory.setItem(menusFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Slot"), portalItem);

        plugin.itemBuilder.setContents(inventory, player, plugin.menusFileManager.getOverviewYAML());
    }

    @NotNull
    @Override
    public Inventory getInventory() {

        MenusFileManager menusFileManager = new MenusFileManager(plugin);
        Inventory inventory = Bukkit.createInventory(this, menusFileManager.getOverviewYAML().getInt("Size"),
                menusFileManager.getString("Title", menusFileManager.getOverviewYAML()));
        setItems(player, inventory);

        return inventory;
    }
}
