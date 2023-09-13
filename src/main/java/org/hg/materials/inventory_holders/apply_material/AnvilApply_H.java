package org.hg.materials.inventory_holders.apply_material;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.hg.materials.Materials;

public class AnvilApply_H implements InventoryHolder {
    private Materials plugin;
    public AnvilApply_H(Materials plugin){
        this.plugin = plugin;
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, InventoryType.ANVIL, ChatColor.AQUA+"Наложение материала");
        return inventory;
    }
}
