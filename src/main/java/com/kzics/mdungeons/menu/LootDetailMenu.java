package com.kzics.mdungeons.menu;

import com.kzics.mdungeons.menu.DungeonsListMenu;
import com.kzics.mdungeons.menu.MysticDungeonsMenu;
import com.kzics.mdungeons.mobs.Loot;
import com.kzics.mdungeons.mobs.MobProperties;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

public class LootDetailMenu extends MysticDungeonsMenu {
    private static final NamespacedKey LOOT_KEY = new NamespacedKey("mdungeons", "loot");
    private final MobProperties mobProperties;
    private final Inventory inventory;

    public LootDetailMenu(MobProperties mobProperties) {
        this.mobProperties = mobProperties;
        this.inventory = Bukkit.createInventory(this, 54, Component.text("Loots"));
        setupMenu();
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
    }

    private void setupMenu() {
        inventory.clear(); // Vide l'inventaire pour éviter la duplication
        DungeonsListMenu.addBorder(inventory);

        List<Loot> loots = mobProperties.loot();
        for (Loot loot : loots) {
            inventory.addItem(createLootItem(loot));
        }
    }

    private ItemStack createLootItem(Loot loot) {
        ItemStack item = new ItemStack(loot.getItemStack().getType());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(loot.getItemStack().displayName());
        meta.lore(List.of(
                Component.text("Drop Rate: ", NamedTextColor.WHITE).append(Component.text(loot.getDropChance() + "%", NamedTextColor.GRAY))
        ));
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        meta.getPersistentDataContainer().set(LOOT_KEY, PersistentDataType.STRING, serializer.serialize(loot.getItemStack().displayName()));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void handle(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == player.getInventory()) {
            Loot newLoot = new Loot(clickedItem, 0);
            mobProperties.loot().add(newLoot);
            player.sendMessage(Component.text("Item added to loot list.", NamedTextColor.GREEN));

            // Ajoute seulement le nouvel item au menu sans réinitialiser
            inventory.addItem(createLootItem(newLoot));
            open(player);
        } else {
            String lootName = clickedItem.getItemMeta().getPersistentDataContainer().get(LOOT_KEY, PersistentDataType.STRING);

            if (lootName != null) {
                PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

                Loot loot = mobProperties.loot().stream()
                        .filter(l -> serializer.serialize(l.getItemStack().displayName()).equals(lootName))
                        .findFirst()
                        .orElse(null);

                if (loot != null) {
                    if (event.isLeftClick()) {
                        loot.setDropChance(loot.getDropChance() + 1);
                    } else if (event.isRightClick()) {
                        if (loot.getDropChance() == 0) return;
                        loot.setDropChance(loot.getDropChance() - 1);
                    }
                    clickedItem.setItemMeta(createLootItem(loot).getItemMeta());
                    open(player);
                }
            }
        }
    }
}
