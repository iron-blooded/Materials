package org.hg.materials.inventory_holders.materials_list;

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
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hg.materials.Materials.createFlag;

public class EditEnchant implements InventoryHolder, Listener {
    private Materials plugin;
    ItemStack item;
    Attributes attributes;
    Enchantment select_enchant = Enchantment.CHANNELING;
    HashMap<Enchantment, Integer> editing_enchant = null;
    int value = 1;
    String str_value;
    ItemStack trackpoint;
    ItemStack confirm = new ItemStack(Material.GREEN_CONCRETE);
    ItemStack cancel = new ItemStack(Material.RED_CONCRETE);
    ItemStack delete = new ItemStack(Material.TNT);
    ItemStack page_left = createFlag(true);
    ItemStack page_right = createFlag(false);
    int page = 1;
    public EditEnchant(Materials plugin, ItemStack item, Attributes attributes, HashMap<Enchantment, Integer> editing_enchant){
        init(plugin, item, attributes, editing_enchant, 1);
    }
    public EditEnchant(Materials plugin, ItemStack item, Attributes attributes, HashMap<Enchantment, Integer> editing_enchant, int page){
        init(plugin, item, attributes, editing_enchant, page);
    }
    public EditEnchant(Materials plugin, ItemStack item, Attributes attributes){
        init(plugin, item, attributes, null, 1);
    }
    private void init(Materials plugin, ItemStack item, Attributes attributes, HashMap<Enchantment, Integer> editing_enchant, int page){
        this.page = page;
        this.plugin = plugin;
        this.item = item;
        this.attributes = attributes;
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
            ItemStack itemStack = new enchant_book().getBook(list.get(i), this.value);
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
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof EditEnchant) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            EditEnchant holder = (EditEnchant) inventory.getHolder();
            if (itemStack == null) {
                return;
            } else if (itemStack.equals(cancel)) {
                player.openInventory(new ListEnchantments(plugin, holder.item, holder.attributes).getInventory());
            } else if (itemStack.equals(delete)) {
                 if (holder.editing_enchant!= null){
                    for (Enchantment e :holder.editing_enchant.keySet())
                    holder.attributes.enchantment.remove(e);
                }
                 player.openInventory(new ListEnchantments(plugin, holder.item, holder.attributes).getInventory());
            } else if (itemStack.equals(confirm)) {
                if (holder.editing_enchant!= null){
                    for (Enchantment e :holder.editing_enchant.keySet())
                    holder.attributes.enchantment.remove(e);
                }
                try{
                    holder.value = Integer.parseInt(holder.str_value);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"Неверный уровень зачарования!");
                    return;
                }
                holder.attributes.enchantment.put(holder.select_enchant, holder.value);
                player.openInventory(new ListEnchantments(plugin, holder.item, holder.attributes).getInventory());
            } else if (itemStack.equals(page_left)) {
                holder.page -= 1;
                player.openInventory(holder.getInventory());
            } else if (itemStack.equals(page_right)) {
                holder.page += 1;
                player.openInventory(holder.getInventory());
            } else if (itemStack.getType().name().equals("BOOK")) {
                for (Enchantment e: new enchant_book().getEnchantment(itemStack).keySet())
                holder.select_enchant = e;
                player.openInventory(holder.getInventory());
            } else if (new trackpoint().is(itemStack)) {
                int num = event.getHotbarButton()+1;
                if (num > 0){
                    new trackpoint().addNumber(num, holder);
                } else if (event.getClick() == ClickType.LEFT) {
                    new trackpoint().backspace(holder);
                } else if (event.getClick() == ClickType.RIGHT) {
                    new trackpoint().addNumber(0, holder);
                } else if (event.getClick() == ClickType.DROP) {
                    new trackpoint().invert(holder);
                }
                try{
                    holder.value = Integer.parseInt(holder.str_value);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED+"Неверное число!");
                }
                player.openInventory(holder.getInventory());
            }
        }
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
    private class type {
        public boolean is(ItemStack itemStack){
            return itemStack.getType().name().contains("BOOK");
        }
        public ItemStack unselect(Enchantment enchantment, EditEnchant holder){
            ItemStack itemStack = new enchant_book().getBook(enchantment, holder.value);
            setDisplayName(itemStack, ChatColor.GOLD+"Выберите нужный тип зачарования");
            itemStack.setType(Material.BOOK);
            return itemStack;
        }
        public ItemStack select(Enchantment enchantment, EditEnchant holder){
            ItemStack itemStack = new enchant_book().getBook(enchantment, holder.value);
            setDisplayName(itemStack, ChatColor.GOLD+"Выбран данный тип зачарования");
            return itemStack;
        }
    }
    private class trackpoint{
        public boolean is(ItemStack itemStack){
            return itemStack.getType().equals(Material.TURTLE_EGG);
        }
        public ItemStack create(EditEnchant holder){
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
        public void addNumber(int number, EditEnchant holder){
            holder.str_value = holder.str_value + number;
        }
        public void backspace(EditEnchant holder){
            try {
                holder.str_value = holder.str_value.substring(0, holder.str_value.length() - 1);
            } catch (Exception e){}
        }
        public void invert(EditEnchant holder){
            try {
                holder.str_value = String.valueOf(Integer.parseInt(holder.str_value)*-1);
            } catch (Exception e){}
        }

    }
    private static void setDisplayName(ItemStack itemStack, String name){
        ItemMeta itemMeta= itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }
}
