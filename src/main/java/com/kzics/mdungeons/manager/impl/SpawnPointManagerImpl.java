package com.kzics.mdungeons.manager.impl;

import com.kzics.mdungeons.manager.SpawnPointManager;
import com.kzics.mdungeons.mobs.MobProperties;
import com.kzics.mdungeons.mobs.SpawnPoint;
import com.kzics.mdungeons.utils.WorldEditUtil;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
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
        loadSpawnPoints();
    }

    @Override
    public boolean setSpawnPoint(MobProperties mobProperties, int spawnInterval, int radius, Player player) {
        Location[] selection = WorldEditUtil.getPlayerSelection(player);

        if (selection == null) return false;

        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        spawnPoints.put(serializer.serialize(mobProperties.name()), new SpawnPoint(selection[0], mobProperties, spawnInterval, radius));
        return true;
    }

    @Override
    public void removeSpawnPoint(String name) {
        spawnPoints.remove(name);
    }

    @Override
    public List<SpawnPoint> listSpawnPoints() {
        return new ArrayList<>(spawnPoints.values());
    }

    @Override
    public Map<String, SpawnPoint> getSpawnPoints() { // Implement this method
        return spawnPoints;
    }

    @Override
    public void updateSpawnPointKey(String oldName, String newName) {
        SpawnPoint spawnPoint = spawnPoints.remove(oldName);
        spawnPoints.put(newName, spawnPoint);
    }

    @Override
    public SpawnPoint getSpawnPoint(String name) {
        return spawnPoints.get(name);
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
               // MobProperties mobProperties = new MobProperties(EntityType.valueOf(mobType), name, 100, 10, new ArrayList<>());
                //spawnPoints.put(name, new SpawnPoint(location, mobProperties, spawnInterval, radius));
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