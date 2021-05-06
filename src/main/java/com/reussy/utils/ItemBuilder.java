package com.reussy.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemBuilder {

    public ItemStack normalItem(XMaterial setItem, int setAmount, String setDisplayName, List<String> setLore) {

        ItemStack Item = setItem.parseItem();
        assert Item != null;
        Item.setAmount(setAmount);
        ItemMeta Meta = Item.getItemMeta();
        Meta.setDisplayName(setDisplayName);
        Meta.setLore(setLore);
        Item.setItemMeta(Meta);

        return Item;
    }

    public ItemStack headItem(XMaterial setItem, int setAmount, String P, String setDisplayName, List<String> setLore) {

        ItemStack Head = setItem.parseItem();
        assert Head != null;
        Head.setAmount(setAmount);
        ItemMeta Meta = Head.getItemMeta();
        SkullMeta Skin = (SkullMeta) Head.getItemMeta();
        SkullUtils.applySkin(Skin, P);
        Skin.setDisplayName(setDisplayName);
        Skin.setLore(setLore);
        Head.setItemMeta(Skin);

        return Head;
    }
}
