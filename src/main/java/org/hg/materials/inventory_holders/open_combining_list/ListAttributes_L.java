package org.hg.materials.inventory_holders.open_combining_list;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.DatabaseComb;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;
import org.bukkit.event.Listener;

public class ListAttributes_L implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof ListAttributes_H) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            ListAttributes_H holder = (ListAttributes_H) inventory.getHolder();
            if (itemStack == null){
                return;
            } else if (itemStack.equals(holder.deny)) {
                player.openInventory(new EditCombining_H(holder.plugin, holder.combination.items.get(0).getItem(), holder.combination.items.get(1).getItem()).getInventory());
            } else if (itemStack.equals(holder.accept)) {
                new DatabaseComb(holder.plugin).deleteValue(holder.combination);
                new DatabaseComb(holder.plugin).setValue(holder.combination);
                player.openInventory(new EditCombining_H(holder.plugin, holder.combination.items.get(0).getItem(), holder.combination.items.get(1).getItem()).getInventory());
            } else if (itemStack.equals(holder.add_attribute)) {
                player.openInventory(new EditAttribute_H(holder.plugin, holder.combination).getInventory());
            } else if (itemStack.getType() == Material.EXPERIENCE_BOTTLE) {
                player.openInventory(new EditAttribute_H(holder.plugin, holder.combination, itemStack.getItemMeta().getAttributeModifiers()).getInventory());
            }
        }
    }
}
