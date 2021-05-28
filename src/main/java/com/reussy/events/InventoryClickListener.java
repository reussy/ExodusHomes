package com.reussy.events;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
import com.reussy.gui.HomesGUI;
import com.reussy.managers.FileManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class InventoryClickListener implements Listener {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();

	@EventHandler
	public void onMainGUI(InventoryClickEvent e) {

		Player player = (Player) e.getWhoClicked();
		HomesGUI homesGUI = new HomesGUI();

		if(fileManager.getText("MainGUI.Title").equalsIgnoreCase(e.getView().getTitle())) {

			if(e.getCurrentItem() == null) return;

			if(e.getCurrentItem().getType() == Material.AIR) return;

			e.setCancelled(true);

			if(e.getCurrentItem().getType() == XMaterial.valueOf(fileManager.getText("MainGUI.Items.Homes.Icon")).parseMaterial()) {
				homesGUI.GUI(player);
			}
		}
	}

	@EventHandler
	public void onHomesGUI(InventoryClickEvent e) {

		Player player = (Player) e.getWhoClicked();

		if(fileManager.getText("HomesGUI.Title").equalsIgnoreCase(plugin.setHexColor(e.getView().getTitle()))) {

			if(e.getClickedInventory() == player.getOpenInventory().getBottomInventory()) return;

			if(e.getCurrentItem() == null) return;

			if(e.getCurrentItem().getType() == Material.AIR) return;

			e.setCancelled(true);

			int i = e.getSlot();
			HomesGUI homesGUI = new HomesGUI();
			List<String> getHome = plugin.databaseType().getHomes(player);

			if(e.getCurrentItem().getType() == XMaterial.valueOf(fileManager.getText("HomesGUI.Items.Homes.Icon")).parseMaterial()) {

				ClickType clickType = e.getClick();

				if(clickType.isLeftClick()) {

					plugin.databaseType().goHome(player, getHome.get(i));
					player.closeInventory();
				}

				if(clickType.isRightClick()) {

					new BukkitRunnable() {
						@Override
						public void run() {
							player.getOpenInventory().close();
							plugin.databaseType().deleteHome(player, getHome.get(i));
							homesGUI.GUI(player);
						}
					}.runTaskLater(plugin, 20L);
				}
			}
		}
	}
}
