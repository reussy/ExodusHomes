package com.reussy.gui;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
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

        XMaterial material = XMaterial.valueOf(plugin.menusFileManager.getOverviewYAML().getString("Static-Contents.Portal-Homes.Material"));
        String headValue = plugin.menusFileManager.getOverviewYAML().getString("Static-Contents.Portal-Homes.Value");
        String displayName = plugin.menusFileManager.getOverviewYAML().getString("Static-Contents.Portal-Homes.Name");
        int amount = plugin.menusFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Amount");
        List<String> portalLore = new ArrayList<>(plugin.menusFileManager.getOverviewYAML().getStringList("Static-Contents.Portal-Homes.Lore"));

        ItemStack portalItem = plugin.itemBuilder.createItem(player, false, material, amount, displayName, portalLore, headValue);

        inventory.setItem(plugin.menusFileManager.getOverviewYAML().getInt("Static-Contents.Portal-Homes.Slot"), portalItem);

        plugin.itemBuilder.setContents(inventory, player, plugin.menusFileManager.getOverviewYAML());
    }

    @NotNull
    @Override
    public Inventory getInventory() {

        Inventory overview = Bukkit.createInventory(this, plugin.menusFileManager.getOverviewYAML().getInt("Size"), plugin.pluginUtils.setHexColor(plugin.menusFileManager.getOverviewYAML().getString("Title")));
        setItems(player, overview);

        return overview;
    }
}
