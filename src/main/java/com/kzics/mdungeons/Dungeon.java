package com.kzics.mdungeons;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public record Dungeon(String name, Location location) {

    public void onPlayerEnter(Player player) {

        player.sendMessage("Bienvenue dans le donjon " + name);
    }

    public void onPlayerExit(Player player) {
        player.sendMessage("Vous avez quitté le donjon " + name);
    }
}
