package org.hg.materials.inventory_holders.open_combining_list;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hg.materials.Combination;
import org.hg.materials.DatabaseComb;
import org.hg.materials.Materials;
import org.hg.materials.SerializeItem;
import org.hg.materials.inventory_holders.materials_list.EditItem;

public class CombiningList_L implements Listener {
    Materials plugin;
    public CombiningList_L(Materials plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof CombiningList_H) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            CombiningList_H holder = (CombiningList_H) inventory.getHolder();
            if (itemStack == null) {
                return;
            } else if (itemStack.equals(holder.deny)) {
                player.openInventory(new EditItem(plugin, holder.item).getInventory());
            } else if (itemStack.equals(holder.left)) {
                holder.page -= 1;
                player.openInventory(holder.getInventory());
            } else if (itemStack.equals(holder.right)) {
                holder.page += 1;
                player.openInventory(holder.getInventory());
            } else if (itemStack.equals(holder.add_combining)) {
                ItemStack create_item = null;
                for (SerializeItem serializeItem : plugin.database.getAllValues()){
                    if (!serializeItem.getItem().equals(holder.item)){
                        create_item = serializeItem.getItem();
                    }
                }
                if (create_item != null){
                    Combination combination = new Combination(plugin);
                    combination.items.add(new SerializeItem(create_item));
                    combination.items.add(new SerializeItem(holder.item));
                    new DatabaseComb(plugin).setValue(combination);
                    player.openInventory(new EditCombining_H(plugin, create_item, holder.item).getInventory());
                }
            } else {
                player.openInventory(new EditCombining_H(plugin, itemStack, holder.item).getInventory());
            }
        }
    }
}
