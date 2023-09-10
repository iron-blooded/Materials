package org.hg.materials.comands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.hg.materials.Materials;
import org.hg.materials.inventory_holders.AddItem;

public class add_item implements CommandExecutor {
    private static Materials plugin;
    public add_item(Materials plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        player.openInventory(new AddItem().getInventory());
        return true;
    }
}
