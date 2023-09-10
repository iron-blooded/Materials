package org.hg.materials.inventory_holders;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Database;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;

public class AddItem implements InventoryHolder, Listener {
    ItemStack target = new ItemStack(Material.TARGET);
    ItemStack accept = new ItemStack(Material.LIME_CONCRETE);
    ItemStack deny = new ItemStack(Material.RED_CONCRETE);
    private static Materials plugin;
    public AddItem(){
        setDisplayName(target, ChatColor.YELLOW+"Положите в центр материал!");
        setDisplayName(accept, ChatColor.GREEN+"Подтвердить добавление материала");
        setDisplayName(deny, ChatColor.RED+"Отклонить добавление материала");
    }
    public AddItem(Materials plugin){
        this.plugin = plugin;
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ChatColor.DARK_AQUA+"Добавление материала");
        inventory.setItem(4, target);
        inventory.setItem(12, target);
        inventory.setItem(14, target);
        inventory.setItem(22, target);
        inventory.setItem(42, accept);
        inventory.setItem(38, deny);
        return inventory;
    }
    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof AddItem){
            event.setCancelled(true);
            int slot = event.getSlot();
            ItemStack item = inventory.getItem(13);
            if (slot == 13) {
                inventory.setItem(13, event.getCursor());
            } else if (slot == 38) {
                event.getWhoClicked().closeInventory(); // отказ от добавления
            } else if (slot == 42 && !(item == null || item.getType().isAir())) {
                plugin.database.addValue(item, new Attributes()); // согласие на добавление
            }
        }
    }
    private void setDisplayName(ItemStack itemStack, String name){
        ItemMeta itemMeta= itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }
}
