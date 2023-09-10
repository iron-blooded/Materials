package org.hg.materials;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.hg.materials.comands.open_material_list;
import org.hg.materials.inventory_holders.AddItem;
import org.hg.materials.inventory_holders.ListItems;

import java.sql.SQLException;

public final class Materials extends JavaPlugin implements Listener {
    public Database database;
    @Override
    public void onEnable() {
        // Plugin startup logic
        database = new Database(this);
        Bukkit.getServer().getPluginManager().registerEvents(new AddItem(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ListItems(this, 0), this);
        getCommand("open_material_list").setExecutor(new open_material_list(this));
    }

    @Override
    public void onDisable() {
        try {
            database.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
