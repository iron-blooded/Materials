package org.hg.materials.inventory_holders.open_combining_list;

import org.bukkit.inventory.InventoryHolder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Combination;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;
import org.hg.materials.inventory_holders.materials_list.EditEnchant;
import org.hg.materials.inventory_holders.materials_list.ListEnchantments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hg.materials.Materials.createFlag;
import static org.hg.materials.inventory_holders.materials_list.EditEnchant.setDisplayName;

public class EditEnchant_H implements InventoryHolder {
    public Materials plugin;
    ItemStack item;
    Combination combination;
    Enchantment select_enchant = Enchantment.CHANNELING;
    HashMap<Enchantment, Integer> editing_enchant = null;
    int value = 1;
    static String str_value;
    ItemStack trackpoint;
    ItemStack confirm = new ItemStack(Material.GREEN_CONCRETE);
    ItemStack cancel = new ItemStack(Material.RED_CONCRETE);
    ItemStack delete = new ItemStack(Material.TNT);
    ItemStack page_left = createFlag(true);
    ItemStack page_right = createFlag(false);
    int page = 1;
    public EditEnchant_H(Materials plugin, Combination combination, HashMap<Enchantment, Integer> editing_enchant){
        init(plugin, combination, editing_enchant, 1);
    }
    public EditEnchant_H(Materials plugin, Combination combination, HashMap<Enchantment, Integer> editing_enchant, int page){
        init(plugin, combination, editing_enchant, page);
    }
    public EditEnchant_H(Materials plugin, Combination combination){
        init(plugin, combination, null, 1);
    }
    private void init(Materials plugin, Combination combination, HashMap<Enchantment, Integer> editing_enchant, int page){
        this.page = page;
        this.plugin = plugin;
        this.combination = combination;
        this.trackpoint = new trackpoint().create(this);
        this.editing_enchant = editing_enchant;
        if (editing_enchant != null){
            for (Enchantment enchantment :editing_enchant.keySet()) {
                this.select_enchant = enchantment;
                this.value = editing_enchant.get(enchantment);
            }
        }
        this.str_value = String.valueOf(value);
        setDisplayName(confirm, ChatColor.GREEN+"Применить изменения");
        setDisplayName(cancel, ChatColor.RED+"Отменить изменения");
        setDisplayName(delete, ChatColor.RED+""+ChatColor.BOLD+"Удалить зачарование");
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ChatColor.DARK_AQUA+"Изменение зачарования");
        if (editing_enchant == null) {
            inventory = Bukkit.createInventory(this, 54, ChatColor.DARK_AQUA + "Добавление зачарования");
        }
        List<Enchantment> list = List.of(Enchantment.values());
        int page = this.page -1;
        if (page<0 || page > list.size()/45){
            page = 0;
            this.page = 1;
        }
        if (!list.isEmpty()) {
            list = list.subList(Math.min(page * 45, list.size() - 1), Math.min(page * 45 + 45, list.size()));
        }
        for (int i = 0; i <= 45 && i < list.size(); i++){
            ItemStack itemStack = new ListEnchantments.enchant_book().getBook(list.get(i), this.value);
            if (!list.get(i).equals(select_enchant)){
                itemStack.setType(Material.BOOK);
            }
            inventory.setItem(i, itemStack);

        }
        if (this.editing_enchant != null) {
            inventory.setItem(53, delete);
        }
        inventory.setItem(51, confirm);
        inventory.setItem(50, page_right);
        inventory.setItem(49, new trackpoint().create(this));
        inventory.setItem(48, page_left);
        inventory.setItem(47, cancel);
        return inventory;
    }
    public static class trackpoint{
        public boolean is(ItemStack itemStack){
            return itemStack.getType().equals(Material.TURTLE_EGG);
        }
        public ItemStack create(EditEnchant_H holder){
            ItemStack itemStack = new ItemStack(Material.TURTLE_EGG);
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW+"Как пользоваться:");
            lore.add(ChatColor.RED+"Наводись сюда и тыкай цифры на клавиатуре");
            lore.add(ChatColor.YELLOW+"Что бы написать ноль, нажми правую кнопку мыши");
            lore.add(ChatColor.YELLOW+"Что бы стереть, нажми левую кнопку мыши");
            lore.add(ChatColor.YELLOW+"Что бы инвертировать значение, нажми Q (выбрось)");
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.AQUA+str_value);
            itemMeta.addEnchant(holder.select_enchant, holder.value, true);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        public void addNumber(int number, EditEnchant_H holder){
            holder.str_value = holder.str_value + number;
        }
        public void backspace(EditEnchant_H holder){
            try {
                holder.str_value = holder.str_value.substring(0, holder.str_value.length() - 1);
            } catch (Exception e){}
        }
        public void invert(EditEnchant_H holder){
            try {
                holder.str_value = String.valueOf(Integer.parseInt(holder.str_value)*-1);
            } catch (Exception e){}
        }

    }
}
