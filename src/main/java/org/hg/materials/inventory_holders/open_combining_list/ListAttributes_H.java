package org.hg.materials.inventory_holders.open_combining_list;

import org.bukkit.inventory.InventoryHolder;
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
import org.hg.materials.Combination;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;

import static org.hg.materials.inventory_holders.materials_list.EditEnchant.setDisplayName;

public class ListAttributes_H implements InventoryHolder {
    public Materials plugin;
    ItemStack accept = new ItemStack(Material.GREEN_CONCRETE);
    ItemStack deny = new ItemStack(Material.RED_CONCRETE);
    ItemStack add_attribute = new ItemStack(Material.SUNFLOWER);
    Combination combination;
    public ListAttributes_H(Materials plugin, Combination combination){
        this.plugin = plugin;
        this.combination = combination;
        setDisplayName(accept, ChatColor.GREEN+"Подтвердить изменение атрибутов");
        setDisplayName(deny, ChatColor.RED+"Отклонить изменения атрибутов");
        setDisplayName(add_attribute, ChatColor.YELLOW+"Добавить атрибут");
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 45, ChatColor.DARK_AQUA+"Список атрибутов");
        int i = 0;
        for (Attribute attribute: combination.attributes.attribute.keySet()){
            for (AttributeModifier attribute_modifier :combination.attributes.attribute.get(attribute)) {
                ItemStack itemStack = new ItemStack(Material.EXPERIENCE_BOTTLE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.WHITE + attribute.name());
                Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
                multimap.put(attribute, attribute_modifier);
                itemMeta.setAttributeModifiers(multimap);
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(i, itemStack);
                i++;
            }
        }
        inventory.setItem(42, accept);
        inventory.setItem(40, add_attribute);
        inventory.setItem(38, deny);
        return inventory;
    }
}
