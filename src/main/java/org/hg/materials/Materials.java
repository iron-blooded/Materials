package org.hg.materials;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.hg.materials.comands.open_material_list;

public final class Materials extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
//        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getCommand("open_material_list").setExecutor(new open_material_list(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
