package com.kzics.mdungeons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kzics.mdungeons.commands.DungeonCommand;
import com.kzics.mdungeons.commands.MobSpawnCommand;
import com.kzics.mdungeons.listeners.DungeonsListener;
import com.kzics.mdungeons.manager.DungeonManager;
import com.kzics.mdungeons.manager.SpawnPointManager;
import com.kzics.mdungeons.manager.impl.SpawnPointManagerImpl;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MysticDungeons extends JavaPlugin {
    private DungeonManager dungeonManager;
    private SpawnPointManager spawnPointManager;
    private static MysticDungeons instance;
    private PlayerPointsAPI ppAPI;
    public static final Gson gson = new GsonBuilder()
            .create();

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("MysticDungeons has been enabled!");
        saveConfigFile();
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            this.ppAPI = PlayerPoints.getInstance().getAPI();
        }

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
        dungeonManager.saveDungeons();
        spawnPointManager.saveSpawnPoints();

    }

    public static MysticDungeons getInstance() {
        return instance;
    }
    public void saveConfigFile() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            getLogger().info("Config file not found, creating a new one...");
            saveResource("config.yml", false);
        }
    }


    public DungeonManager dungeonManager() {
        return dungeonManager;
    }

    public SpawnPointManager spawnPointManager() {
        return spawnPointManager;
    }

    public PlayerPointsAPI getPpAPI() {
        return ppAPI;
    }
}
