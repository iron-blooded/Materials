package org.hg.materials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.hg.materials.comands.apply_material;
import org.hg.materials.comands.open_material_list;
import org.hg.materials.comands.view_apply_materials;
import org.hg.materials.inventory_holders.apply_material.AnvilApply_L;
import org.hg.materials.inventory_holders.materials_list.*;
import org.hg.materials.inventory_holders.open_combining_list.*;
import org.hg.materials.inventory_holders.view_apply_materials.ViewApplyMaterials_L;

import java.sql.SQLException;
import java.util.UUID;

public final class Materials extends JavaPlugin implements Listener {
    public Database database;
    public UUID uuid = UUID.fromString("22fa46b8-1326-4a32-a971-5dc39bd5dfa4");

    @Override
    public void onEnable() {
        // Plugin startup logic
        database = new Database(this);
        Bukkit.getServer().getPluginManager().registerEvents(new AddItem(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EditItem(this, null), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ListItems(this, 0), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ListAttributes(this, new ItemStack(Material.AIR)), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ListEnchantments(this, new ItemStack(Material.AIR)), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EditAttribute(this, new ItemStack(Material.AIR), null), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EditEnchant(this, new ItemStack(Material.AIR), null), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AnvilApply_L(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ViewApplyMaterials_L(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CombiningList_L(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EditCombining_L(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new SelectMaterialForCombing_L(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ListEnchantments_L(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EditEnchant_L(), this);
        getCommand("open_material_list").setExecutor(new open_material_list(this));
        getCommand("apply_material").setExecutor(new apply_material(this));
        getCommand("view_apply_materials").setExecutor(new view_apply_materials(this));
    }

    @Override
    public void onDisable() {
        try {
            database.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ItemStack createFlag(boolean left) {
        // Создаем ItemStack с типом GRAY_BANNER
        ItemStack flag = new ItemStack(Material.GRAY_BANNER);
        ItemMeta meta = flag.getItemMeta();
        BannerMeta bannerMeta = (BannerMeta) meta;
        if (left) {
            bannerMeta.setDisplayName(ChatColor.AQUA+"На страницу назад");
            bannerMeta.addPattern(new Pattern(DyeColor.ORANGE, PatternType.STRIPE_LEFT));
        } else {
            bannerMeta.setDisplayName(ChatColor.AQUA+"На страницу вперед");
            bannerMeta.addPattern(new Pattern(DyeColor.ORANGE, PatternType.STRIPE_RIGHT));
        }
        bannerMeta.addPattern(new Pattern(DyeColor.GRAY, PatternType.CURLY_BORDER));
        bannerMeta.addPattern(new Pattern(DyeColor.GRAY, PatternType.STRIPE_BOTTOM));
        bannerMeta.addPattern(new Pattern(DyeColor.GRAY, PatternType.STRIPE_TOP));
        bannerMeta.addPattern(new Pattern(DyeColor.ORANGE, PatternType.STRIPE_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.GRAY, PatternType.CURLY_BORDER));
        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        flag.setItemMeta(bannerMeta);
        return flag;
    }
}
