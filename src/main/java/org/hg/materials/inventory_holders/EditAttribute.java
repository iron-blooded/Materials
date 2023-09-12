package org.hg.materials.inventory_holders;

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
import org.hg.materials.Materials;
import org.hg.materials.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;

public class EditAttribute implements InventoryHolder, Listener {
    private Materials plugin;
    ItemStack item;
    Attributes attributes;
    Attribute select_attribute = Attribute.GENERIC_ATTACK_DAMAGE;
    Multimap<Attribute, AttributeModifier> editing_attribute = null;
    EquipmentSlot equipment_slot = EquipmentSlot.HAND;
    double value = 1;
    String str_value;
    ItemStack trackpoint;
    ItemStack confirm = new ItemStack(Material.GREEN_CONCRETE);
    ItemStack cancel = new ItemStack(Material.RED_CONCRETE);
    ItemStack delete = new ItemStack(Material.TNT);
    public EditAttribute(Materials plugin, ItemStack item, Attributes attributes, Multimap<Attribute, AttributeModifier> editing_attribute){
        init(plugin, item, attributes, editing_attribute);
    }
    public EditAttribute(Materials plugin, ItemStack item, Attributes attributes){
        init(plugin, item, attributes, null);
    }
    private void init(Materials plugin, ItemStack item, Attributes attributes, Multimap<Attribute, AttributeModifier> editing_attribute){
        this.plugin = plugin;
        this.item = item;
        this.attributes = attributes;
        this.trackpoint = new trackpoint().create(this);
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
        int i = 0;
        for (Attribute attribute: Attribute.values()){
            if (i == 11){
                i += 5;
            }
            if (this.select_attribute.equals(attribute)){
                inventory.setItem(i, new type().select(attribute));
            }else {
                inventory.setItem(i, new type().unselect(attribute));
            }
            i++;
        }
        i = 4+9;
        for (EquipmentSlot equipmentSlot: EquipmentSlot.values()){
            if (i == 4+9+1){
                i += 6;
            }
            if (this.equipment_slot.equals(equipmentSlot)){
                inventory.setItem(i, new equipment().selected(equipmentSlot));
            } else {
                inventory.setItem(i, new equipment().unselected(equipmentSlot));
            }
            i++;
        }
        this.trackpoint = new trackpoint().create(this);
        inventory.setItem(4+9*4, trackpoint);
        inventory.setItem(2+9*5, cancel);
        inventory.setItem(6+9*5, confirm);
        if (this.editing_attribute != null) {
            inventory.setItem(6 + 9 * 5 + 2, delete);
        }
        return inventory;
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getHolder() instanceof EditAttribute) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            Player player = (Player) event.getWhoClicked();
            EditAttribute holder = (EditAttribute) inventory.getHolder();
            if (itemStack == null){
                return;
            } else if (new type().is(itemStack)) {
                for (Attribute attribute: itemStack.getItemMeta().getAttributeModifiers().keySet()) {
                    holder.select_attribute = attribute;
                }
                player.openInventory(holder.getInventory());
            } else if (new equipment().is(itemStack)) {
                for (AttributeModifier attributeModifier: itemStack.getItemMeta().getAttributeModifiers().values()){
                    holder.equipment_slot = attributeModifier.getSlot();
                }
                player.openInventory(holder.getInventory());
            } else if (new trackpoint().is(itemStack)){
                int num = event.getHotbarButton()+1;
                if (num > 0){
                    new trackpoint().addNumber(num, holder);
                } else if (event.getClick() == ClickType.LEFT) {
                    new trackpoint().backspace(holder);
                } else if (event.getClick() == ClickType.SHIFT_LEFT) {
                    new trackpoint().addPoint(holder);
                } else if (event.getClick() == ClickType.RIGHT) {
                    new trackpoint().addNumber(0, holder);
                } else if (event.getClick() == ClickType.DROP) {
                    new trackpoint().invert(holder);
                }
                try{
                    holder.value = Double.parseDouble(holder.str_value);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED+"Неверное число!");
                }
                player.openInventory(holder.getInventory());
            } else if (itemStack.equals(cancel)) {
                player.openInventory(new ListAttributes(plugin, holder.item, holder.attributes).getInventory());
            } else if (itemStack.equals(delete)) {
                if (holder.editing_attribute != null) {
                    for (Attribute attribute :holder.editing_attribute.keySet()) {
                        for (AttributeModifier attributeModifier : holder.editing_attribute.get(attribute)) {
                            holder.attributes.attribute.remove(attribute, attributeModifier);
                        }
                    }
                }
                player.openInventory(new ListAttributes(plugin, holder.item, holder.attributes).getInventory());
            } else if (itemStack.equals(confirm)) {
                try{
                    holder.value = Double.parseDouble(holder.str_value);
                } catch (Exception e){
                    player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"Неверное число!");
                    return;
                }
                if (holder.editing_attribute != null) {
                    for (Attribute attribute :holder.editing_attribute.keySet()) {
                        for (AttributeModifier attributeModifier : holder.editing_attribute.get(attribute)) {
                            holder.attributes.attribute.remove(attribute, attributeModifier);
                        }
                    }
                }
                holder.attributes.attribute.put(holder.select_attribute, new AttributeModifier(plugin.uuid, holder.select_attribute.name(), holder.value, AttributeModifier.Operation.ADD_NUMBER, holder.equipment_slot));
                player.openInventory(new ListAttributes(plugin, holder.item, holder.attributes).getInventory());
            }
        }
    }
    private class trackpoint{
        public boolean is(ItemStack itemStack){
            return itemStack.getType().equals(Material.TURTLE_EGG);
        }
        public ItemStack create(EditAttribute holder){
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
            itemMeta.setDisplayName(ChatColor.AQUA+str_value);
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            AttributeModifier attributeModifier = new AttributeModifier(plugin.uuid, holder.select_attribute.name(), value, AttributeModifier.Operation.ADD_NUMBER, holder.equipment_slot);
            multimap.put(holder.select_attribute, attributeModifier);
            itemMeta.setAttributeModifiers(multimap);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        public void addNumber(int number, EditAttribute holder){
            holder.str_value = holder.str_value + number;
        }
        public void backspace(EditAttribute holder){
            try {
                holder.str_value = holder.str_value.substring(0, holder.str_value.length() - 1);
            } catch (Exception e){}
        }
        public void addPoint(EditAttribute holder){
            if (!holder.str_value.contains(".")) {
                holder.str_value = holder.str_value + ".";
            }
        }
        public void invert(EditAttribute holder){
            try {
                holder.str_value = String.valueOf(Double.parseDouble(holder.str_value)*-1);
            } catch (Exception e){}
        }

    }
    private class equipment{
        public boolean is(ItemStack itemStack){
            return itemStack.getType().equals(Material.HONEY_BLOCK) || itemStack.getType().equals(Material.SLIME_BLOCK);
        }
        public ItemStack unselected(EquipmentSlot equipmentSlot){
            ItemStack itemStack = new ItemStack(Material.HONEY_BLOCK);
            setDisplayName(itemStack, ChatColor.GOLD+"Выберите нужный тип слота (рука, броня)");
            ItemMeta itemMeta = itemStack.getItemMeta();
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            AttributeModifier attributeModifier = new AttributeModifier(plugin.uuid, select_attribute.name(), value, AttributeModifier.Operation.ADD_NUMBER, equipmentSlot);
            multimap.put(select_attribute, attributeModifier);
            itemMeta.setAttributeModifiers(multimap);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        public ItemStack selected(EquipmentSlot equipmentSlot){
            ItemStack itemStack = new ItemStack(Material.SLIME_BLOCK);
            setDisplayName(itemStack, ChatColor.GOLD+"Выбран данный тип слота");
            ItemMeta itemMeta = itemStack.getItemMeta();
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            AttributeModifier attributeModifier = new AttributeModifier(plugin.uuid, select_attribute.name(), value, AttributeModifier.Operation.ADD_NUMBER, equipmentSlot);
            multimap.put(select_attribute, attributeModifier);
            itemMeta.setAttributeModifiers(multimap);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }
    private class type {
        public boolean is(ItemStack itemStack){
            return itemStack.getType().name().contains("_CANDLE");
        }
        public ItemStack unselect(Attribute attribute){
            ItemStack itemStack = new ItemStack(Material.RED_CANDLE);
            setDisplayName(itemStack, ChatColor.GOLD+"Выберите нужный тип атрибута");
            ItemMeta itemMeta = itemStack.getItemMeta();
            Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();
            AttributeModifier attributeModifier = new AttributeModifier(plugin.uuid, attribute.name(), value, AttributeModifier.Operation.ADD_NUMBER, equipment_slot);
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
            AttributeModifier attributeModifier = new AttributeModifier(plugin.uuid, attribute.name(), value, AttributeModifier.Operation.ADD_NUMBER, equipment_slot);
            multimap.put(attribute, attributeModifier);
            itemMeta.setAttributeModifiers(multimap);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }
    private static void setDisplayName(ItemStack itemStack, String name){
        ItemMeta itemMeta= itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }
}
