package org.hg.materials.inventory_holders.open_combining_list;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hg.materials.Materials;

import java.util.ArrayList;
import java.util.List;

import static org.hg.materials.inventory_holders.materials_list.EditEnchant.setDisplayName;

public class EditCombining_H implements InventoryHolder {
    private Materials plugin;
    ItemStack item_editing;
    ItemStack item_comb;
    ItemStack enchantments = new ItemStack(Material.ENCHANTED_BOOK);
    ItemStack attributes = new ItemStack(Material.EXPERIENCE_BOTTLE);
    ItemStack delete = new ItemStack(Material.TNT);
    ItemStack playback = new ItemStack(Material.RED_CONCRETE);
    ItemStack info = new ItemStack(Material.PAINTING);
    public EditCombining_H(Materials plugin, ItemStack item_comb, ItemStack item_editing){
        this.plugin = plugin;
        this.item_comb = item_comb.clone();
        this.item_editing = item_editing;
        setDisplayName(playback, ChatColor.RED + "Вернуться назад");
        setDisplayName(enchantments, ChatColor.DARK_PURPLE + "Изменить зачарования");
        setDisplayName(attributes, ChatColor.GOLD + "Изменить атрибуты");
        setDisplayName(delete, ChatColor.RED + "" + ChatColor.BOLD + "Удалить комбинацию");
        setDisplayName(info, ChatColor.WHITE+"Информация");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"Для того, что бы изменить предмет комбинации,");
        lore.add(ChatColor.WHITE+"нажми на предмет слева от данного блока.");
        lore.add(ChatColor.WHITE+"Комбинирование работает просто - если на материал");
        lore.add(ChatColor.WHITE+"наложены два материала, для которых есть комбинация,");
        lore.add(ChatColor.WHITE+"поверх уже существующих свойств накладываются свойства");
        lore.add(ChatColor.WHITE+"которые содержит комбинация.");
        ItemMeta itemMeta = info.getItemMeta();
        itemMeta.setLore(lore);
        info.setItemMeta(itemMeta);
    }
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, InventoryType.DISPENSER, ChatColor.AQUA+"Изменение комбинации");
        inventory.setItem(0, item_comb);
        inventory.setItem(1, info);
        inventory.setItem(2, enchantments);
        inventory.setItem(4, delete);
        inventory.setItem(6, playback);
        inventory.setItem(8, attributes);
        return inventory;
    }
}
