package org.hg.materials.comands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hg.materials.Materials;
import org.hg.materials.inventory_holders.view_apply_materials.ViewApplyMaterials_H;

public class view_apply_materials implements CommandExecutor {
    private Materials plugin;
    public view_apply_materials(Materials materials) {
        this.plugin = materials;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        player.openInventory(new ViewApplyMaterials_H(plugin, player.getInventory().getItemInMainHand()).getInventory());
        return true;
    }
}
