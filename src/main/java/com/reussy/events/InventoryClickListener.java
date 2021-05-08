package com.reussy.events;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.gui.HomesGUI;
import com.reussy.sql.SQLData;
import com.reussy.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class InventoryClickListener implements Listener {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();
	SQLData sqlData = new SQLData();
	HomesGUI homesGUI = new HomesGUI();

	@EventHandler
	public void onMainGUI(InventoryClickEvent e) {

		Player player = (Player) e.getWhoClicked();

		if(e.getView().getTitle().equalsIgnoreCase(plugin.setColor(fileManager.getGui().getString("MainGUI.Title")))) {

			if(e.getCurrentItem() == null) return;

			if(e.getCurrentItem().getType() == XMaterial.AIR.parseMaterial()) return;

			e.setCancelled(true);

			if(e.getSlot() == fileManager.getGui().getInt("MainGUI.Items.Homes.Slot")) {
				homesGUI.GUI(player);
			}
		}
	}

	@EventHandler
	public void onHomesGUI(InventoryClickEvent e) {

		Player player = (Player) e.getWhoClicked();

		if(e.getView().getTitle().equalsIgnoreCase(plugin.setColor(fileManager.getGui().getString("HomesGUI.Title")))) {

			if(e.getClickedInventory() == player.getOpenInventory().getBottomInventory()) return;

			if(e.getCurrentItem() == null) return;

			if(e.getCurrentItem().getType() == XMaterial.AIR.parseMaterial()) return;

			e.setCancelled(true);

			int i = e.getSlot();
			List<String> getHome = sqlData.getHomes(plugin.getSQL(), player.getUniqueId());
			String getWorld = sqlData.getWorld(plugin.getSQL(), player.getUniqueId(), getHome.get(i));
			World World = Bukkit.getWorld(getWorld);
			double getX = sqlData.getX(plugin.getSQL(), player.getUniqueId(), getHome.get(i));
			double getY = sqlData.getY(plugin.getSQL(), player.getUniqueId(), getHome.get(i));
			double getZ = sqlData.getZ(plugin.getSQL(), player.getUniqueId(), getHome.get(i));
			float getPitch = sqlData.getPitch(plugin.getSQL(), player.getUniqueId(), getHome.get(i));
			float getYaw = sqlData.getYaw(plugin.getSQL(), player.getUniqueId(), getHome.get(i));
			Location Home = new Location(World, getX, getY, getZ, getYaw, getPitch);

			if(e.getCurrentItem().getType() == XMaterial.valueOf(fileManager.getGui().getString("HomesGUI.Items.Homes.Icon")).parseMaterial()) {

				player.teleport(Home);
			}
		}
	}
}
