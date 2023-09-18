package org.hg.materials.inventory_holders.open_combining_list;

import org.bukkit.event.Listener;
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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Combination;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;
public class EditAttribute_L implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof EditAttribute_H) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            EditAttribute_H holder = (EditAttribute_H) inventory.getHolder();
            if (itemStack == null){
                return;
            } else if (new EditAttribute_H.type(holder).is(itemStack)) {
                for (Attribute attribute: itemStack.getItemMeta().getAttributeModifiers().keySet()) {
                    holder.select_attribute = attribute;
                }
                player.openInventory(holder.getInventory());
            } else if (new EditAttribute_H.equipment(holder).is(itemStack)) {
                for (AttributeModifier attributeModifier: itemStack.getItemMeta().getAttributeModifiers().values()){
                    holder.equipment_slot = attributeModifier.getSlot();
                }
                player.openInventory(holder.getInventory());
            } else if (new EditAttribute_H.trackpoint(holder).is(itemStack)){
                int num = event.getHotbarButton()+1;
                if (num > 0){
                    new EditAttribute_H.trackpoint(holder).addNumber(num, holder);
                } else if (event.getClick() == ClickType.LEFT) {
                    new EditAttribute_H.trackpoint(holder).backspace(holder);
                } else if (event.getClick() == ClickType.SHIFT_LEFT) {
                    new EditAttribute_H.trackpoint(holder).addPoint(holder);
                } else if (event.getClick() == ClickType.RIGHT) {
                    new EditAttribute_H.trackpoint(holder).addNumber(0, holder);
                } else if (event.getClick() == ClickType.DROP) {
                    new EditAttribute_H.trackpoint(holder).invert(holder);
                }
                try{
                    holder.value = Double.parseDouble(holder.str_value);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED+"Неверное число!");
                }
                player.openInventory(holder.getInventory());
            } else if (itemStack.equals(holder.cancel)) {
                player.openInventory(new ListAttributes_H(holder.plugin, holder.combination).getInventory());
            } else if (itemStack.equals(holder.delete)) {
                if (holder.editing_attribute != null) {
                    for (Attribute attribute :holder.editing_attribute.keySet()) {
                        for (AttributeModifier attributeModifier : holder.editing_attribute.get(attribute)) {
                            holder.combination.attributes.attribute.remove(attribute, attributeModifier);
                        }
                    }
                }
                player.openInventory(new ListAttributes_H(holder.plugin, holder.combination).getInventory());
            } else if (itemStack.equals(holder.confirm)) {
                try{
                    holder.value = Double.parseDouble(holder.str_value);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"Неверное число!");
                    return;
                }
                if (holder.editing_attribute != null) {
                    for (Attribute attribute :holder.editing_attribute.keySet()) {
                        for (AttributeModifier attributeModifier : holder.editing_attribute.get(attribute)) {
                            holder.combination.attributes.attribute.remove(attribute, attributeModifier);
                        }
                    }
                }
                holder.combination.attributes.attribute.put(holder.select_attribute, new AttributeModifier(holder.plugin.uuid, holder.select_attribute.name(), holder.value, AttributeModifier.Operation.ADD_NUMBER, holder.equipment_slot));
                player.openInventory(new ListAttributes_H(holder.plugin, holder.combination).getInventory());
            }
        }
    }
}
