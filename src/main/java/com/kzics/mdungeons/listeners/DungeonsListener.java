package com.kzics.mdungeons.listeners;

import com.kzics.mdungeons.Dungeon;
import com.kzics.mdungeons.MysticDungeons;
import com.kzics.mdungeons.manager.DungeonManager;
import com.kzics.mdungeons.menu.MobSpawnDetailMenu;
import com.kzics.mdungeons.menu.MysticDungeonsMenu;
import com.kzics.mdungeons.mobs.Loot;
import com.kzics.mdungeons.mobs.SpawnPoint;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonsListener implements Listener {

    private final MysticDungeons mysticDungeons;
    public static final Map<Player, MobSpawnDetailMenu.UpdateContext> updateContexts = new HashMap<>();

    public DungeonsListener(MysticDungeons mysticDungeons) {
        this.mysticDungeons = mysticDungeons;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getView().getTopInventory();

        if (inventory.getHolder() instanceof MysticDungeonsMenu menu) {
            menu.handle(event);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Component entityName = entity.customName();

        if (entityName != null) {
            for (SpawnPoint spawnPoint : mysticDungeons.spawnPointManager().listSpawnPoints()) {
                if (spawnPoint.mobProperties().name().equals(entityName)) {
                    handleLootDrop(entity, spawnPoint);
                    break;
                }
            }
        }
    }
    private void handleLootDrop(LivingEntity entity, SpawnPoint spawnPoint) {
        List<Loot> loots = spawnPoint.mobProperties().loot();
        double totalChance = loots.stream().mapToDouble(Loot::getDropChance).sum();
        double randomValue = Math.random() * totalChance;

        double cumulativeChance = 0.0;
        for (Loot loot : loots) {
            cumulativeChance += loot.getDropChance();
            if (randomValue <= cumulativeChance) {
                entity.getWorld().dropItemNaturally(entity.getLocation(), loot.getItemStack());
                break;
            }
        }
    }


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        String message = event.getMessage();

        for (Dungeon dungeon : mysticDungeons.dungeonManager().listDungeons().values()) {
            if (dungeon.isPlayerInDungeon(player.getLocation())) {
                if (!player.isOp()) {
                    player.sendMessage(Component.text("Vous ne pouvez pas utiliser de commandes dans un donjon."));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        DungeonManager dungeonManager = mysticDungeons.dungeonManager();

        for (Dungeon dungeon : dungeonManager.listDungeons().values()) {
            boolean wasInDungeon = dungeon.isPlayerInDungeon(event.getFrom());
            boolean isInDungeon = dungeon.isPlayerInDungeon(event.getTo());

            if (!wasInDungeon && isInDungeon) {
                dungeon.onPlayerEnter(player);
                Bukkit.broadcast(Component.text(player.getName() + " est entré dans le donjon " + dungeon.name()));
            } else if (wasInDungeon && !isInDungeon) {
                dungeon.onPlayerExit(player);
                Bukkit.broadcast(Component.text(player.getName() + " a quitté le donjon " + dungeon.name()));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (updateContexts.containsKey(player)) {
            event.setCancelled(true);
            MobSpawnDetailMenu.UpdateContext context = updateContexts.remove(player);
            context.getUpdateAction().accept(event.getMessage());
            Bukkit.getScheduler().runTask(mysticDungeons, () -> context.getMenu().open(player));

        }
    }
}