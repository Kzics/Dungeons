package com.kzics.mdungeons;

import com.kzics.mdungeons.manager.DungeonManager;
import com.kzics.mdungeons.manager.SpawnPointManager;
import com.kzics.mdungeons.manager.impl.SpawnPointManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class MysticDungeons extends JavaPlugin {

    private DungeonManager dungeonManager;
    private SpawnPointManager spawnPointManager;
    @Override
    public void onEnable() {
        getLogger().info("MysticDungeons has been enabled!");
        dungeonManager = new DungeonManager(getConfig());
        spawnPointManager = new SpawnPointManagerImpl(getConfig());
    }

    @Override
    public void onDisable() {
        getLogger().info("MysticDungeons has been disabled!");
    }


}
