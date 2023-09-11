package org.hg.materials;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.hg.materials.comands.open_material_list;
import org.hg.materials.inventory_holders.*;

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
        Bukkit.getServer().getPluginManager().registerEvents(new ListAttributes(this, null), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EditAttribute(this, null, null), this);
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
