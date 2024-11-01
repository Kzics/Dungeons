package com.kzics.mdungeons.utils;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.PlacementType;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldEditUtil {

    private static WorldEditPlugin getWorldEdit() {

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin instanceof WorldEditPlugin) {
            return (WorldEditPlugin) plugin;
        } else {
            return null;
        }
    }

    public static Location getPlayerSelection(Player player) {
        WorldEditPlugin worldEditPlugin = getWorldEdit();
        if (worldEditPlugin == null) {
            player.sendMessage("WorldEdit plugin not found!");
            return null;
        }

        LocalSession localSession = worldEditPlugin.getSession(player);
        Region selection = null;
        try {
            selection = localSession.getSelection(BukkitAdapter.adapt(player.getWorld()));
        } catch (IncompleteRegionException e) {
            return null;
        }

        if (selection == null) {
            return null;
        }

        World weWorld = selection.getWorld();
        BlockVector3 minPoint = selection.getMinimumPoint();

        return new Location(BukkitAdapter.adapt(weWorld), minPoint.x(), minPoint.y(), minPoint.z());
    }


}
