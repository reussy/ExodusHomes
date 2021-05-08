package com.reussy.utils;

import com.reussy.ExodusHomes;
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

	public ItemStack setBackground(Inventory inventory, XMaterial material, int amount, String name, int size, int slot) {

		boolean setFill = plugin.getConfig().getBoolean("Background.Fill");
		ItemStack item = material.parseItem();
		item.setAmount(amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);

		if(setFill)
			while(size < inventory.getSize()) {

				inventory.setItem(slot, item);
				slot++;
			}

		return item;
	}
}
