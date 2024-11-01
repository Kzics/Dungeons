package com.kzics.mdungeons;

import com.kzics.mdungeons.commands.DungeonCommand;
import com.kzics.mdungeons.commands.MobSpawnCommand;
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

        getCommand("mobspawn").setExecutor(new MobSpawnCommand(spawnPointManager));
        getCommand("dungeon").setExecutor(new DungeonCommand(dungeonManager));
    }

    @Override
    public void onDisable() {
        getLogger().info("MysticDungeons has been disabled!");
    }

    public DungeonManager dungeonManager() {
        return dungeonManager;
    }

    public SpawnPointManager spawnPointManager() {
        return spawnPointManager;
    }


}
