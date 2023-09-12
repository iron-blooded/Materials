package org.hg.materials.inventory_holders;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;

public class ListEnchantments implements InventoryHolder, Listener {
    private Materials plugin;
    ItemStack item;
    ItemStack accept = new ItemStack(Material.GREEN_CONCRETE);
    ItemStack deny = new ItemStack(Material.RED_CONCRETE);
    ItemStack add_enchant = new ItemStack(Material.SUNFLOWER);
    Attributes attributes;
    public ListEnchantments(Materials plugin, ItemStack item){
        init(plugin, item, plugin.database.getValue(item));
    }
    public ListEnchantments(Materials plugin, ItemStack item, Attributes attributes){
        init(plugin, item, attributes);
    }
    private void init(Materials plugin, ItemStack item, Attributes attributes){
        this.plugin = plugin;
        this.item = item;
        setDisplayName(accept, ChatColor.GREEN+"Подтвердить изменение атрибутов");
        setDisplayName(deny, ChatColor.RED+"Отклонить изменения атрибутов");
        setDisplayName(add_enchant, ChatColor.DARK_PURPLE+"Добавить зачарование");
        this.attributes = attributes;
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 45, ChatColor.DARK_AQUA+"Список зачарований");
        int i = 0;
        for (Enchantment enchantment: attributes.enchantment.keySet()){
            ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.WHITE + enchantment.getName());
            itemMeta.addEnchant(enchantment, attributes.enchantment.get(enchantment), true);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(i, itemStack);
            i++;
        }
        inventory.setItem(42, accept);
        inventory.setItem(40, add_enchant);
        inventory.setItem(38, deny);
        return inventory;
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof ListEnchantments) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            ListEnchantments holder = (ListEnchantments) inventory.getHolder();
            if (itemStack == null) {
                return;
            }  else if (itemStack.equals(deny)) {
                player.openInventory(new EditItem(plugin, holder.item).getInventory());
            } else if (itemStack.equals(accept)) {
                plugin.database.addValue(holder.item, holder.attributes);
                player.openInventory(new EditItem(plugin, holder.item).getInventory());
            } else if (itemStack.equals(add_enchant)) {

            } else if (itemStack.getType() == Material.ENCHANTED_BOOK) {

            }
        }
    }
    private void setDisplayName(ItemStack itemStack, String name){
        ItemMeta itemMeta= itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }
}
