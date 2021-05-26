package com.reussy.events;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
import com.reussy.gui.HomesGUI;
import com.reussy.managers.FileManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class InventoryClickListener implements Listener {

	private final ExodusHomes plugin;

	public InventoryClickListener(ExodusHomes plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onMainGUI(InventoryClickEvent e) {

		FileManager fileManager = new FileManager(plugin);
		Player player = (Player) e.getWhoClicked();
		HomesGUI homesGUI = new HomesGUI(plugin);

		if(e.getView().getTitle().equalsIgnoreCase(plugin.setHexColor(fileManager.getGui().getString("MainGUI.Title")))) {

			if(e.getCurrentItem() == null) return;

			if(e.getCurrentItem().getType() == Material.AIR) return;

			e.setCancelled(true);

			if(e.getSlot() == fileManager.getGui().getInt("MainGUI.Items.Homes.Slot")) {
				homesGUI.GUI(player);
			}
		}
	}

	@EventHandler
	public void onHomesGUI(InventoryClickEvent e) {

		FileManager fileManager = new FileManager(plugin);
		Player player = (Player) e.getWhoClicked();

		if(e.getView().getTitle().equalsIgnoreCase(plugin.setHexColor(fileManager.getGui().getString("HomesGUI.Title")))) {

			if(e.getClickedInventory() == player.getOpenInventory().getBottomInventory()) return;

			if(e.getCurrentItem() == null) return;

			if(e.getCurrentItem().getType() == Material.AIR) return;

			e.setCancelled(true);

			int i = e.getSlot();
			List<String> getHome = plugin.databaseType().getHomes(player);

			if(e.getCurrentItem().getType() == XMaterial.valueOf(fileManager.getGui().getString("HomesGUI.Items.Homes.Icon")).parseMaterial()) {

				plugin.databaseType().goHome(player, getHome.get(i));
				player.closeInventory();
			}
		}
	}
}
