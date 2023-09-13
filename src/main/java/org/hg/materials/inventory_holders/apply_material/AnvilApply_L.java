package org.hg.materials.inventory_holders.apply_material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.hg.materials.Materials;
import org.hg.materials.use_materials.UpgradingItem;

public class AnvilApply_L implements Listener {
    private Materials plugin;
    public AnvilApply_L(Materials materials) {
        this.plugin = materials;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof AnvilApply_H){
            if (event.getSlot() == 2  && event.getCurrentItem() != null){
                inventory.setItem(0, null);
                inventory.setItem(1, null);
            } else {
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                    AnvilApply_H holder = (AnvilApply_H) inventory.getHolder();
                    ItemStack material = inventory.getItem(1);
                    ItemStack item = inventory.getItem(0);
                    ItemStack result_item = item.clone();
                    UpgradingItem upgradingItem = new UpgradingItem(plugin, result_item);
                    upgradingItem.applyMaterial(material);
                    inventory.setItem(2, upgradingItem.getItem());
                    Player player = (Player) event.getWhoClicked();
                    }
                };
                runnable.runTaskLater(plugin, 1);
            }
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (inventory != null && inventory.getHolder() instanceof AnvilApply_H){
            player.getInventory().addItem(inventory.getItem(0));
            player.getInventory().addItem(inventory.getItem(1));
        }
    }
}
