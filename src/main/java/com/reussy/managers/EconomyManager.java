package com.reussy.managers;

import com.reussy.ExodusHomes;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class EconomyManager {

    private final ExodusHomes plugin;

    /**
     * @param plugin main class
     */
    public EconomyManager(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    /**
     * @param player sender
     * @param amount money
     * @return true
     */
    public boolean isEnoughMoney(OfflinePlayer player, int amount) {

        FileManager fileManager = new FileManager(plugin);
        double playerBalance = plugin.economy.getBalance(player);

        if (amount <= 0) return false;

        if (playerBalance >= amount) {
            plugin.economy.withdrawPlayer(player, amount);
            plugin.pluginUtils.sendMessageWithPrefix(player.getPlayer(), fileManager.getMessage("Satisfactory-Payment")
                    .replace("%money_paid%", String.valueOf(amount)).replace("%player_balance%", String.valueOf(playerBalance - amount)));
            return false;
        } else {
            plugin.pluginUtils.sendMessageWithPrefix(player.getPlayer(), fileManager.getMessage("Not-Enough-Money"));
            plugin.pluginUtils.sendSound((Player) player, Objects.requireNonNull(player.getPlayer()).getLocation(), plugin.getConfig().getString("Sounds.Not-Enough-Money"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
            return true;
        }
    }
}
