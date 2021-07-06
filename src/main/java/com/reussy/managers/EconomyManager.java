package com.reussy.managers;

import com.reussy.ExodusHomes;
import com.reussy.utils.MessageUtils;
import com.reussy.utils.PlayerUtils;
import org.bukkit.entity.Player;

public class EconomyManager {

    private final ExodusHomes plugin;
    MessageUtils messageUtils = new MessageUtils();
    PlayerUtils playerUtils = new PlayerUtils();

    public EconomyManager(ExodusHomes plugin) {
        this.plugin = plugin;
    }

    /**
     * @param player sender
     * @param amount money
     * @return true
     */
    public boolean isEnoughMoney(Player player, int amount) {

        FileManager fileManager = new FileManager(plugin);
        double playerBalance = plugin.economy.getBalance(player.getName());

        if (amount <= 0) return false;

        if (playerBalance >= amount) {
            plugin.economy.withdrawPlayer(player.getName(), amount);
            playerUtils.sendMessageWithPrefix(player, fileManager.getMessage("Satisfactory-Payment")
                    .replace("%money_paid%", String.valueOf(amount)).replace("%player_balance%", String.valueOf(playerBalance - amount)));
            return false;
        } else {
            playerUtils.sendMessageWithPrefix(player, fileManager.getMessage("Not-Enough-Money"));
            playerUtils.sendSound(player, player.getLocation(), plugin.getConfig().getString("Sounds.Not-Enough-Money"), plugin.getConfig().getInt("Sounds.Volume"), plugin.getConfig().getInt("Sounds.Pitch"));
            return true;
        }
    }
}
