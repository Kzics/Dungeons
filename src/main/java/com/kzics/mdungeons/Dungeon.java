package com.kzics.mdungeons;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public record Dungeon(String name, Location point1, Location point2) {

    public void onPlayerEnter(Player player) {

        player.sendMessage("Bienvenue dans le donjon " + name);
    }

    public void onPlayerExit(Player player) {
        player.sendMessage("Vous avez quitté le donjon " + name);
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

}
