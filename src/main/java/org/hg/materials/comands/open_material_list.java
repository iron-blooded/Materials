package org.hg.materials.comands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.hg.materials.Materials;
import org.hg.materials.inventory_holders.AddItem;

public class open_material_list implements CommandExecutor {
    private static Materials plugin;
    public open_material_list(Materials plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        return true;
    }
}
