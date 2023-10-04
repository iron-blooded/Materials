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

import java.util.ArrayList;
import java.util.List;

import static org.hg.materials.Materials.createFlag;
import static org.hg.materials.inventory_holders.materials_list.EditEnchant.setDisplayName;

public class CombiningList_H implements InventoryHolder {
    Materials plugin;
    ItemStack item;
    public int page;
    public ItemStack left = createFlag(true);
    public ItemStack right = createFlag(false);
    public ItemStack add_combining = new ItemStack(Material.SUNFLOWER);
//    ItemStack accept = new ItemStack(Material.GREEN_CONCRETE);
    ItemStack deny = new ItemStack(Material.RED_CONCRETE);
    public CombiningList_H(Materials plugin, ItemStack item){
        this.item = item;
        this.plugin = plugin;
        setDisplayName(add_combining, ChatColor.GOLD+"Добавить комбинацию");
//        setDisplayName(accept, ChatColor.GREEN+"Подтвердить изменение комбинаций");
        setDisplayName(deny, ChatColor.RED+"Вернуться назад");

    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ChatColor.AQUA+"Список доступных комбинирований");
        List<SerializeItem> list = new ArrayList<>();
        for (Combination combination : new DatabaseComb(plugin).getValues(new SerializeItem(this.item))){
            list.addAll(combination.items);
        }
        list.removeIf(serializeItem -> serializeItem.getItem().equals(this.item));
        int page = this.page -1;
        if (page<0 || page > list.size()/45){
            page = 0;
            this.page = 1;
        }
        if (list != null && !list.isEmpty()) {
            list = list.subList(Math.min(page * 45, list.size() - 1), Math.min(page * 45 + 45, list.size()));
            for (int i = 0; i <= 45 && i < list.size(); i++){
                inventory.setItem(i, list.get(i).getItem());
            }
        }
//        inventory.setItem(51, accept);
        inventory.setItem(50, right);
        inventory.setItem(49, add_combining);
        inventory.setItem(48, left);
        inventory.setItem(47, deny);
        return inventory;
    }
}
