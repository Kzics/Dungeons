package com.kzics.mdungeons.manager.impl;

import com.kzics.mdungeons.MysticDungeons;
import com.kzics.mdungeons.manager.SpawnPointManager;
import com.kzics.mdungeons.mobs.MobProperties;
import com.kzics.mdungeons.mobs.SpawnPoint;
import com.kzics.mdungeons.utils.WorldEditUtil;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnPointManagerImpl implements SpawnPointManager {
    private final Map<String, SpawnPoint> spawnPoints = new HashMap<>();
    private final File file;

    public SpawnPointManagerImpl(FileConfiguration config) {
        this.file = new File(MysticDungeons.getInstance().getDataFolder(), "spawnpoints.json");
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
    public Map<String, SpawnPoint> getSpawnPoints() {
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

    public void loadSpawnPoints() {
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JSONParser parser = new JSONParser();
            JSONArray spawnPointArray = (JSONArray) parser.parse(reader);
            for (Object obj : spawnPointArray) {
                JSONObject spawnPointJson = (JSONObject) obj;
                SpawnPoint spawnPoint = SpawnPoint.fromJson(spawnPointJson);
                String name = PlainTextComponentSerializer.plainText().serialize(spawnPoint.mobProperties().name());
                spawnPoints.put(name, spawnPoint);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void saveSpawnPoints() {
        JSONArray spawnPointArray = new JSONArray();

        for (SpawnPoint spawnPoint : spawnPoints.values()) {
            spawnPointArray.add(spawnPoint.toJson());
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(spawnPointArray.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
