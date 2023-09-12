package org.hg.materials.inventory_holders;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;

public class AddItem implements InventoryHolder, Listener {
    ItemStack target = new ItemStack(Material.TARGET);
    ItemStack accept = new ItemStack(Material.LIME_CONCRETE);
    ItemStack deny = new ItemStack(Material.RED_CONCRETE);
    private static Materials plugin;
    public AddItem(Materials plugin){
        this.plugin = plugin;
        setDisplayName(target, ChatColor.YELLOW+"Положите в центр материал!");
        setDisplayName(accept, ChatColor.GREEN+"Подтвердить добавление материала");
        setDisplayName(deny, ChatColor.RED+"Отклонить добавление материала");
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
            ItemStack itemSlot = event.getCurrentItem();
            if (itemSlot == null){}
            else if (itemSlot.equals(deny)) {
                event.getWhoClicked().openInventory(new ListItems(plugin, 0).getInventory());
            } else if (itemSlot.equals(accept) && item != null) {
                plugin.database.addValue(item, new Attributes()); // согласие на добавление
                item.setAmount(0);
                event.getWhoClicked().sendMessage(ChatColor.GREEN+"Материал успешно добавлен!");
            }
            if (slot == 13) {
                inventory.setItem(13, event.getCursor());
            }
        }
    }
    private void setDisplayName(ItemStack itemStack, String name){
        ItemMeta itemMeta= itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }
}
