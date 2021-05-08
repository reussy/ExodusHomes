package com.reussy.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemBuilder {

	public ItemStack createItem(Player player, XMaterial material, int amount, String name, List<String> lore) {

		ItemStack item = material.parseItem();
		assert item != null;
		item.setAmount(amount);
		ItemMeta meta = item.getItemMeta();
		assert meta != null;
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);

		if(item.getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {

			SkullMeta texture = (SkullMeta) item.getItemMeta();
			texture.setOwningPlayer(player);
			item.setItemMeta(texture);
		}

		return item;
	}
}
