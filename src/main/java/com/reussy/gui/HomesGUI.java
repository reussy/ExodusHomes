package com.reussy.gui;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
import com.reussy.managers.FileManager;
import com.reussy.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HomesGUI {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();
	ItemBuilder itemBuilder = new ItemBuilder();

	public void GUI(Player player) {

		int slot = 0;
		int slot_1 = 45;
		int size = fileManager.getGui().getInt("HomesGUI.Size");
		String title = plugin.setHexColor(fileManager.getText("HomesGUI.Title"));
		Inventory gui = Bukkit.createInventory(null, size, title);
		List<String> getHomes = plugin.databaseType().getHomes(player);
		boolean hasHome = plugin.databaseType().hasHome(player);

		if(!hasHome) {
			List<String> emptyLore = new ArrayList<>();
			for(String getLore : fileManager.getGui().getStringList("HomesGUI.Items.Empty.Lore")) {
				emptyLore.add(plugin.setHexColor(getLore));
			}

			ItemStack emptyItem = itemBuilder.createItem(player, XMaterial.valueOf(fileManager.getText("HomesGUI.Items.Empty.Icon")),
					fileManager.getGui().getInt("HomesGUI.Items.Empty.Amount"), fileManager.getText("HomesGUI.Items.Empty.Name"), emptyLore);

			gui.setItem(22, emptyItem);

		}

		for(String getHome : getHomes) {

			String homeWorld = plugin.databaseType().getWorld(player, getHome);
			double homeX = plugin.databaseType().getX(player, getHome);
			double homeY = plugin.databaseType().getY(player, getHome);
			double homeZ = plugin.databaseType().getZ(player, getHome);

			List<String> homeLore = new ArrayList<>();
			for(String getLore : fileManager.getGui().getStringList("HomesGUI.Items.Homes.Lore")) {
				homeLore.add(plugin.setHexColor(getLore)
						.replace("%home_x%", String.valueOf(homeX))
						.replace("%home_y%", String.valueOf(homeY))
						.replace("%home_z%", String.valueOf(homeZ))
						.replace("%home_world%", homeWorld)
						.replace("%home_name%", getHome));
			}

			ItemStack home = itemBuilder.createItem(player, XMaterial.valueOf(fileManager.getText("HomesGUI.Items.Homes.Icon")), slot + 1,
					fileManager.getText("HomesGUI.Items.Homes.Name").replace("%home_x%", String.valueOf(homeX))
							.replace("%home_y%", String.valueOf(homeY))
							.replace("%home_z%", String.valueOf(homeZ))
							.replace("%home_world%", homeWorld)
							.replace("%home_name%", getHome), homeLore);

			gui.setItem(slot, home);

			slot++;

			if(slot > 45) break;
		}

		ItemStack itemFill = itemBuilder.createItem(player, XMaterial.valueOf(plugin.getConfig().getString("Background.Icon")), 1
				, plugin.setHexColor(plugin.getConfig().getString("Background.Name")), null);

		while(slot_1 < 54) {

			gui.setItem(slot_1, itemFill);

			slot_1++;
		}

		List<String> playerLore = new ArrayList<>();
		for(String getLore : fileManager.getGui().getStringList("HomesGUI.Items.Player-Info.Lore"))
			playerLore.add(plugin.setHexColor(getLore).replace("%homes_count%", Integer.toString(getHomes.size())));

		ItemStack head = itemBuilder.createItem(player, XMaterial.valueOf(fileManager.getGui().getString("HomesGUI.Items.Player-Info.Icon")), fileManager.getGui().getInt("HomesGUI.Items.Player-Info.Amount"),
				plugin.setHexColor(fileManager.getGui().getString("HomesGUI.Items.Player-Info.Name")), playerLore);

		gui.setItem(fileManager.getGui().getInt("HomesGUI.Items.Player-Info.Slot"), head);

		player.openInventory(gui);
	}
}
