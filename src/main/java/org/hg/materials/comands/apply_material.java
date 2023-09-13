package org.hg.materials.comands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hg.materials.Materials;
import org.hg.materials.inventory_holders.apply_material.AnvilApply_H;

public class apply_material implements CommandExecutor {
    private Materials plugin;
    public apply_material(Materials materials) {
        this.plugin = materials;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        player.openInventory(new AnvilApply_H(plugin).getInventory());
        return true;
    }
}
