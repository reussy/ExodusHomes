package com.reussy.managers;

import org.bukkit.entity.Player;

public class HomesManager {

	private final Player player;
	private final double x;
	private final double y;
	private final double z;
	private final float yaw;
	private final float pitch;
	private String world;

	public HomesManager(Player player, String world, double x, double y, double z, float yaw, float pitch) {
		this.player = player;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Player getPlayer() {
		return player;
	}

	public String getWorld() {
		return world;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}
}
