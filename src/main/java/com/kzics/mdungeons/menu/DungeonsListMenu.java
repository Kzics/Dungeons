package com.kzics.mdungeons.menu;

import com.kzics.mdungeons.Dungeon;
import com.kzics.mdungeons.commands.DungeonCommand;
import com.kzics.mdungeons.manager.DungeonManager;
import com.kzics.mdungeons.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class DungeonsListMenu extends MysticDungeonsMenu {
    private final Inventory inventory;
    private static final NamespacedKey DUNGEON_KEY = new NamespacedKey("mdungeons", "dungeon");
    private final DungeonManager dungeonManager;
    public DungeonsListMenu(DungeonManager dungeonManager) {
        this.inventory = Bukkit.createInventory(this, 54, Component.text("Dungeons List"));
        this.dungeonManager = dungeonManager;
    }

    public void open(Player player) {
        addBorder(inventory);

        dungeonManager.listDungeons().forEach((name, dungeon)-> {
            ItemStack item = new ItemBuilder(Material.PAPER)
                    .addPersistentData(DUNGEON_KEY, name)
                    .setName(Component.text("(✱) ").color(NamedTextColor.GOLD)
                            .append(Component.text("Dungeon: " + name)))
                    .setLore()
                    .build();
            inventory.addItem(item);
        });
        player.openInventory(inventory);
    }

    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }

        if (item.getItemMeta().getPersistentDataContainer().has(DUNGEON_KEY, PersistentDataType.STRING)) {
            String name = item.getItemMeta().getPersistentDataContainer().get(DUNGEON_KEY, PersistentDataType.STRING);

            Player player = (Player) event.getWhoClicked();

            Bukkit.dispatchCommand(player, "dungeon teleport " + name);
            player.closeInventory();
        }
    }

    public static void addBorder(Inventory inventory) {
        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

        int size = inventory.getSize();
        int rows = size / 9;

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, blackPane);
            inventory.setItem(size - 1 - i, blackPane);
        }

        for (int i = 0; i < rows; i++) {
            inventory.setItem(i * 9, blackPane);
            inventory.setItem(i * 9 + 8, blackPane);
        }
    }


}
