package org.hg.materials.use_materials;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.hg.materials.Materials;
import org.hg.materials.SerializeItem;
import org.hg.materials.attributes.Attributes;

import java.util.ArrayList;
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
        if (material!= null && plugin.database.getValue(material)!=null) {
            apply_materials.add(material);
        }
    }

    public ItemStack getItem() {
        ItemStack item = this.item.clone();
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null){
            for (ItemStack material: apply_materials) {
                int limit = plugin.database.getValue(material).limit;
                int amount = 0;
                try {
                    amount = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, new SerializeItem(material).hash()), PersistentDataType.INTEGER) | 0;
                } catch (Exception e){}
                if (amount < limit) {
                    itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, new SerializeItem(material).hash()), PersistentDataType.INTEGER, amount + 1);
                }
            }
            item.setItemMeta(itemMeta);
        }
        return item.clone();
    }
}
