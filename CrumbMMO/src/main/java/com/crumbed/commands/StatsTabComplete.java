package com.crumbed.commands;

import com.crumbed.CrumbMMO;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StatsTabComplete implements TabCompleter {

    private CrumbMMO plugin;

    public StatsTabComplete(CrumbMMO plugin) { this.plugin = plugin; }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> options = new ArrayList<>();

        if (!(sender instanceof Player)) { return null; }
        Player player = (Player) sender;

        if (args.length == 1) {
            if (player.hasPermission("CMMO.Admin")) { options.add("admin"); }
            options.add("show");
            return options;
        } else if (args.length == 2) {
            if (player.hasPermission("CMMO.Admin") && args[0].equalsIgnoreCase("admin")) {
                options.add("reset");
                options.add("set");
                return options;
            }
        } else if (args.length == 3) {
            if (!player.hasPermission("CMMO.Admin")) { return null; }

            if (args[1].equalsIgnoreCase("set")) {
                options.add("health");
                options.add("defense");
                options.add("mana");
                return options;
            }
        } else if (args.length == 5) {
            if (!player.hasPermission("CMMO.Admin")) { return null; }
            options.add("<value>");
            return options;
        }

        return null;
    }
}
