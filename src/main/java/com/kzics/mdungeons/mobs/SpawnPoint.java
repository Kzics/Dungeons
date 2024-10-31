package com.kzics.mdungeons.mobs;

import org.bukkit.Location;

public record SpawnPoint(MobProperties mobProperties, int spawnInterval, int radius, Location location) {
}