package com.kzics.mdungeons.commands;

import com.kzics.mdungeons.manager.SpawnPointManager;
import com.kzics.mdungeons.mobs.MobProperties;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MobSpawnCommand implements CommandExecutor {

    private final SpawnPointManager spawnPointManager;
    public MobSpawnCommand(SpawnPointManager spawnPointManager) {
        this.spawnPointManager = spawnPointManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length < 5) {
            player.sendMessage("Usage: /mobspawn set <mobType> <name> <spawnInterval> <radius>");
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (!subCommand.equals("set")) {
            player.sendMessage("Usage: /mobspawn set <mobType> <name> <spawnInterval> <radius>");
            return true;
        }

        String mobType = args[1];
        String name = args[2];
        int spawnInterval = Integer.parseInt(args[3]);
        int radius = Integer.parseInt(args[4]);

        MobProperties mobProperties = new MobProperties(EntityType.valueOf(mobType.toUpperCase()), name, 100, 10, new ArrayList<>());
        spawnPointManager.setSpawnPoint(mobProperties, spawnInterval, radius, player);
        player.sendMessage("Spawn point set for " + name + " at your WorldEdit selection.");

        return true;

    }
}
