package com.kzics.mdungeons.mobs;

import org.bukkit.entity.EntityType;

import java.util.List;

public record MobProperties(EntityType type, String name, int health, int damage, List<Loot> loot) {}
