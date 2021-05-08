package com.reussy.utils;

import com.reussy.ExodusHomes;
import com.reussy.filemanager.FileManager;
import org.bukkit.entity.Player;

import java.util.List;

public class YamlType implements DatabaseType {

	private final ExodusHomes plugin = ExodusHomes.getPlugin(ExodusHomes.class);
	FileManager fileManager = new FileManager();

	@Override
	public boolean hasHome(Player player) {
		return false;
	}

	@Override
	public void createHome(Player player, String home) {

	}

	@Override
	public void deleteHome(Player player, String home) {

	}

	@Override
	public void goHome(Player player, String home) {

	}

	@Override
	public void getHomes(Player player) {

	}

	@Override
	public String getWorld(Player player, String home) {
		return null;
	}

	@Override
	public double getX(Player player, String home) {
		return 0;
	}

	@Override
	public double getY(Player player, String home) {
		return 0;
	}

	@Override
	public double getZ(Player player, String home) {
		return 0;
	}

	@Override
	public float getPitch(Player player, String home) {
		return 0;
	}

	@Override
	public float getYaw(Player player, String home) {
		return 0;
	}
}
