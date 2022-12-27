package com.crumbed.commands;

import com.crumbed.guis.CraftingGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Craft implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use that command!"); return true; }
        if (!cmd.getName().equals("craft") && !cmd.getName().equals("carft")) return true;
        Player player = (Player) sender;
        if (!player.hasPermission("cmmo.craft")) return true;

        CraftingGui gui = new CraftingGui(player);
        player.openInventory(gui.getInv(player));

        return true;
    }
}
