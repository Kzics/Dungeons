package com.kzics.mdungeons.mobs;

import org.bukkit.Location;

public class SpawnPoint {

    private final Location location;
    private final MobProperties mobProperties;
    private final int spawnInterval;
    private final int radius;
    private long lastSpawn;


    public SpawnPoint(Location location, MobProperties mobProperties, int spawnInterval, int radius) {
        this.location = location;
        this.mobProperties = mobProperties;
        this.spawnInterval = spawnInterval;
        this.radius = radius;
        this.lastSpawn = 0;
    }

    public Location location() {
        return location;
    }

    public void updateLastSpawn() {
        this.lastSpawn = System.currentTimeMillis() / 1000L;
    }

    public long lastSpawn() {
        return lastSpawn;
    }

    public MobProperties mobProperties() {
        return mobProperties;
    }

    public int spawnInterval() {
        return spawnInterval;
    }

    public int radius() {
        return radius;
    }
}