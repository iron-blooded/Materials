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
import org.hg.materials.attributes.Attributes;

import java.util.ArrayList;

public class EditCombining_L implements Listener {
    Materials plugin;
    public EditCombining_L(Materials plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof EditCombining_H) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            EditCombining_H holder = (EditCombining_H) inventory.getHolder();
            Combination combination = new Combination(plugin);
            combination.items.add(new SerializeItem(holder.item_editing));
            combination.items.add(new SerializeItem(holder.item_comb));
            combination.attributes = new DatabaseComb(plugin).getAttribute(combination);
            combination.sortItems();
            if (itemStack == null) {
                return;
            } else if (itemStack.equals(holder.playback)) {
                player.openInventory(new CombiningList_H(plugin, holder.item_editing).getInventory());
            } else if (itemStack.equals(holder.delete)) {
                new DatabaseComb(plugin).deleteValue(holder.item_comb, holder.item_editing);
                player.openInventory(new CombiningList_H(plugin, holder.item_editing).getInventory());
            } else if (itemStack.equals(holder.item_comb)) {
                player.openInventory(new SelectMaterialForCombing_H(plugin, holder.item_editing, holder.item_comb).getInventory());
            } else if (itemStack.equals(holder.enchantments)) {
                player.openInventory(new ListEnchantments_H(plugin, combination).getInventory());
            } else if (itemStack.equals(holder.attributes)) {
                player.openInventory(new ListAttributes_H(plugin, combination).getInventory());
            }
        }
    }
}
