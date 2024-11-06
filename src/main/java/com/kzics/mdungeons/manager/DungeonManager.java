package com.kzics.mdungeons.manager;

import com.kzics.mdungeons.Dungeon;
import com.kzics.mdungeons.MysticDungeons;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DungeonManager {
    private final Map<String, Dungeon> dungeons = new HashMap<>();
    private final File file;

    public DungeonManager(FileConfiguration config) {
        this.file = new File(MysticDungeons.getInstance().getDataFolder(),"dungeons.json");
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

    public void saveDungeons() {
        JSONArray dungeonArray = new JSONArray();

        for (Dungeon dungeon : dungeons.values()) {
            JSONObject dungeonJson = new JSONObject();
            dungeonJson.put("name", dungeon.name());
            dungeonJson.put("point1", locationToJson(dungeon.point1()));
            dungeonJson.put("point2", locationToJson(dungeon.point2()));
            dungeonArray.add(dungeonJson);
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(dungeonArray.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDungeons() {
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JSONParser parser = new JSONParser();
            JSONArray dungeonArray = (JSONArray) parser.parse(reader);
            for (Object obj : dungeonArray) {
                JSONObject dungeonJson = (JSONObject) obj;
                String name = (String) dungeonJson.get("name");
                Location point1 = jsonToLocation((JSONObject) dungeonJson.get("point1"));
                Location point2 = jsonToLocation((JSONObject) dungeonJson.get("point2"));
                dungeons.put(name, new Dungeon(name, point1, point2));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private JSONObject locationToJson(Location location) {
        JSONObject json = new JSONObject();
        json.put("world", location.getWorld().getName());
        json.put("x", location.getX());
        json.put("y", location.getY());
        json.put("z", location.getZ());
        json.put("yaw", location.getYaw());
        json.put("pitch", location.getPitch());
        return json;
    }

    private Location jsonToLocation(JSONObject json) {
        return new Location(
                Bukkit.getWorld((String) json.get("world")),
                ((Number) json.get("x")).doubleValue(),
                ((Number) json.get("y")).doubleValue(),
                ((Number) json.get("z")).doubleValue(),
                ((Number) json.get("yaw")).floatValue(),
                ((Number) json.get("pitch")).floatValue()
        );
    }
}
