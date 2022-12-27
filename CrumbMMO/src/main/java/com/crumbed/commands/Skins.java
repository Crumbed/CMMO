package com.crumbed.commands;

import com.crumbed.CrumbMMO;
import com.crumbed.guis.SkinsGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Skins implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use that command!"); return true; }
        if (!cmd.getName().equals("skins")) return true;
        Player player = (Player) sender;
        if (!player.hasPermission("cmmo.skins")) return true;

        player.openInventory(SkinsGui.INV);


        return true;
    }
}
