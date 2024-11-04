package com.kzics.mdungeons;

import com.kzics.mdungeons.commands.DungeonCommand;
import com.kzics.mdungeons.commands.MobSpawnCommand;
import com.kzics.mdungeons.listeners.DungeonsListener;
import com.kzics.mdungeons.manager.DungeonManager;
import com.kzics.mdungeons.manager.SpawnPointManager;
import com.kzics.mdungeons.manager.impl.SpawnPointManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class MysticDungeons extends JavaPlugin {
    private DungeonManager dungeonManager;
    private SpawnPointManager spawnPointManager;
    private static MysticDungeons instance;
    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("MysticDungeons has been enabled!");

        dungeonManager = new DungeonManager(getConfig());
        spawnPointManager = new SpawnPointManagerImpl(getConfig());

        getCommand("mobspawn").setExecutor(new MobSpawnCommand(spawnPointManager));
        getCommand("dungeon").setExecutor(new DungeonCommand(dungeonManager));
        getServer().getPluginManager().registerEvents(new DungeonsListener(this), this);
        new MobSpawnTask(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MysticDungeons has been disabled!");
    }

    public static MysticDungeons getInstance() {
        return instance;
    }

    public DungeonManager dungeonManager() {
        return dungeonManager;
    }

    public SpawnPointManager spawnPointManager() {
        return spawnPointManager;
    }


}
