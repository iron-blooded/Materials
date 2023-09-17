package org.hg.materials.inventory_holders.open_combining_list;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hg.materials.Combination;
import org.hg.materials.DatabaseComb;
import org.hg.materials.SerializeItem;
import org.hg.materials.attributes.Attributes;

import java.util.ArrayList;

public class SelectMaterialForCombing_L implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof SelectMaterialForCombing_H) {
            event.setCancelled(true);
            ItemStack itemClick = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            SelectMaterialForCombing_H holder = (SelectMaterialForCombing_H) inventory.getHolder();
            if (itemClick == null) {
                return;
            } else if (itemClick.equals(holder.right)) {
                holder.page += 1;
                player.openInventory(holder.getInventory());
            } else if (itemClick.equals(holder.left)) {
                holder.page -= 1;
                player.openInventory(holder.getInventory());
            } else if (itemClick.equals(holder.deny)) {
                player.openInventory(new EditCombining_H(holder.plugin, holder.material_editing, holder.item).getInventory());
            } else {
                ArrayList<SerializeItem> or_list = new ArrayList<>();
                or_list.add(new SerializeItem(holder.material_editing));
                or_list.add(new SerializeItem(holder.item));
                ArrayList<SerializeItem> rep_list = new ArrayList<>();
                rep_list.add(new SerializeItem(itemClick));
                rep_list.add(new SerializeItem(holder.item));
                new DatabaseComb(holder.plugin).replaceValues(new Combination(holder.plugin, or_list, new Attributes()), new Combination(holder.plugin, rep_list, new Attributes()), true);
                player.openInventory(new EditCombining_H(holder.plugin, itemClick, holder.item).getInventory());
            }
        }
    }
}
