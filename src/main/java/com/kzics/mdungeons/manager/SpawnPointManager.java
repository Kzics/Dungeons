package com.kzics.mdungeons.manager;

import com.kzics.mdungeons.mobs.MobProperties;
import com.kzics.mdungeons.mobs.SpawnPoint;
import org.bukkit.entity.Player;

import java.util.List;

public interface SpawnPointManager {
    boolean setSpawnPoint(MobProperties mobProperties, int spawnInterval, int radius, Player player);
    void removeSpawnPoint(String name);
    List<SpawnPoint> listSpawnPoints();
}