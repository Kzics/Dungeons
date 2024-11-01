package com.kzics.mdungeons.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class MysticDungeonsMenu implements InventoryHolder {

    public abstract void open(Player player);
    public abstract void handle(InventoryClickEvent event);
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
