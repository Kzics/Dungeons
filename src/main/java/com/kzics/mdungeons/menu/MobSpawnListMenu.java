package com.kzics.mdungeons.menu;

import com.kzics.mdungeons.manager.SpawnPointManager;
import com.kzics.mdungeons.mobs.SpawnPoint;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class MobSpawnListMenu extends MysticDungeonsMenu {
    private static final NamespacedKey SPAWN_POINT_KEY = new NamespacedKey("mdungeons", "spawn_point");
    private final SpawnPointManager spawnPointManager;
    private final int page;
    private final Inventory inventory;

    public MobSpawnListMenu(SpawnPointManager spawnPointManager, int page) {
        this.inventory = Bukkit.createInventory(this, 54, Component.text("Mob Spawns - Page " + (page + 1)));

        this.spawnPointManager = spawnPointManager;
        this.page = page;
        setupMenu();
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    private void setupMenu() {
        addBorder(inventory);
        List<SpawnPoint> spawnPoints = spawnPointManager.listSpawnPoints();
        int start = page * 45;
        int end = Math.min(start + 45, spawnPoints.size());

        for (int i = start; i < end; i++) {
            SpawnPoint spawnPoint = spawnPoints.get(i);
            inventory.addItem(createSpawnPointItem(spawnPoint));
        }

        if (page > 0) {
            inventory.setItem(45, createNavigationItem("Previous Page", Material.ARROW));
        }
        if (end < spawnPoints.size()) {
            inventory.setItem(53, createNavigationItem("Next Page", Material.ARROW));
        }

        for (int i = 45; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, createBorderItem());
            }
        }
    }

    private ItemStack createBorderItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(" "));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createNavigationItem(String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createSpawnPointItem(SpawnPoint spawnPoint) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        String name = PlainTextComponentSerializer.plainText().serialize(spawnPoint.mobProperties().name());
        meta.displayName(Component.text("(✱) Mob: " + name, NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
        meta.lore(List.of(
                Component.empty(),
                Component.text("✧ Name: ", NamedTextColor.WHITE).append(spawnPoint.mobProperties().name().color(NamedTextColor.GRAY)),
                Component.text("✧ Health: ", NamedTextColor.WHITE).append(Component.text(spawnPoint.mobProperties().health(), NamedTextColor.GRAY)),
                Component.text("✧ Damage: ", NamedTextColor.WHITE).append(Component.text(spawnPoint.mobProperties().damage(), NamedTextColor.GRAY)),
                Component.text("✧ Spawn Interval: ", NamedTextColor.WHITE).append(Component.text(spawnPoint.spawnInterval(), NamedTextColor.GRAY)),
                Component.text("✧ Radius: ", NamedTextColor.WHITE).append(Component.text(spawnPoint.radius(), NamedTextColor.GRAY)),
                Component.text("Shift-click to remove", NamedTextColor.RED)
        ));
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        meta.getPersistentDataContainer().set(SPAWN_POINT_KEY, PersistentDataType.STRING, serializer.serialize(spawnPoint.mobProperties().name()));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (clickedItem.getType() == Material.ARROW) {
            if (clickedItem.getItemMeta().displayName().equals(Component.text("Previous Page"))) {
                new MobSpawnListMenu(spawnPointManager, page - 1).open(player);
            } else if (clickedItem.getItemMeta().displayName().equals(Component.text("Next Page"))) {
                new MobSpawnListMenu(spawnPointManager, page + 1).open(player);
            }
        } else if (clickedItem.getType() == Material.PAPER) {
            String mobName = clickedItem.getItemMeta().getPersistentDataContainer().get(SPAWN_POINT_KEY, PersistentDataType.STRING);

            if (mobName != null) {
                if (event.isShiftClick()) {
                    spawnPointManager.removeSpawnPoint(mobName);
                    player.sendMessage(Component.text("Spawn point " + mobName + " removed.", NamedTextColor.RED));
                    new MobSpawnListMenu(spawnPointManager, page).open(player);
                } else {
                    SpawnPoint spawnPoint = spawnPointManager.getSpawnPoint(mobName);
                    new MobSpawnDetailMenu(spawnPoint).open(player);
                }
            }
        }
    }

    public static void addBorder(Inventory inventory) {
        DungeonsListMenu.addBorder(inventory);
    }
}