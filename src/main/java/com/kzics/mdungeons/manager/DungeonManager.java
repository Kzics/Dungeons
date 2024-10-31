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
    }

    public void setDungeon(String name, Location location) {
        dungeons.put(name, new Dungeon(name, location));
    }

    public Dungeon getDungeon(String name) {
        return dungeons.get(name);
    }

    public void removeDungeon(String name) {
        dungeons.remove(name);
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
                double x = config.getDouble(path + ".x");
                double y = config.getDouble(path + ".y");
                double z = config.getDouble(path + ".z");
                Location location = new Location(Bukkit.getWorld(world), x, y, z);
                dungeons.put(name, new Dungeon(name, location));
            }
        }
    }

    private void saveDungeons() {
        config.set("dungeons", null);
        int index = 0;
        for (Dungeon dungeon : dungeons.values()) {
            String path = "dungeons." + index++;
            config.set(path + ".name", dungeon.name());
            config.set(path + ".world", dungeon.location().getWorld().getName());
            config.set(path + ".x", dungeon.location().getX());
            config.set(path + ".y", dungeon.location().getY());
            config.set(path + ".z", dungeon.location().getZ());
        }
    }

}