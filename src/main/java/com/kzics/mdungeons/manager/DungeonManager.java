package com.kzics.mdungeons.manager;

import com.kzics.mdungeons.Dungeon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class DungeonManager {
    private final Map<String, Dungeon> dungeons = new HashMap<>();
    private final FileConfiguration config;

    public DungeonManager(FileConfiguration config) {
        this.config = config;
        loadDungeons();
    }

    public void setDungeon(String name, Location point1, Location point2) {
        dungeons.put(name, new Dungeon(name, point1, point2));
        saveDungeons();
    }

    public Dungeon getDungeon(String name) {
        return dungeons.get(name);
    }

    public void removeDungeon(String name) {
        dungeons.remove(name);
        saveDungeons();
    }

    public Map<String, Dungeon> listDungeons() {
        return new HashMap<>(dungeons);
    }

    private void loadDungeons() {
        if (config.isConfigurationSection("dungeons")) {
            for (String key : config.getConfigurationSection("dungeons").getKeys(false)) {
                String path = "dungeons." + key;
                String name = config.getString(path + ".name");
                String world = config.getString(path + ".world");
                double x1 = config.getDouble(path + ".x1");
                double y1 = config.getDouble(path + ".y1");
                double z1 = config.getDouble(path + ".z1");
                double x2 = config.getDouble(path + ".x2");
                double y2 = config.getDouble(path + ".y2");
                double z2 = config.getDouble(path + ".z2");
                Location point1 = new Location(Bukkit.getWorld(world), x1, y1, z1);
                Location point2 = new Location(Bukkit.getWorld(world), x2, y2, z2);
                dungeons.put(name, new Dungeon(name, point1, point2));
            }
        }
    }

    private void saveDungeons() {
        config.set("dungeons", null);
        int index = 0;
        for (Dungeon dungeon : dungeons.values()) {
            String path = "dungeons." + index++;
            config.set(path + ".name", dungeon.name());
            config.set(path + ".world", dungeon.point1().getWorld().getName());
            config.set(path + ".x1", dungeon.point1().getX());
            config.set(path + ".y1", dungeon.point1().getY());
            config.set(path + ".z1", dungeon.point2().getZ());
            config.set(path + ".x2", dungeon.point2().getX());
            config.set(path + ".y2", dungeon.point2().getY());
            config.set(path + ".z2", dungeon.point2().getZ());
        }
    }
}