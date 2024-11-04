package com.kzics.mdungeons.mobs;

import org.bukkit.inventory.ItemStack;

public class Loot {
    private ItemStack itemStack;
    private double dropChance;

    public Loot(ItemStack itemStack, double dropChance) {
        this.itemStack = itemStack;
        this.dropChance = dropChance;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public double getDropChance() {
        return dropChance;
    }

    public void setDropChance(double dropChance) {
        this.dropChance = dropChance;
    }
}