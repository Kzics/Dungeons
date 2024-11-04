package com.kzics.mdungeons.menu;

import com.kzics.mdungeons.MysticDungeons;
import com.kzics.mdungeons.listeners.DungeonsListener;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MobSpawnDetailMenu extends MysticDungeonsMenu {
    private static final NamespacedKey SPAWN_POINT_KEY = new NamespacedKey("mdungeons", "spawn_point");
    private final SpawnPoint spawnPoint;
    private final Inventory inventory;
    private final Map<String, Consumer<String>> updateActions = new HashMap<>();

    public MobSpawnDetailMenu(SpawnPoint spawnPoint) {
        this.spawnPoint = spawnPoint;
        this.inventory = Bukkit.createInventory(this, 27, Component.text("Mob Spawn Details"));
        setupMenu();
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
    }

    private void setupMenu() {
        DungeonsListMenu.addBorder(inventory);
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        inventory.setItem(22, createDetailItem("Name", serializer.serialize(spawnPoint.mobProperties().name())));
        inventory.setItem(10, createDetailItem("Health", String.valueOf(spawnPoint.mobProperties().health())));
        inventory.setItem(12, createDetailItem("Damage", String.valueOf(spawnPoint.mobProperties().damage())));
        inventory.setItem(14, createDetailItem("Spawn Interval", String.valueOf(spawnPoint.spawnInterval())));
        inventory.setItem(16, createDetailItem("Radius", String.valueOf(spawnPoint.radius())));
        inventory.setItem(4,createDetailItem("Loot", "Click to view loot"));
    }

    private ItemStack createDetailItem(String name, String value) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("✱ " + name).color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true));
        meta.lore(List.of(Component.empty(),
                Component.text("➔ ", NamedTextColor.GRAY).append(Component.text("Value: ", NamedTextColor.WHITE))
                        .append(Component.text(value, NamedTextColor.GREEN))));
        meta.getPersistentDataContainer().set(SPAWN_POINT_KEY, PersistentDataType.STRING, name);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        String detailName = clickedItem.getItemMeta().getPersistentDataContainer().get(SPAWN_POINT_KEY, PersistentDataType.STRING);

        if (detailName != null) {
            player.closeInventory();
            if(detailName.equals("Loot")){
                new LootDetailMenu(spawnPoint.mobProperties()).open(player);
                return;
            }
            player.sendMessage(Component.text("Please enter a new value for " + detailName + ":"));
            DungeonsListener.updateContexts.put(player, new UpdateContext(this, newValue -> updateDetail(detailName, newValue)));
        }
    }

    private void updateDetail(String detailName, String newValue) {
        switch (detailName) {
            case "Name":
                Component oldName = spawnPoint.mobProperties().name();
                PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
                spawnPoint.mobProperties().setName(Component.text(newValue));
                MysticDungeons.getInstance().spawnPointManager().updateSpawnPointKey(serializer.serialize(oldName), newValue);
                break;
            case "Health":
                spawnPoint.mobProperties().setHealth(Integer.parseInt(newValue));
                break;
            case "Damage":
                spawnPoint.mobProperties().setDamage(Integer.parseInt(newValue));
                break;
            case "Spawn Interval":
                spawnPoint.setSpawnInterval(Integer.parseInt(newValue));
                break;
            case "Radius":
                spawnPoint.setRadius(Integer.parseInt(newValue));
                break;
        }
        setupMenu();
    }

    public static class UpdateContext {
        private final MobSpawnDetailMenu menu;
        private final Consumer<String> updateAction;

        public UpdateContext(MobSpawnDetailMenu menu, Consumer<String> updateAction) {
            this.menu = menu;
            this.updateAction = updateAction;
        }

        public MobSpawnDetailMenu getMenu() {
            return menu;
        }

        public Consumer<String> getUpdateAction() {
            return updateAction;
        }
    }
}