package com.crumbed.commands;

import com.crumbed.stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.crumbed.CrumbMMO.statsManager;
import static java.lang.Integer.parseInt;

public class SetStats {

    public static boolean setHealth(CommandSender sender, String[] args) {

        Player player = Bukkit.getPlayer(args[3]);
        if (player == null) { sender.sendMessage(ChatColor.RED + "Player does not exist!"); return true; }

        int newVal;
        try {
            newVal = parseInt(args[4]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid Syntax: "+args[4]+", is not an integer!");
            return true;
        }

        Stats playerStats = statsManager.getStats(player.getUniqueId());

        int baseHealth = playerStats.getBaseHealth();
        playerStats.setBaseHealth(newVal);

        /*
        if (baseHealth > newVal) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + (baseHealth - newVal));
        } else {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + (newVal - baseHealth));
        }
         */

        statsManager.updateStats(player.getUniqueId(), playerStats);

        sender.sendMessage(ChatColor.GREEN + "Successfully set "+args[3]+"'s base health to "+newVal);

        return true;
    }
    public static boolean setDefense(CommandSender sender, String[] args) {

        Player player = Bukkit.getPlayer(args[3]);
        if (player == null) { sender.sendMessage(ChatColor.RED + "Player does not exist!"); return true; }

        int newVal;
        try {
            newVal = parseInt(args[4]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid Syntax: "+args[4]+", is not an integer!");
            return true;
        }

        Stats playerStats = statsManager.getStats(player.getUniqueId());
        int baseDefense = playerStats.getBaseDefense();
        playerStats.setBaseDefense(newVal);
        if (baseDefense > newVal) {
            playerStats.setDefense(playerStats.getDefense() + (baseDefense - newVal));
        } else {
            playerStats.setDefense(playerStats.getDefense() + (newVal - baseDefense));
        }

        statsManager.updateStats(player.getUniqueId(), playerStats);

        sender.sendMessage(ChatColor.GREEN + "Successfully set "+args[3]+"'s base defense to "+newVal);

        return true;
    }
    public static boolean setMana(CommandSender sender, String[] args) {

        Player player = Bukkit.getPlayer(args[3]);
        if (player == null) { sender.sendMessage(ChatColor.RED + "Player does not exist!"); return true; }

        int newVal;
        try {
            newVal = parseInt(args[4]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid Syntax: "+args[4]+", is not an integer!");
            return true;
        }

        Stats playerStats = statsManager.getStats(player.getUniqueId());
        playerStats.setBaseMana(newVal);
        statsManager.updateStats(player.getUniqueId(), playerStats);

        sender.sendMessage(ChatColor.GREEN + "Successfully set "+args[3]+"'s base mana to "+newVal);

        return true;
    }

}
