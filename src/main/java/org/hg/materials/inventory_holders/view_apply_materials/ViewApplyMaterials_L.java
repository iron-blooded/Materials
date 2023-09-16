package org.hg.materials.inventory_holders.view_apply_materials;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ViewApplyMaterials_L implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof ViewApplyMaterials_H && event.getAction() != InventoryAction.CLONE_STACK) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            ViewApplyMaterials_H holder = (ViewApplyMaterials_H) inventory.getHolder();
            if (itemStack == null) {
                return;
            } else if (itemStack.equals(holder.left)) {
                holder.page -= 1;
                player.openInventory(holder.getInventory());
            } else if (itemStack.equals(holder.right)) {
                holder.page += 1;
                player.openInventory(holder.getInventory());
            }
        }
    }
}
