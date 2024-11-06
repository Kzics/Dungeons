package com.kzics.mdungeons;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public record Dungeon(String name, Location point1, Location point2) {

    public void onPlayerEnter(Player player, String message) {

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        player.setAllowFlight(false);

    }

    public void onPlayerExit(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        player.setAllowFlight(true);
    }

    public boolean isPlayerInDungeon(Location player) {
        return player.getWorld().equals(point1.getWorld()) &&
                player.getX() >= Math.min(point1.getX(), point2.getX()) &&
                player.getX() <= Math.max(point1.getX(), point2.getX()) &&
                player.getY() >= Math.min(point1.getY(), point2.getY()) &&
                player.getY() <= Math.max(point1.getY(), point2.getY()) &&
                player.getZ() >= Math.min(point1.getZ(), point2.getZ()) &&
                player.getZ() <= Math.max(point1.getZ(), point2.getZ());
    }

    @Override
    public String toString() {
        return MysticDungeons.gson.toJson(this);
    }

    public static Dungeon fromString(String json) {
        return MysticDungeons.gson.fromJson(json, Dungeon.class);
    }
}
