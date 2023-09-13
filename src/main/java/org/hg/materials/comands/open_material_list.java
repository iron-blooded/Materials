package org.hg.materials.comands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hg.materials.Materials;
import org.hg.materials.inventory_holders.materials_list.ListItems;

public class open_material_list implements CommandExecutor {
    private static Materials plugin;
    public open_material_list(Materials plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        player.openInventory(new ListItems(plugin,1).getInventory());
        return true;
    }
}
