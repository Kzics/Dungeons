package com.kzics.mdungeons;

import com.kzics.mdungeons.mobs.SpawnPoint;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class MobSpawnTask extends BukkitRunnable {

    private final MysticDungeons mysticDungeons;
    private final Random random;
    public MobSpawnTask(MysticDungeons plugin) {
        this.runTaskTimer(plugin, 0, 20);

        this.random = new Random();
        this.mysticDungeons = plugin;
    }

    private Location getRandomLocationWithinRadius(SpawnPoint spawnPoint) {
        Location center = spawnPoint.location();
        int radius = spawnPoint.radius();
        int x = center.getBlockX() + random.nextInt(2 * radius + 1) - radius;
        int z = center.getBlockZ() + random.nextInt(2 * radius + 1) - radius;
        int y = center.getWorld().getHighestBlockYAt(x, z);
        return new Location(center.getWorld(), x, y, z);
    }
    private boolean isPlayerNearby(SpawnPoint spawnPoint, int radius) {
        Location center = spawnPoint.location();
        for (Player player : center.getWorld().getPlayers()) {
            if (player.getLocation().distance(center) <= radius) {
                return true;
            }
        }
        return false;
    }

    private void spawnMob(SpawnPoint spawnPoint) {
        Location location = getRandomLocationWithinRadius(spawnPoint);
        EntityType type = spawnPoint.mobProperties().type();
        LivingEntity entity = (LivingEntity) spawnPoint.location().getWorld().spawnEntity(location, type);
        entity.customName(Component.text(spawnPoint.mobProperties().name()));
        AttributeInstance maxHealth = entity.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(spawnPoint.mobProperties().health());
        }
        entity.setHealth(spawnPoint.mobProperties().health());
    }

    @Override
    public void run() {
        for (SpawnPoint spawnPoint : mysticDungeons.spawnPointManager().listSpawnPoints()) {
            long currentTime = System.currentTimeMillis() / 1000L;
            if (currentTime - spawnPoint.lastSpawn() >= spawnPoint.spawnInterval() && isPlayerNearby(spawnPoint, spawnPoint.radius())) {
                spawnMob(spawnPoint);
                spawnPoint.updateLastSpawn();
            }
        }
    }
}
