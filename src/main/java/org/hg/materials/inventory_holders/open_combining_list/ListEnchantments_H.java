package org.hg.materials.inventory_holders.open_combining_list;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.hg.materials.Combination;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;
import org.hg.materials.inventory_holders.materials_list.ListEnchantments;

import java.util.ArrayList;
import java.util.List;

import static org.hg.materials.Materials.createFlag;
import static org.hg.materials.inventory_holders.materials_list.EditEnchant.setDisplayName;

public class ListEnchantments_H implements InventoryHolder {
    Materials plugin;
    Combination combination;
    ItemStack accept = new ItemStack(Material.GREEN_CONCRETE);
    ItemStack deny = new ItemStack(Material.RED_CONCRETE);
    ItemStack add_enchant = new ItemStack(Material.SUNFLOWER);
    ItemStack page_left = createFlag(true);
    ItemStack page_right = createFlag(false);
    int page = 1;
    public ListEnchantments_H(Materials plugin, Combination combination){
        init(plugin, combination);
    }
    private void init(Materials plugin, Combination combination){
        this.page = page;
        this.plugin = plugin;
        this.combination = combination;
        this.combination.sortItems();
        setDisplayName(accept, ChatColor.GREEN+"Подтвердить изменение зачарований");
        setDisplayName(deny, ChatColor.RED+"Отклонить изменения зачарований");
        setDisplayName(add_enchant, ChatColor.DARK_PURPLE+"Добавить зачарование");
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ChatColor.DARK_AQUA+"Список зачарований");
        List<Enchantment> list = new ArrayList<>();
        list.addAll(combination.attributes.enchantment.keySet());
        int page = this.page -1;
        if (page<0 || page > list.size()/45){
            page = 0;
            this.page = 1;
        }
        if (!list.isEmpty()) {
            list = list.subList(Math.min(page * 45, list.size() - 1), Math.min(page * 45 + 45, list.size()));
        }
        for (int i = 0; i <= 45 && i < list.size(); i++){
            inventory.setItem(i, new ListEnchantments.enchant_book().getBook(list.get(i), combination.attributes.enchantment.get(list.get(i))));
        }
        inventory.setItem(51, accept);
        inventory.setItem(50, page_right);
        inventory.setItem(49, add_enchant);
        inventory.setItem(48, page_left);
        inventory.setItem(47, deny);
        return inventory;
    }
}
