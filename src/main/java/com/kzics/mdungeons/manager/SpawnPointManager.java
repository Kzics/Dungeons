package com.kzics.mdungeons.manager;

import com.kzics.mdungeons.mobs.MobProperties;
import com.kzics.mdungeons.mobs.SpawnPoint;

import java.util.List;

public interface SpawnPointManager {
    void setSpawnPoint(MobProperties mobProperties, int spawnInterval, int radius);
    void removeSpawnPoint(String name);
    List<SpawnPoint> listSpawnPoints();
}