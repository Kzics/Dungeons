package com.kzics.mdungeons.commands;

import com.kzics.mdungeons.manager.DungeonManager;
import com.kzics.mdungeons.utils.WorldEditUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DungeonCommand implements CommandExecutor {
    private final DungeonManager dungeonManager;

    public DungeonCommand(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("Usage: /dungeon <set|teleport|list> [name]");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String name = null;

        switch (subCommand) {
            case "set":
                if (args.length < 2) {
                    player.sendMessage("Usage: /dungeon set <name>");
                    return true;
                }
                name = args[1];
                Location selection = WorldEditUtil.getPlayerSelection(player);
                if (selection == null) {
                    player.sendMessage("You need to make a WorldEdit selection first!");
                    return true;
                }
                dungeonManager.setDungeon(name, selection);
                player.sendMessage("Dungeon " + name + " set at your WorldEdit selection.");
                break;

            case "teleport":
                if (args.length < 2) {
                    player.sendMessage("Usage: /dungeon teleport <name>");
                    return true;
                }
                name = args[1];
                Location dungeonLocation = dungeonManager.getDungeon(name).location();
                if (dungeonLocation == null) {
                    player.sendMessage("Dungeon " + name + " not found.");
                    return true;
                }
                player.teleport(dungeonLocation);
                player.sendMessage("Teleported to dungeon " + name + ".");
                break;

            case "list":
                if (dungeonManager.listDungeons().isEmpty()) {
                    player.sendMessage("No dungeons available.");
                    return true;
                }
                player.sendMessage("Available dungeons:");
                dungeonManager.listDungeons().forEach((dungeonName, dungeon) -> {
                    Location loc = dungeon.location();
                    player.sendMessage("- " + dungeonName + ": " + loc.getWorld().getName() + " (" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
                });
                break;

            default:
                player.sendMessage("Unknown subcommand. Usage: /dungeon <set|teleport|list> [name]");
                break;
        }

        return true;
    }
}