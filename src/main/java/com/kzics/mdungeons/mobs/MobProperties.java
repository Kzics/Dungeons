package com.kzics.mdungeons.mobs;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;
import java.util.List;

public class MobProperties {
    private EntityType type;
    private Component name;
    private int health;
    private int damage;
    private List<Loot> loot;

    public MobProperties(EntityType type, Component name, int health, int damage, List<Loot> loot) {
        this.type = type;
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.loot = loot;
    }

    public EntityType type() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public Component name() {
        return name;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public int health() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int damage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public List<Loot> loot() {
        return loot;
    }

    public void setLoot(List<Loot> loot) {
        this.loot = loot;
    }
}