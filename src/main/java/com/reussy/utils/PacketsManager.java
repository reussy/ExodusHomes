package com.reussy.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PacketsManager {

	public void sendPacket(Player player, Object packet){

		try{

			Object playerHandle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public Class<?> getNMSClass(String name){

		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch(ClassNotFoundException e){
			e.printStackTrace();
			return null;
		}
	}

	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut){

		try{
			Object getTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
			Object getSubTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
			Object getTitle_1 = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
			Object getSubtitle_2 = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
			Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
			Object packet_1 = titleConstructor.newInstance(getTitle, getTitle_1, fadeIn, stay, fadeOut);
			Object packet_2 = titleConstructor.newInstance(getSubTitle, getSubtitle_2, fadeIn, stay, fadeOut);
			sendPacket(player, packet_1);
			sendPacket(player, packet_2);

		}catch(InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException e){
			e.printStackTrace();
		}
	}
}
