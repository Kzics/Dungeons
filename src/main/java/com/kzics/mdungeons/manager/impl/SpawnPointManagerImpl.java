package com.kzics.mdungeons.manager.impl;

import com.kzics.mdungeons.manager.SpawnPointManager;
import com.kzics.mdungeons.mobs.MobProperties;
import com.kzics.mdungeons.mobs.SpawnPoint;
import com.kzics.mdungeons.utils.WorldEditUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnPointManagerImpl implements SpawnPointManager {
    private final Map<String, SpawnPoint> spawnPoints = new HashMap<>();
    private final FileConfiguration config;
    public SpawnPointManagerImpl(FileConfiguration config) {
        this.config = config;
    }
    @Override
    public void setSpawnPoint(MobProperties mobProperties, int spawnInterval, int radius, Player player) {
        Location selection = WorldEditUtil.getPlayerSelection(player);

        spawnPoints.put(mobProperties.name(), new SpawnPoint(mobProperties, spawnInterval, radius, selection));
    }

    @Override
    public void removeSpawnPoint(String name) {
        spawnPoints.remove(name);
    }

    @Override
    public List<SpawnPoint> listSpawnPoints() {
        return new ArrayList<>(spawnPoints.values());
    }


    private void loadSpawnPoints() {
        if (config.isConfigurationSection("spawnPoints")) {
            for (String key : config.getConfigurationSection("spawnPoints").getKeys(false)) {
                String path = "spawnPoints." + key;
                String name = config.getString(path + ".name");
                String mobType = config.getString(path + ".mobType");
                String world = config.getString(path + ".world");
                double x = config.getDouble(path + ".x");
                double y = config.getDouble(path + ".y");
                double z = config.getDouble(path + ".z");
                int spawnInterval = config.getInt(path + ".spawnInterval");
                int radius = config.getInt(path + ".radius");
                Location location = new Location(Bukkit.getWorld(world), x, y, z);
                MobProperties mobProperties = new MobProperties(EntityType.valueOf(mobType), name, 100, 10, new ArrayList<>());
                spawnPoints.put(name, new SpawnPoint(mobProperties, spawnInterval, radius, location));
            }
        }
    }

    private void saveSpawnPoints() {
        config.set("spawnPoints", null);
        int index = 0;
        for (SpawnPoint spawnPoint : spawnPoints.values()) {
            String path = "spawnPoints." + index++;
            config.set(path + ".name", spawnPoint.mobProperties().name());
            config.set(path + ".mobType", spawnPoint.mobProperties().type().name());
            config.set(path + ".world", spawnPoint.location().getWorld().getName());
            config.set(path + ".x", spawnPoint.location().getX());
            config.set(path + ".y", spawnPoint.location().getY());
            config.set(path + ".z", spawnPoint.location().getZ());
            config.set(path + ".spawnInterval", spawnPoint.spawnInterval());
            config.set(path + ".radius", spawnPoint.radius());
        }
    }

}