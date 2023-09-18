package org.hg.materials.inventory_holders.open_combining_list;

import org.bukkit.event.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;
import org.hg.materials.inventory_holders.materials_list.ListEnchantments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hg.materials.Materials.createFlag;

public class EditEnchant_L implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof EditEnchant_H) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            EditEnchant_H holder = (EditEnchant_H) inventory.getHolder();
            if (itemStack == null) {
                return;
            } else if (itemStack.equals(holder.cancel)) {
                player.openInventory(new ListEnchantments_H(holder.plugin, holder.combination).getInventory());
            } else if (itemStack.equals(holder.delete)) {
                 if (holder.editing_enchant!= null){
                    for (Enchantment e :holder.editing_enchant.keySet())
                    holder.combination.attributes.enchantment.remove(e);
                }
                 player.openInventory(new ListEnchantments_H(holder.plugin, holder.combination).getInventory());
            } else if (itemStack.equals(holder.confirm)) {
                if (holder.editing_enchant!= null){
                    for (Enchantment e :holder.editing_enchant.keySet())
                    holder.combination.attributes.enchantment.remove(e);
                }
                try{
                    holder.value = Integer.parseInt(holder.str_value);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"Неверный уровень зачарования!");
                    return;
                }
                holder.combination.attributes.enchantment.put(holder.select_enchant, holder.value);
                player.openInventory(new ListEnchantments_H(holder.plugin, holder.combination).getInventory());
            } else if (itemStack.equals(holder.page_left)) {
                holder.page -= 1;
                player.openInventory(holder.getInventory());
            } else if (itemStack.equals(holder.page_right)) {
                holder.page += 1;
                player.openInventory(holder.getInventory());
            } else if (itemStack.getType().name().equals("BOOK")) {
                for (Enchantment e: new ListEnchantments.enchant_book().getEnchantment(itemStack).keySet())
                holder.select_enchant = e;
                player.openInventory(holder.getInventory());
            } else if (new EditEnchant_H.trackpoint().is(itemStack)) {
                int num = event.getHotbarButton()+1;
                if (num > 0){
                    new EditEnchant_H.trackpoint().addNumber(num, holder);
                } else if (event.getClick() == ClickType.LEFT) {
                    new EditEnchant_H.trackpoint().backspace(holder);
                } else if (event.getClick() == ClickType.RIGHT) {
                    new EditEnchant_H.trackpoint().addNumber(0, holder);
                } else if (event.getClick() == ClickType.DROP) {
                    new EditEnchant_H.trackpoint().invert(holder);
                }
                try{
                    holder.value = Integer.parseInt(holder.str_value);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED+"Неверное число!");
                }
                player.openInventory(holder.getInventory());
            }
        }
    }
}
