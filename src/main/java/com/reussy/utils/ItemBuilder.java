package com.reussy.utils;

import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemBuilder {

	public ItemStack createItem(Player player, XMaterial material, int amount, String name, List<String> lore) {

		String namePlaceholders;
		List<String> lorePlaceholders;
		ItemStack item = material.parseItem();
		assert item != null;
		item.setAmount(amount);
		ItemMeta meta = item.getItemMeta();
		assert meta != null;
		namePlaceholders = PlaceholderAPI.setPlaceholders(player, name);
		meta.setDisplayName(namePlaceholders);
		lorePlaceholders = PlaceholderAPI.setPlaceholders(player, lore);
		meta.setLore(lorePlaceholders);
		item.setItemMeta(meta);

		if(item.getType() == Material.PLAYER_HEAD) {

			SkullMeta texture = (SkullMeta) item.getItemMeta();
			texture.setOwningPlayer(player);
			item.setItemMeta(texture);
		}

		return item;
	}
}
