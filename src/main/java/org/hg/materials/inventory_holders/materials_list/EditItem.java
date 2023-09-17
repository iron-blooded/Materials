package org.hg.materials.inventory_holders.materials_list;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;
import org.hg.materials.inventory_holders.open_combining_list.CombiningList_H;

import java.util.ArrayList;
import java.util.List;

public class EditItem implements InventoryHolder, Listener {
    private Materials plugin;
    ItemStack item;
    ItemStack playback = new ItemStack(Material.RED_CONCRETE);
    ItemStack enchantments = new ItemStack(Material.ENCHANTED_BOOK);
    ItemStack attributes = new ItemStack(Material.EXPERIENCE_BOTTLE);
    ItemStack delete = new ItemStack(Material.TNT);
    ItemStack combining = new ItemStack(Material.COMPARATOR);
    private int value = 1;
    private String str_value = "1";
    public EditItem(Materials plugin, ItemStack itemStack){
        this.item = itemStack;
        this.plugin = plugin;
        setDisplayName(playback, ChatColor.RED + "Вернуться назад");
        setDisplayName(enchantments, ChatColor.DARK_PURPLE + "Изменить зачарования");
        setDisplayName(attributes, ChatColor.GOLD + "Изменить атрибуты");
        setDisplayName(delete, ChatColor.RED + "" + ChatColor.BOLD + "Удалить предмет");
        setDisplayName(combining, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Изменить комбинации материалов");
            if (itemStack != null && itemStack.getType() != Material.AIR) {
            this.value = plugin.database.getValue(item).limit;
            this.str_value = ""+this.value;
        }
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, InventoryType.DROPPER, ChatColor.DARK_AQUA+"Изменение материала");
        inventory.setItem(0, item);
        inventory.setItem(1, combining);
        inventory.setItem(2, enchantments);
        inventory.setItem(4, new trackpoint().create(this));
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
            } else if (itemStack.equals(combining)) {
                player.openInventory(new CombiningList_H(plugin, holder.item).getInventory());
            } else if (itemStack.equals(delete)) {
                plugin.database.deleteValue(holder.item);
                player.openInventory(new ListItems(plugin, 0).getInventory());
            } else if (new trackpoint().is(itemStack)) {
                int num = event.getHotbarButton()+1;
                if (num > 0){
                    new trackpoint().addNumber(num, holder);
                } else if (event.getClick() == ClickType.LEFT) {
                    new trackpoint().backspace(holder);
                } else if (event.getClick() == ClickType.RIGHT) {
                    new trackpoint().addNumber(0, holder);
                }
                try{
                    holder.value = Integer.parseInt(holder.str_value);
                    Attributes attributes1= plugin.database.getValue(holder.item);
                    attributes1.limit = holder.value;
                    plugin.database.addValue(holder.item, attributes1);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED+"Неверное число!");
                }
                player.openInventory(holder.getInventory());
            }
        }
    }
    private class trackpoint{
        public boolean is(ItemStack itemStack){
            return itemStack.getType().equals(Material.TURTLE_EGG);
        }
        public ItemStack create(EditItem holder){
            ItemStack itemStack = new ItemStack(Material.TURTLE_EGG);
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW+"Как пользоваться:");
            lore.add(ChatColor.RED+"Наводись сюда и тыкай цифры на клавиатуре");
            lore.add(ChatColor.YELLOW+"Что бы написать ноль, нажми правую кнопку мыши");
            lore.add(ChatColor.YELLOW+"Что бы стереть, нажми левую кнопку мыши");
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.AQUA+"Макс. кол-во наложений материала: "+str_value);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        public void addNumber(int number, EditItem holder){
            holder.str_value = holder.str_value + number;
        }
        public void backspace(EditItem holder){
            try {
                holder.str_value = holder.str_value.substring(0, holder.str_value.length() - 1);
            } catch (Exception e){}
        }
        public void invert(EditItem holder){
            try {
                holder.str_value = String.valueOf(Integer.parseInt(holder.str_value)*-1);
            } catch (Exception e){}
        }

    }
    private void setDisplayName(ItemStack itemStack, String name){
        ItemMeta itemMeta= itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }
}
