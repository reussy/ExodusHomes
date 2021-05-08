package com.reussy.gui;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import com.reussy.sql.SQLData;
import com.reussy.utils.ItemBuilder;
import com.reussy.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomesGUI {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();
	SQLData sqlData = new SQLData();
	ItemBuilder itemBuilder = new ItemBuilder();

	public void GUI(Player player) {

		int slot = 0;
		int size = fileManager.getGui().getInt("HomesGUI.Size");
		String title = plugin.setColor(fileManager.getGui().getString("HomesGUI.Title"));
		List<String> getHomes = sqlData.getHomes(plugin.getSQL(), player.getUniqueId());

		Inventory gui = Bukkit.createInventory(null, size, title);

		for(String getHome : getHomes) {

			String getWorld = sqlData.getWorld(plugin.getSQL(), player.getUniqueId(), getHome);
			World homeWorld = Bukkit.getWorld(getWorld);
			double homeX = sqlData.getX(plugin.getSQL(), player.getUniqueId(), getHome);
			double homeY = sqlData.getY(plugin.getSQL(), player.getUniqueId(), getHome);
			double homeZ = sqlData.getZ(plugin.getSQL(), player.getUniqueId(), getHome);

			List<String> homeLore = new ArrayList<>();
			for(String getLore : fileManager.getGui().getStringList("HomeGUI.Items.Homes.Lore")) {
				homeLore.add(plugin.setColor(getLore));

				ItemStack home = itemBuilder.createItem(player, XMaterial.valueOf(fileManager.getGui().getString("HomesGUI.Items.Homes.Icon")), fileManager.getGui().getInt("HomesGUI.Items.Homes.Amount"),
						plugin.setColor(Objects.requireNonNull(fileManager.getGui().getString("HomesGUI.Items.Homes.Name")).replace("%player_home%", getHome)), homeLore);

				gui.setItem(slot, home);
				slot++;
			}
		}
        player.openInventory(gui);
	}
}
