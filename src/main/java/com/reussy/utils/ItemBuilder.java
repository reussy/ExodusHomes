package com.reussy.utils;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import com.reussy.ExodusHomes;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final ExodusHomes plugin;

    /**
     * @param plugin main class
     */
    public ItemBuilder(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem(Player player, boolean setGlow, XMaterial material, int amount, String name, List<String> lore, String value) {

        ItemStack item = material.parseItem();
        assert item != null;
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            String namePlaceholders = null;
            List<String> lorePlaceholders = null;
            if (name != null)
                namePlaceholders = PlaceholderAPI.setPlaceholders(player, plugin.pluginUtils.setHexColor(name));
            meta.setDisplayName(namePlaceholders);
            if (lore != null) lorePlaceholders = PlaceholderAPI.setPlaceholders(player, lore);
            meta.setLore(lorePlaceholders);

        } else {
            meta.setDisplayName(plugin.pluginUtils.setHexColor(name));
            meta.setLore(lore);
        }
        if (setGlow) {
            meta.addEnchant(Enchantment.DURABILITY, 10, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);

        if (item.getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {

            SkullMeta texture = (SkullMeta) item.getItemMeta();
            SkullUtils.applySkin(texture, value.replace("%player%", player.getName()));
            item.setItemMeta(texture);
        }

        return item;
    }

    public void setContents(Inventory inventory, Player player, FileConfiguration fileConfiguration) {

        ConfigurationSection getContents = fileConfiguration.getConfigurationSection("Contents");
        assert getContents != null;
        try {
            for (String getItem : getContents.getKeys(false)) {

                XMaterial itemMaterial = XMaterial.valueOf(getContents.getString(getItem + ".Material"));
                int itemAmount = getContents.getInt(getItem + ".Amount");
                int itemSlot = getContents.getInt(getItem + ".Slot");
                String itemName = getContents.getString(getItem + ".Name");
                String itemValue = getContents.getString(getItem + ".Value");
                List<String> itemLore = new ArrayList<>();
                for (String lore : getContents.getStringList(getItem + ".Lore")) {
                    itemLore.add(plugin.pluginUtils.setHexColor(lore));
                }

                ItemStack newItem = plugin.itemBuilder.createItem(player, false, itemMaterial, itemAmount, itemName, itemLore, itemValue);

                if (XMaterial.valueOf(getContents.getString(getItem + ".Material")).isSupported()) {
                    inventory.setItem(itemSlot, newItem);
                } else {
                    inventory.setItem(itemSlot, XMaterial.STONE.parseItem());
                    Bukkit.getConsoleSender().sendMessage(plugin.pluginUtils.setHexColor("&c[ExodusMiner] &e" + itemMaterial + " is invalid material for your server version or is invalid!"));
                }
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(plugin.pluginUtils.setHexColor("&c[ExodusMiner] &eThere is a possible error in some menu. Check your files that they are correct!"));
            e.printStackTrace();
        }
    }

    public void setBackground(Player player, Inventory gui, int minSlot, int maxSlot) {

        if (plugin.getConfig().getBoolean("Background.Enable")) {

            ItemStack itemFill = this.createItem(player, false, XMaterial.valueOf(plugin.getConfig().getString("Background.Icon")), 1
                    , plugin.pluginUtils.setHexColor(plugin.getConfig().getString("Background.Name")), null, null);

            if (plugin.getConfig().getBoolean("Background.Enable")) while (minSlot < maxSlot) {

                gui.setItem(minSlot, itemFill);
                minSlot++;
            }
        }
    }
}
