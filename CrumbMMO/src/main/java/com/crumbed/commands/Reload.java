package com.crumbed.commands;

import com.crumbed.CrumbMMO;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.crumbed.CrumbMMO.itemManager;
import static com.crumbed.CrumbMMO.mobManager;

public class Reload implements CommandExecutor {

    private CrumbMMO plugin;

    public Reload(CrumbMMO plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equals("creload")) {

            mobManager.reloadMobConfig();
            itemManager.reloadConfig(plugin);
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                itemManager.checkForOutdatedItems(player);
            }

            sender.sendMessage(ChatColor.GREEN+"CMMO Successfully reloaded.");

        }

        return true;
    }
}
