package org.hg.materials.inventory_holders.view_apply_materials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.hg.materials.Materials;
import org.hg.materials.SerializeItem;
import org.hg.materials.use_materials.UpgradingItem;

import java.util.List;

import static org.hg.materials.Materials.createFlag;

public class ViewApplyMaterials_H implements InventoryHolder {
    private Materials plugin;
    public int page;
    public ItemStack left = createFlag(true);
    public ItemStack right = createFlag(false);
    private ItemStack item;
    public ViewApplyMaterials_H(Materials materials, ItemStack itemStack){
        this.plugin = materials;
        this.item = itemStack;
        this.page = 1;
    }
    public ViewApplyMaterials_H(Materials materials, ItemStack itemStack, int page){
        this.plugin = materials;
        this.item = itemStack;
        this.page = page;
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ChatColor.AQUA+"Просмотр наложенных материалов");
        List<SerializeItem> list = new UpgradingItem(plugin, item).getApplyMaterials();
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
        inventory.setItem(48, left);
        return inventory;
    }
}
