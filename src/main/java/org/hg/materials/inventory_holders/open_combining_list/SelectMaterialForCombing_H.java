package org.hg.materials.inventory_holders.open_combining_list;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.hg.materials.Combination;
import org.hg.materials.DatabaseComb;
import org.hg.materials.Materials;
import org.hg.materials.SerializeItem;

import java.util.List;

import static org.hg.materials.Materials.createFlag;
import static org.hg.materials.inventory_holders.materials_list.EditEnchant.setDisplayName;

public class SelectMaterialForCombing_H implements InventoryHolder {
    Materials plugin;
    public int page = 1;
    ItemStack item;
    ItemStack material_editing;
    List<Combination> combinations;
    ItemStack left = createFlag(true);
    ItemStack right = createFlag(false);
    ItemStack deny = new ItemStack(Material.RED_CONCRETE);
    public SelectMaterialForCombing_H(Materials plugin, ItemStack item, ItemStack material_editing){
        this.plugin = plugin;
        this.item = item;
        this.material_editing = material_editing;
        this.combinations = new DatabaseComb(plugin).getValues(new SerializeItem(item));
        setDisplayName(left, ChatColor.AQUA+"На страницу назад");
        setDisplayName(right, ChatColor.AQUA+"На страницу вперед");
        setDisplayName(deny, ChatColor.RED+"Отклонить выбор");
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ChatColor.DARK_AQUA+"Выбор материала для работы комбинации");
        List<SerializeItem> list = plugin.database.getAllValues();
        list.removeIf(serializeItem -> serializeItem.getItem().equals(item));
        int page = this.page -1;
        if (page<0 || page > list.size()/45){
            page = 0;
            this.page = 1;
        }
        if (!list.isEmpty()) {
            list = list.subList(Math.min(page * 45, list.size() - 1), Math.min(page * 45 + 45, list.size()));
        }
        for (int i = 0; i <= 45 && i < list.size(); i++){
            inventory.setItem(i, list.get(i).getItem());
        }
        inventory.setItem(50, right);
        inventory.setItem(49, deny);
        inventory.setItem(48, left);
        return inventory;
    }
}
