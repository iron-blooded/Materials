package org.hg.materials.use_materials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.hg.materials.Materials;
import org.hg.materials.SerializeItem;
import org.hg.materials.attributes.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UpgradingItem {
    private Materials plugin;
    private ItemStack item = new ItemStack(Material.AIR);
    List<ItemStack> apply_materials = new ArrayList<>();
    public UpgradingItem(Materials plugin, ItemStack itemStack){
        this.plugin = plugin;
        if (itemStack != null) {
            this.item = itemStack;
        }
    }

    public void applyMaterial(ItemStack material){
        if (material!= null) {
            apply_materials.add(material);
        }
    }

    public List<SerializeItem> getApplyMaterials(){
        List<SerializeItem> list = new ArrayList<>();
        ItemMeta itemMeta = this.item.getItemMeta();
        if (itemMeta != null) {
            ArrayList<SerializeItem> database_items = plugin.database.getAllValues();
            for (NamespacedKey key : itemMeta.getPersistentDataContainer().getKeys()) {
                for (SerializeItem serializeItems : database_items) {
                    if (serializeItems.hash().equals(key.getKey())) {
                        list.add(serializeItems);
                    }
                }
            }
        }
        return list;
    }

    public ItemStack getItem() {
        if (item == null){
            return new ItemStack(Material.AIR);
        }
        ItemStack item = this.item.clone();
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null){
            for (ItemStack material: apply_materials) {
                int amount;
                try {
                    amount = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, new SerializeItem(material).hash()), PersistentDataType.INTEGER);
                } catch (NullPointerException e){amount = 0;} catch (Exception e){return new ItemStack(Material.AIR);}
                Attributes attributes = plugin.database.getValue(material);
                if (attributes != null && amount < attributes.limit) {
                    List<String> lore = itemMeta.getLore();
                    if (lore == null){
                        lore = new ArrayList<>();
                    } else {
                        for (String l : itemMeta.getLore()) {
                            if (l.equals(ChatColor.WHITE + "Наложен: " + material.getItemMeta().getDisplayName() + ChatColor.WHITE + " (x" + (amount) + ")")) {
                                lore.remove(l);
                            }
                        }
                    }
                    lore.add(ChatColor.WHITE+"Наложен: "+material.getItemMeta().getDisplayName()+ChatColor.WHITE+" (x"+(amount+1)+")");
                    itemMeta.setLore(lore);
                    for(Attribute attr : attributes.attribute.keySet()){
                        for (AttributeModifier modifier: attributes.attribute.get(attr)) {
                            double multiplier = 0;
                            if (itemMeta.getAttributeModifiers() != null && itemMeta.getAttributeModifiers(attr) != null) {
                                for (AttributeModifier e : itemMeta.getAttributeModifiers(attr)) {
                                    if (e.getSlot() == modifier.getSlot()) {
                                        multiplier += e.getAmount();
                                        itemMeta.removeAttributeModifier(attr, e);
                                    }
                                }
                            }
                            itemMeta.addAttributeModifier(attr, new AttributeModifier(modifier.getUniqueId(), modifier.getName(), modifier.getAmount()+multiplier, modifier.getOperation(), modifier.getSlot()));
                        }
                    }
                    itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, new SerializeItem(material).hash()), PersistentDataType.INTEGER, amount + 1);
                    for (Enchantment enchantment_need: attributes.enchantment.keySet()){
                        int multiplier = 0;
                        if (itemMeta.hasEnchant(enchantment_need)){
                            multiplier += itemMeta.getEnchants().get(enchantment_need).intValue();
                            itemMeta.removeEnchant(enchantment_need);
                        }
                        multiplier += attributes.enchantment.get(enchantment_need).intValue();
                        itemMeta.addEnchant(enchantment_need, multiplier, true);
                    }
                } else {
                    return new ItemStack(Material.AIR);
                }
            }
            item.setItemMeta(itemMeta);
        }
        return item.clone();
    }
}
