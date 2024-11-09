package com.kzics.mdungeons.mobs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MobProperties {
    private EntityType type;
    private Component name;
    private int health;
    private int damage;
    private List<Loot> loot;
    private double coinChance;
    private int coinAmount;


    public MobProperties(EntityType type, Component name, int health, int damage, List<Loot> loot, double coinChance, int coinAmount) {
        this.type = type;
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.loot = loot;
        this.coinChance = coinChance;
        this.coinAmount = coinAmount;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", type.name());
        json.put("name", GsonComponentSerializer.gson().serialize(name));
        json.put("health", health);
        json.put("damage", damage);
        json.put("coinChance", coinChance);
        json.put("coinAmount", coinAmount);

        JSONArray lootArray = new JSONArray();
        for (Loot lootItem : loot) {
            JSONObject lootJson = new JSONObject();
            lootJson.put("itemStack", lootItem.getItemStack().serialize()); // Sérialiser l'ItemStack
            lootJson.put("dropChance", lootItem.getDropChance());
            lootArray.add(lootJson);
        }
        json.put("loot", lootArray);

        return json;
    }
    public Component name() {
        return name;
    }

    public double coinChance() {
        return coinChance;
    }

    public void setCoinChance(double coinChance) {
        this.coinChance = coinChance;
    }

    public int coinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(int coinAmount) {
        this.coinAmount = coinAmount;
    }

    public EntityType type() {
        return type;
    }

    public int health() {
        return health;
    }

    public int damage() {
        return damage;
    }

    public List<Loot> loot() {
        return loot;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setLoot(List<Loot> loot) {
        this.loot = loot;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public static MobProperties fromJson(JSONObject json) {
        EntityType type = EntityType.valueOf((String) json.get("type"));
        Component name = GsonComponentSerializer.gson().deserialize((String) json.get("name"));
        int health = ((Long) json.get("health")).intValue();
        int damage = ((Long) json.get("damage")).intValue();

        // Lire la liste de loot depuis le JSON
        List<Loot> lootList = new ArrayList<>();
        JSONArray lootArray = (JSONArray) json.get("loot");
        for (Object obj : lootArray) {
            JSONObject lootJson = (JSONObject) obj;
            ItemStack itemStack = ItemStack.deserialize((Map<String, Object>) lootJson.get("itemStack"));
            double dropChance = (double) lootJson.get("dropChance");
            lootList.add(new Loot(itemStack, dropChance));
        }
        double coinChance = (double) json.get("coinChance");
        int coinAmount = ((Long) json.get("coinAmount")).intValue();

        return new MobProperties(type, name, health, damage, lootList, coinChance, coinAmount);
    }}
