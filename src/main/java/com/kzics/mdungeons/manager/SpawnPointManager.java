package com.kzics.mdungeons.manager;

import com.kzics.mdungeons.mobs.MobProperties;
import com.kzics.mdungeons.mobs.SpawnPoint;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface SpawnPointManager {
    boolean setSpawnPoint(MobProperties mobProperties, int spawnInterval, int radius, Player player);
    void removeSpawnPoint(String name);
    List<SpawnPoint> listSpawnPoints();
    SpawnPoint getSpawnPoint(String name);
    Map<String, SpawnPoint> getSpawnPoints();
    void updateSpawnPointKey(String oldName, String newName);
}