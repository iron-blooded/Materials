package org.hg.materials.inventory_holders.open_combining_list;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Combination;
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;
import org.hg.materials.inventory_holders.materials_list.EditAttribute;

import java.util.ArrayList;
import java.util.List;

import static org.hg.materials.inventory_holders.materials_list.EditEnchant.setDisplayName;

public class EditAttribute_H implements InventoryHolder{
    Materials plugin;
    Combination combination;
    Attribute select_attribute = Attribute.GENERIC_ATTACK_DAMAGE;
    Multimap<Attribute, AttributeModifier> editing_attribute = null;
    EquipmentSlot equipment_slot = EquipmentSlot.HAND;
    double value = 1;
    String str_value;
    ItemStack trackpoint;
    ItemStack confirm = new ItemStack(Material.GREEN_CONCRETE);
    ItemStack cancel = new ItemStack(Material.RED_CONCRETE);
    ItemStack delete = new ItemStack(Material.TNT);
    public EditAttribute_H(Materials plugin, Combination combination, Multimap<Attribute, AttributeModifier> editing_attribute){
        init(plugin, combination, editing_attribute);
    }
    public EditAttribute_H(Materials plugin, Combination combination){
        init(plugin, combination, null);
    }
    private void init(Materials plugin, Combination combination, Multimap<Attribute, AttributeModifier> editing_attribute){
        this.plugin = plugin;
        this.combination = combination;
        this.trackpoint = new trackpoint(this).create(this);
        this.editing_attribute = editing_attribute;
        if (editing_attribute != null){
            for (Attribute attribute :editing_attribute.keySet()) {
                this.select_attribute = attribute;
                for (AttributeModifier attributeModifier : editing_attribute.get(attribute)) {
                    this.equipment_slot = attributeModifier.getSlot();
                    this.value = attributeModifier.getAmount();
                }
            }
        }
        this.str_value = String.valueOf(value);
        setDisplayName(confirm, ChatColor.GREEN+"Применить изменения");
        setDisplayName(cancel, ChatColor.RED+"Отменить изменения");
        setDisplayName(delete, ChatColor.RED+""+ChatColor.BOLD+"Удалить атрибут");
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, ChatColor.DARK_AQUA+"Изменение атрибута");
        if (editing_attribute == null){
            inventory = Bukkit.createInventory(this, 54, ChatColor.DARK_AQUA+"Добавление атрибута");
        }
        int i = 0;
        for (Attribute attribute: Attribute.values()){
            if (i == 11){
                i += 5;
            }
            if (this.select_attribute.equals(attribute)){
                inventory.setItem(i, new EditAttribute_H.type(this).select(attribute));
            }else {
                inventory.setItem(i, new type(this).unselect(attribute));
            }
            i++;
        }
        i = 4+9;
        for (EquipmentSlot equipmentSlot: EquipmentSlot.values()){
            if (i == 4+9+1){
                i += 6;
            }
            if (this.equipment_slot.equals(equipmentSlot)){
                inventory.setItem(i, new equipment(this).selected(equipmentSlot));
            } else {
                inventory.setItem(i, new equipment(this).unselected(equipmentSlot));
            }
            i++;
        }
        this.trackpoint = new trackpoint(this).create(this);
        inventory.setItem(4+9*4, trackpoint);
        inventory.setItem(2+9*5, cancel);
        inventory.setItem(6+9*5, confirm);
        if (this.editing_attribute != null) {
            inventory.setItem(6 + 9 * 5 + 2, delete);
        }
        return inventory;
    }
    public static class trackpoint{
        EditAttribute_H holder;
        public trackpoint(EditAttribute_H holder){
            this.holder = holder;
        }
        public boolean is(ItemStack itemStack){
            return itemStack.getType().equals(Material.TURTLE_EGG);
        }
        public ItemStack create(EditAttribute_H holder){
            ItemStack itemStack = new ItemStack(Material.TURTLE_EGG);
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW+"Как пользоваться:");
            lore.add(ChatColor.RED+"Наводись сюда и тыкай цифры на клавиатуре");
            lore.add(ChatColor.YELLOW+"Что бы написать ноль, нажми правую кнопку мыши");
            lore.add(ChatColor.YELLOW+"Что бы стереть, нажми левую кнопку мыши");
            lore.add(ChatColor.YELLOW+"Что бы поставить точку, нажми SHIFT + ЛКМ");
            lore.add(ChatColor.YELLOW+"Что бы инвертировать значение, нажми Q (выбрось)");
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.AQUA+ holder.str_value);
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            AttributeModifier attributeModifier = new AttributeModifier(holder.plugin.uuid, holder.select_attribute.name(), holder.value, AttributeModifier.Operation.ADD_NUMBER, holder.equipment_slot);
            multimap.put(holder.select_attribute, attributeModifier);
            itemMeta.setAttributeModifiers(multimap);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        public void addNumber(int number, EditAttribute_H holder){
            holder.str_value = holder.str_value + number;
        }
        public void backspace(EditAttribute_H holder){
            try {
                holder.str_value = holder.str_value.substring(0, holder.str_value.length() - 1);
            } catch (Exception e){}
        }
        public void addPoint(EditAttribute_H holder){
            if (!holder.str_value.contains(".")) {
                holder.str_value = holder.str_value + ".";
            }
        }
        public void invert(EditAttribute_H holder){
            try {
                holder.str_value = String.valueOf(Double.parseDouble(holder.str_value)*-1);
            } catch (Exception e){}
        }

    }
    public static class equipment{
        EditAttribute_H holder;
        public equipment(EditAttribute_H holder){
            this.holder = holder;
        }
        public boolean is(ItemStack itemStack){
            return itemStack.getType().equals(Material.HONEY_BLOCK) || itemStack.getType().equals(Material.SLIME_BLOCK);
        }
        public ItemStack unselected(EquipmentSlot equipmentSlot){
            ItemStack itemStack = new ItemStack(Material.HONEY_BLOCK);
            setDisplayName(itemStack, ChatColor.GOLD+"Выберите нужный тип слота (рука, броня)");
            ItemMeta itemMeta = itemStack.getItemMeta();
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            AttributeModifier attributeModifier = new AttributeModifier(holder.plugin.uuid, holder.select_attribute.name(), holder.value, AttributeModifier.Operation.ADD_NUMBER, equipmentSlot);
            multimap.put(holder.select_attribute, attributeModifier);
            itemMeta.setAttributeModifiers(multimap);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        public ItemStack selected(EquipmentSlot equipmentSlot){
            ItemStack itemStack = new ItemStack(Material.SLIME_BLOCK);
            setDisplayName(itemStack, ChatColor.GOLD+"Выбран данный тип слота");
            ItemMeta itemMeta = itemStack.getItemMeta();
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            AttributeModifier attributeModifier = new AttributeModifier(holder.plugin.uuid, holder.select_attribute.name(), holder.value, AttributeModifier.Operation.ADD_NUMBER, equipmentSlot);
            multimap.put(holder.select_attribute, attributeModifier);
            itemMeta.setAttributeModifiers(multimap);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }
    public static class type {
        EditAttribute_H holder;
        public type(EditAttribute_H holder){
            this.holder = holder;
        }
        public boolean is(ItemStack itemStack){
            return itemStack.getType().name().contains("_CANDLE");
        }
        public ItemStack unselect(Attribute attribute){
            ItemStack itemStack = new ItemStack(Material.RED_CANDLE);
            setDisplayName(itemStack, ChatColor.GOLD+"Выберите нужный тип атрибута");
            ItemMeta itemMeta = itemStack.getItemMeta();
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            AttributeModifier attributeModifier = new AttributeModifier(holder.plugin.uuid, attribute.name(), holder.value, AttributeModifier.Operation.ADD_NUMBER, holder.equipment_slot);
            multimap.put(attribute, attributeModifier);
            itemMeta.setAttributeModifiers(multimap);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        public ItemStack select(Attribute attribute){
            ItemStack itemStack = new ItemStack(Material.GREEN_CANDLE);
            setDisplayName(itemStack, ChatColor.GOLD+"Выбран данный тип атрибута");
            ItemMeta itemMeta = itemStack.getItemMeta();
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            AttributeModifier attributeModifier = new AttributeModifier(holder.plugin.uuid, attribute.name(), holder.value, AttributeModifier.Operation.ADD_NUMBER, holder.equipment_slot);
            multimap.put(attribute, attributeModifier);
            itemMeta.setAttributeModifiers(multimap);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }
}
