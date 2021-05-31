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

public class MainGUI {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();
	ItemBuilder itemBuilder = new ItemBuilder();

	public void GUI(Player player) {

		int size = fileManager.getGui().getInt("MainGUI.Size");
		String title = plugin.setHexColor(fileManager.getGui().getString("MainGUI.Title"));
		Inventory gui = Bukkit.createInventory(null, size, title);

		itemBuilder.setBackground(player, gui, 0, fileManager.getGui().getInt("MainGUI.Size"));

		List<String> homesLore = new ArrayList<>();
		for(String getLore : fileManager.getGui().getStringList("MainGUI.Items.Homes.Lore"))
			homesLore.add(plugin.setHexColor(getLore));

		ItemStack itemHomes = itemBuilder.createItem(player, XMaterial.valueOf(fileManager.getGui().getString("MainGUI.Items.Homes.Icon")), fileManager.getGui().getInt("MainGUI.Items.Homes.Amount")
				, plugin.setHexColor(fileManager.getGui().getString("MainGUI.Items.Homes.Name")), homesLore);

		gui.setItem(fileManager.getGui().getInt("MainGUI.Items.Homes.Slot"), itemHomes);

		List<String> importLore = new ArrayList<>();
		for(String getLore : fileManager.getGui().getStringList("MainGUI.Items.Import.Lore"))
			importLore.add(plugin.setHexColor(getLore));

		ItemStack importItem = itemBuilder.createItem(player, XMaterial.valueOf(fileManager.getGui().getString("MainGUI.Items.Import.Icon")), fileManager.getGui().getInt("MainGUI.Items.Import.Amount"),
				plugin.setHexColor(fileManager.getGui().getString("MainGUI.Items.Import.Name")), importLore);

		if(player.hasPermission(fileManager.getGui().getString("MainGUI.Items.Import.Permission")))
			gui.setItem(fileManager.getGui().getInt("MainGUI.Items.Import.Slot"), importItem);

		player.openInventory(gui);

	}
}
