package org.hg.materials.inventory_holders.materials_list;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Materials;
import org.hg.materials.SerializeItem;

import java.util.List;

import static org.hg.materials.Materials.createFlag;

public class ListItems implements InventoryHolder, Listener {
    private int page;
    private ItemStack left = createFlag(true);
    private ItemStack right = createFlag(false);
    private ItemStack add_item = new ItemStack(Material.SUNFLOWER);
    private Materials plugin;
    public ListItems(Materials plugin, int page){
        this.page = page;
        this.plugin = plugin;
        setDisplayName(left, ChatColor.AQUA+"На страницу назад");
        setDisplayName(right, ChatColor.AQUA+"На страницу вперед");
        setDisplayName(add_item, ChatColor.YELLOW+"Добавить материал");
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ChatColor.DARK_AQUA+"Список материалов");
        List<SerializeItem> list = plugin.database.getAllValues();
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
        inventory.setItem(49, add_item);
        inventory.setItem(48, left);
        return inventory;
    }
    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof ListItems && event.getAction() != InventoryAction.CLONE_STACK){
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            if (itemStack==null){
                return;
            }
            if (itemStack.equals(right)){
                player.openInventory(new ListItems(plugin,((ListItems) inventory.getHolder()).page+1).getInventory());
            } else if (itemStack.equals(left)) {
                player.openInventory(new ListItems(plugin,((ListItems) inventory.getHolder()).page-1).getInventory());
            } else if (itemStack.equals(add_item)) {
                player.openInventory(new AddItem(plugin).getInventory());
            } else {
                player.openInventory(new EditItem(plugin, itemStack).getInventory());
            }
        }
    }
    private void setDisplayName(ItemStack itemStack, String name){
        ItemMeta itemMeta= itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }
}
