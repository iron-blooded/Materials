package org.hg.materials.inventory_holders;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
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

import java.util.*;

import static org.hg.materials.Materials.createFlag;

public class ListEnchantments implements InventoryHolder, Listener {
    private Materials plugin;
    ItemStack item;
    ItemStack accept = new ItemStack(Material.GREEN_CONCRETE);
    ItemStack deny = new ItemStack(Material.RED_CONCRETE);
    ItemStack add_enchant = new ItemStack(Material.SUNFLOWER);
    ItemStack page_left = createFlag(true);
    ItemStack page_right = createFlag(false);
    Attributes attributes;
    int page = 1;
    public ListEnchantments(Materials plugin, ItemStack item){
        init(plugin, item, plugin.database.getValue(item), 1);
    }
    public ListEnchantments(Materials plugin, ItemStack item, Attributes attributes){
        init(plugin, item, attributes, 1);
    }
    public ListEnchantments(Materials plugin, ItemStack item, Attributes attributes, int page){
        init(plugin, item, attributes, page);
    }
    private void init(Materials plugin, ItemStack item, Attributes attributes, int page){
        this.page = page;
        this.plugin = plugin;
        this.item = item;
        setDisplayName(accept, ChatColor.GREEN+"Подтвердить изменение зачарований");
        setDisplayName(deny, ChatColor.RED+"Отклонить изменения зачарований");
        setDisplayName(add_enchant, ChatColor.DARK_PURPLE+"Добавить зачарование");
        this.attributes = attributes;
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ChatColor.DARK_AQUA+"Список зачарований");
        List<Enchantment> list = new ArrayList<>();
        for (Enchantment ench : attributes.enchantment.keySet()){
            list.add(ench);
        }
        int page = this.page -1;
        if (page<0 || page > list.size()/45){
            page = 0;
            this.page = 1;
        }
        if (!list.isEmpty()) {
            list = list.subList(Math.min(page * 45, list.size() - 1), Math.min(page * 45 + 45, list.size()));
        }
        for (int i = 0; i <= 45 && i < list.size(); i++){
            inventory.setItem(i, new enchant_book().getBook(list.get(i), attributes.enchantment.get(list.get(i))));
        }
        inventory.setItem(51, accept);
        inventory.setItem(50, page_right);
        inventory.setItem(49, add_enchant);
        inventory.setItem(48, page_left);
        inventory.setItem(47, deny);
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
                player.openInventory(new EditEnchant(plugin, holder.item, holder.attributes).getInventory());
            } else if (itemStack.getType() == Material.ENCHANTED_BOOK) {
                player.openInventory(new EditEnchant(plugin, holder.item, holder.attributes, new enchant_book().getEnchantment(itemStack)).getInventory());
            } else if (itemStack.equals(page_left)) {
                holder.page -= 1;
                player.openInventory(holder.getInventory());
            } else if (itemStack.equals(page_right)) {
                holder.page += 1;
                player.openInventory(holder.getInventory());
            }
        }
    }
    private void setDisplayName(ItemStack itemStack, String name){
        ItemMeta itemMeta= itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }
    private class enchant_book{
        public ItemStack getBook(Enchantment enchantment, int level){
            ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.LIGHT_PURPLE+enchantment.getName());
            lore.add(ChatColor.WHITE+"Уровень: " + ChatColor.YELLOW+level);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        public HashMap<Enchantment, Integer> getEnchantment(ItemStack itemStack){
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = itemMeta.getLore();
            Enchantment enchantment = Enchantment.CHANNELING;
            int value = 1;
            for (String str: lore){
                if (str.contains(ChatColor.LIGHT_PURPLE+"")){
                    enchantment = Enchantment.getByName(str.split(ChatColor.LIGHT_PURPLE+"")[1]);
                } else if (str.contains(ChatColor.YELLOW+"")) {
                    value = Integer.parseInt(str.split(ChatColor.YELLOW+"")[1]);
                }
            }
            HashMap<Enchantment, Integer> map = new HashMap<>();
            map.put(enchantment, value);
            return map;
        }
    }
}
