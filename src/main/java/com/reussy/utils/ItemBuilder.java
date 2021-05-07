package com.reussy.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemBuilder {

    public ItemStack createItem(Player player, XMaterial setItem, int setAmount, String setDisplayName, List<String> setLore) {

        ItemStack Item = setItem.parseItem();
        assert Item != null;
        Item.setAmount(setAmount);
        ItemMeta Meta = Item.getItemMeta();
        Meta.setDisplayName(setDisplayName);
        Meta.setLore(setLore);
        Item.setItemMeta(Meta);

        if (Item.getType() == XMaterial.PLAYER_HEAD.parseMaterial()){

            SkullMeta texture = (SkullMeta) Item.getItemMeta();
            texture.setOwningPlayer(player);
            Item.setItemMeta(texture);
        }

        return Item;
    }
}
