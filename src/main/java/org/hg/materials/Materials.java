package org.hg.materials;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.hg.materials.comands.add_item;
import org.hg.materials.comands.open_material_list;
import org.hg.materials.inventory_holders.AddItem;

public final class Materials extends JavaPlugin implements Listener {
    public Database database;
    @Override
    public void onEnable() {
        // Plugin startup logic
        database = new Database(this);
        Bukkit.getServer().getPluginManager().registerEvents(new AddItem(this), this);
        getCommand("open_material_list").setExecutor(new open_material_list(this));
        getCommand("add_item").setExecutor(new add_item(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
