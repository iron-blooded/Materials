package org.hg.materials.inventory_holders.materials_list;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Materials;

public class EditItem implements InventoryHolder, Listener {
    private Materials plugin;
    ItemStack item;
    ItemStack playback = new ItemStack(Material.RED_CONCRETE);
    ItemStack enchantments = new ItemStack(Material.ENCHANTED_BOOK);
    ItemStack attributes = new ItemStack(Material.EXPERIENCE_BOTTLE);
    ItemStack delete = new ItemStack(Material.TNT);
    public EditItem(Materials plugin, ItemStack itemStack){
        this.item = itemStack;
        this.plugin = plugin;
        setDisplayName(playback, ChatColor.RED+"Вернуться назад");
        setDisplayName(enchantments, ChatColor.DARK_PURPLE+"Изменить зачарования");
        setDisplayName(attributes, ChatColor.GOLD+"Изменить атрибуты");
        setDisplayName(delete, ChatColor.RED+""+ChatColor.BOLD+"Удалить предмет");
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, InventoryType.DROPPER, ChatColor.DARK_AQUA+"Изменение материала");
        inventory.setItem(0, item);
        inventory.setItem(2, enchantments);
        inventory.setItem(5, delete);
        inventory.setItem(6, playback);
        inventory.setItem(8, attributes);
        return inventory;
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof EditItem) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            EditItem holder = (EditItem) inventory.getHolder();
            Player player = (Player) event.getWhoClicked();
            if (itemStack==null){
                return;
            }
            if (itemStack.equals(playback)){
                player.openInventory(new ListItems(plugin, 0).getInventory());
            } else if (itemStack.equals(attributes)) {
                player.openInventory(new ListAttributes(plugin, holder.item).getInventory());
            } else if (itemStack.equals(enchantments)) {
                player.openInventory(new ListEnchantments(plugin, holder.item).getInventory());
            } else if (itemStack.equals(delete)) {
                plugin.database.deleteValue(holder.item);
                player.openInventory(new ListItems(plugin, 0).getInventory());
            }
        }
    }
    private void setDisplayName(ItemStack itemStack, String name){
        ItemMeta itemMeta= itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }
}
