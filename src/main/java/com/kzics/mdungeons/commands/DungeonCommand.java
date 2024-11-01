package com.kzics.mdungeons.commands;

import com.kzics.mdungeons.manager.DungeonManager;
import com.kzics.mdungeons.menu.DungeonsListMenu;
import com.kzics.mdungeons.utils.WorldEditUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DungeonCommand implements CommandExecutor, TabCompleter {
    private final DungeonManager dungeonManager;

    public DungeonCommand(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(Component.text("Usage: /dungeon <set|teleport|list> [name]").color(NamedTextColor.RED));
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String name = null;

        switch (subCommand) {
            case "set":
                if (args.length < 2) {
                    player.sendMessage(Component.text("Usage: /dungeon set <name>").color(NamedTextColor.RED));
                    return true;
                }
                name = args[1];
                Location selection = WorldEditUtil.getPlayerSelection(player);
                if (selection == null) {
                    player.sendMessage(Component.text("You need to make a WorldEdit selection first!").color(NamedTextColor.RED));
                    return true;
                }
                dungeonManager.setDungeon(name, selection);
                player.sendMessage(Component.text("Dungeon " + name + " sucessfully created!", NamedTextColor.GREEN));
                break;

            case "teleport":
                if (args.length < 2) {
                    player.sendMessage(Component.text("Usage: /dungeon teleport <name>", NamedTextColor.RED));
                    return true;
                }
                name = args[1];
                Location dungeonLocation = dungeonManager.getDungeon(name).location();
                if (dungeonLocation == null) {
                    player.sendMessage(Component.text("Dungeon " + name + " not found.", NamedTextColor.RED));
                    return true;
                }
                player.teleport(dungeonLocation);
                player.sendMessage(Component.text("Teleported to dungeon " + name + ".", NamedTextColor.GREEN));
                break;

            case "list":
                if (dungeonManager.listDungeons().isEmpty()) {
                    player.sendMessage(Component.text("No dungeons available.", NamedTextColor.RED));
                    return true;
                }
                new DungeonsListMenu(dungeonManager).open(player);
                break;

            default:
                player.sendMessage(Component.text("Unknown subcommand. Usage: /dungeon <set|teleport|list> [name]", NamedTextColor.RED));
                break;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("teleport")) {
            return new ArrayList<>(dungeonManager.listDungeons().keySet());
        }
        return null;
    }

}