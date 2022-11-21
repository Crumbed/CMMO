package com.crumbed.commands;

import com.crumbed.CrumbMMO;
import com.crumbed.stats.Stats;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.crumbed.CrumbMMO.statsManager;

public class StatsCmd implements CommandExecutor {

    private CrumbMMO plugin;

    public StatsCmd(CrumbMMO plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("stats")) {
            if (!(args.length > 0)) { return help(sender); }
            switch (args[0].toLowerCase()) {
                case "admin":
                    if (!(args.length > 2)) { return help(sender); }
                    if (sender instanceof Player && !(sender.hasPermission("CMMO.Admin")) || !(sender.hasPermission("CMMO.StatsAdmin"))) {
                        sender.sendMessage(ChatColor.RED + "You don't have access to this command!");
                        return true;
                    }
                    switch (args[1].toLowerCase()) {
                        case "reset":
                            return ResetStats.reset(sender, args);
                        case "set":
                            if (!(args.length == 5)) { return help(sender); }
                            switch (args[2].toLowerCase()) {
                                case "health":
                                    return SetStats.setHealth(sender, args);
                                case "defense":
                                    return SetStats.setDefense(sender, args);
                                case "mana":
                                    return SetStats.setMana(sender, args);
                            }
                    }
                    help(sender);
                    break;
                case "show":
                    if (!(sender instanceof Player)) { sender.sendMessage(ChatColor.RED + "Only players may use this!"); return true; }
                    Player player = (Player) sender;
                    Stats playerStats = statsManager.getStats(player.getUniqueId());
                    sender.sendMessage(
                            ChatColor.GREEN + "Your Stats: \n"
                            +ChatColor.GRAY + "Health: "+ChatColor.RED+""+(int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()+"\n"
                            +ChatColor.GRAY + "Defense: "+ChatColor.GREEN+""+playerStats.getDefense()+"\n"
                            +ChatColor.GRAY + "Mana: "+ChatColor.AQUA+""+playerStats.getMaxMana()+"\n"
                            +ChatColor.GRAY + "Damage: "+ChatColor.RED+""+playerStats.getDamage()+"\n"
                            +ChatColor.GRAY + "Strength: "+ChatColor.RED+""+playerStats.getStrength()+"\n"
                            +ChatColor.GRAY + "Crit Damage: "+ChatColor.BLUE+""+playerStats.getCritDamage()+"%\n"
                            +ChatColor.GRAY + "Crit Chance: "+ChatColor.BLUE+""+(int)(playerStats.getCritChance()*100)+"%"
                    );
                    return true;
            }

        }

        return true;
    }


    public boolean help(CommandSender sender) {
        sender.sendMessage(
                ChatColor.GRAY + "Syntax for /stats: \n"+ChatColor.GREEN
                +"/stats admin reset <player>\n"
                +"/stats admin set <stat> <player> <integer>\n"
                +"/stats show"
        );
        return true;
    }

}
