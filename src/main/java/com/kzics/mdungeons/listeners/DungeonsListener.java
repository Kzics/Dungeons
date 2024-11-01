package com.kzics.mdungeons.listeners;

import com.kzics.mdungeons.MysticDungeons;
import com.kzics.mdungeons.menu.DungeonsListMenu;
import com.kzics.mdungeons.menu.MysticDungeonsMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;

public class DungeonsListener implements Listener {

    private final MysticDungeons mysticDungeons;
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
    public void onCommand(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        String message = event.getMessage();
        ;

    }
}
