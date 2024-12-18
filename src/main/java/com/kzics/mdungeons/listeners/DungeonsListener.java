package com.kzics.mdungeons.listeners;

import com.kzics.mdungeons.Dungeon;
import com.kzics.mdungeons.MysticDungeons;
import com.kzics.mdungeons.manager.DungeonManager;
import com.kzics.mdungeons.menu.MobSpawnDetailMenu;
import com.kzics.mdungeons.menu.MysticDungeonsMenu;
import com.kzics.mdungeons.mobs.Loot;
import com.kzics.mdungeons.mobs.SpawnPoint;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DungeonsListener implements Listener {

    private final MysticDungeons mysticDungeons;
    private Random random = new Random();
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
                    event.getDrops().clear();
                    handleLootDrop(entity, spawnPoint);

                    double coinChance = spawnPoint.mobProperties().coinChance();

                    if (random.nextDouble() * 100 <= coinChance) {
                        Player killer = entity.getKiller();
                        if(killer == null) return;
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playerpoints give " + killer.getName() + " " + spawnPoint.mobProperties().coinAmount());
                        killer.sendMessage(Component.text("You received " + spawnPoint.mobProperties().coinAmount() + " coins.", NamedTextColor.GREEN));
                    }
                    break;
                }
            }
        }
    }
    private void handleLootDrop(LivingEntity entity, SpawnPoint spawnPoint) {
        List<Loot> loots = spawnPoint.mobProperties().loot();

        for (Loot loot : loots) {
            double randomChance = random.nextDouble() * 100;

            if (randomChance <= loot.getDropChance()) {
                ItemStack item = loot.getItemStack();
                item.setAmount(1);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
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
                String joinMessage = mysticDungeons.getConfig().getString("player-join-dungeon")
                        .replace("{name}", dungeon.name())
                        .replace("{player}", player.getName());
                dungeon.onPlayerEnter(player, joinMessage);
            } else if (wasInDungeon && !isInDungeon) {
                String leaveMessage = mysticDungeons.getConfig().getString("player-quit-dungeon")
                        .replace("{name}", dungeon.name())
                        .replace("{player}", player.getName());

                dungeon.onPlayerExit(player, leaveMessage);
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