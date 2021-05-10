package com.reussy.utils;

import org.bukkit.entity.Player;

import java.util.List;

public interface DatabaseType {

	boolean hasHome(Player player);

	void createHome(Player player, String home);

	void deleteHome(Player player, String home);

	void goHome(Player player, String home);

	void listHomes(Player player);

	String getWorld(Player player, String home);

	double getX(Player player, String home);

	double getY(Player player, String home);

	double getZ(Player player, String home);

	float getPitch(Player player, String home);

	float getYaw(Player player, String home);

	List<String> getHomes(Player player);

}
