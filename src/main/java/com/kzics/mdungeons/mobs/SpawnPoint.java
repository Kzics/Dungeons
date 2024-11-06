package com.kzics.mdungeons.mobs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.simple.JSONObject;

public class SpawnPoint {

    private Location location;
    private final MobProperties mobProperties;
    private int spawnInterval;
    private int radius;
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

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setSpawnInterval(int spawnInterval) {
        this.spawnInterval = spawnInterval;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int radius() {
        return radius;
    }

    public void setLastSpawn(long lastSpawn) {
        this.lastSpawn = lastSpawn;
    }


    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("location", locationToJson(location));
        json.put("mobProperties", mobProperties.toJson());
        json.put("spawnInterval", spawnInterval);
        json.put("radius", radius);
        return json;
    }

    public static SpawnPoint fromJson(JSONObject json) {
        Location location = jsonToLocation((JSONObject) json.get("location"));
        MobProperties mobProperties = MobProperties.fromJson((JSONObject) json.get("mobProperties"));
        int spawnInterval = ((Long) json.get("spawnInterval")).intValue();
        int radius = ((Long) json.get("radius")).intValue();
        return new SpawnPoint(location, mobProperties, spawnInterval, radius);
    }

    private JSONObject locationToJson(Location location) {
        JSONObject json = new JSONObject();
        json.put("world", location.getWorld().getName());
        json.put("x", location.getX());
        json.put("y", location.getY());
        json.put("z", location.getZ());
        json.put("yaw", location.getYaw());
        json.put("pitch", location.getPitch());
        return json;
    }

    private static Location jsonToLocation(JSONObject json) {
        return new Location(
                Bukkit.getWorld((String) json.get("world")),
                ((Number) json.get("x")).doubleValue(),
                ((Number) json.get("y")).doubleValue(),
                ((Number) json.get("z")).doubleValue(),
                ((Number) json.get("yaw")).floatValue(),
                ((Number) json.get("pitch")).floatValue()
        );
    }
}