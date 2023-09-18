package org.hg.materials.inventory_holders.open_combining_list;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hg.materials.DatabaseComb;
import org.hg.materials.inventory_holders.materials_list.ListEnchantments;

public class ListEnchantments_L implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof ListEnchantments_H) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            ListEnchantments_H holder = (ListEnchantments_H) inventory.getHolder();
            if (itemStack == null) {
                return;
            }  else if (itemStack.equals(holder.deny)) {
                player.openInventory(new EditCombining_H(holder.plugin, holder.combination.items.get(0).getItem(), holder.combination.items.get(1).getItem()).getInventory());
            } else if (itemStack.equals(holder.accept)) {
                new DatabaseComb(holder.plugin).deleteValue(holder.combination);
                new DatabaseComb(holder.plugin).setValue(holder.combination);
                player.openInventory(new EditCombining_H(holder.plugin, holder.combination.items.get(0).getItem(), holder.combination.items.get(1).getItem()).getInventory());
            } else if (itemStack.equals(holder.add_enchant)) {
                player.openInventory(new EditEnchant_H(holder.plugin, holder.combination).getInventory());
//                player.openInventory(new EditEnchant(holder.plugin, holder.item, holder.attributes).getInventory());
            } else if (itemStack.getType() == Material.ENCHANTED_BOOK) {
                player.openInventory(new EditEnchant_H(holder.plugin, holder.combination, new ListEnchantments.enchant_book().getEnchantment(itemStack)).getInventory());
//                player.openInventory(new EditEnchant(holder.plugin, holder.item, holder.attributes, new ListEnchantments.enchant_book().getEnchantment(itemStack)).getInventory());
            } else if (itemStack.equals(holder.page_left)) {
                holder.page -= 1;
                player.openInventory(holder.getInventory());
            } else if (itemStack.equals(holder.page_right)) {
                holder.page += 1;
                player.openInventory(holder.getInventory());
            }
        }
    }
}
