package org.hg.materials.inventory_holders.apply_material;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
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
        Inventory open_inventory = event.getView().getTopInventory();
        Inventory click_inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack current_item = event.getCurrentItem();
        InventoryAction action = event.getAction();
        if (open_inventory != null && open_inventory.getHolder() instanceof AnvilApply_H && action != InventoryAction.NOTHING){
            if (click_inventory != open_inventory && action != InventoryAction.MOVE_TO_OTHER_INVENTORY){
                return;
            } else if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
                return;
            } else if (action.name().contains("PICKUP") && click_inventory == open_inventory && event.getSlot() == 2) {
                open_inventory.setItem(0, new ItemStack(Material.AIR));
                open_inventory.setItem(1, new ItemStack(Material.AIR));
                return;
            }
            ItemStack item = open_inventory.getItem(0);
            ItemStack material = open_inventory.getItem(1);
            if (click_inventory == open_inventory){
                if (action.name().contains("PLACE")){
                    if (event.getCursor().getAmount() > 1 && action != InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_ONE && open_inventory.getItem(event.getSlot())!= null){
                        event.setCancelled(true);
                        return;
                    } else if (event.getSlot() == 0 && item != event.getCursor()){
                        item = event.getCursor().clone();
                        item.setAmount(1);
                    } else if (event.getSlot() == 1 && item != event.getCursor()){
                        material = event.getCursor().clone();
                        material.setAmount(1);
                    }
                } else if (action.name().contains("PICKUP")) {
                    if (event.getSlot() == 0){
                        item = null;
                    } else if (event.getSlot() == 1){
                        material = null;
                    }
                }
            }
            UpgradingItem upgradingItem = new UpgradingItem(plugin, item);
            upgradingItem.applyMaterial(material);
            open_inventory.setItem(2, upgradingItem.getItem());
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (inventory != null && inventory.getHolder() instanceof AnvilApply_H){
            try {
                player.getInventory().addItem(inventory.getItem(0));
            } catch (Exception e){}
            try {
                player.getInventory().addItem(inventory.getItem(1));
            } catch (Exception e){}
        }
    }
}
