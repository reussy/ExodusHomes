package com.reussy.utils;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemBuilder {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);

	public ItemStack createItem(Player player, XMaterial material, int amount, String name, List<String> lore) {

		ItemStack item = material.parseItem();
		assert item != null;
		item.setAmount(amount);
		ItemMeta meta = item.getItemMeta();
		assert meta != null;

		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			String namePlaceholders = null;
			List<String> lorePlaceholders = null;
			if(name != null) namePlaceholders = PlaceholderAPI.setPlaceholders(player, name);
			meta.setDisplayName(namePlaceholders);
			if(lore != null) lorePlaceholders = PlaceholderAPI.setPlaceholders(player, lore);
			meta.setLore(lorePlaceholders);
			item.setItemMeta(meta);

		} else {
			meta.setDisplayName(name);
			meta.setLore(lore);
			item.setItemMeta(meta);
		}

		if(item.getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {

			SkullMeta texture = (SkullMeta) item.getItemMeta();
			SkullUtils.applySkin(texture, player);
			item.setItemMeta(texture);
		}

		return item;
	}

	public void setBackground(Player player, Inventory gui, int minSlot, int maxSlot) {

		int test = 0, test2 = 9;

		if(plugin.setFill) {

			ItemStack itemFill = this.createItem(player, XMaterial.valueOf(plugin.getConfig().getString("Background.Icon")), 1
					, plugin.setHexColor(plugin.getConfig().getString("Background.Name")), null);

			if(plugin.setFill) while(minSlot < maxSlot) {

				gui.setItem(minSlot, itemFill);
				minSlot++;
			}
		}
	}
}
