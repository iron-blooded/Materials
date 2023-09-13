package org.hg.materials.comands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.hg.materials.Materials;
import org.hg.materials.inventory_holders.AddItem;

public class test implements CommandExecutor {
    Materials plugin;
    public test(Materials materials) {
        this.plugin = materials;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player =  (Player) commandSender;
        Inventory inventory = Bukkit.createInventory(new AddItem(plugin), InventoryType.FURNACE);
        player.openInventory(inventory);
        return false;
    }
}
