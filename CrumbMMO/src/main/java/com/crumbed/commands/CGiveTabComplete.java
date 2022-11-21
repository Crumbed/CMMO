package com.crumbed.commands;

import com.crumbed.CrumbMMO;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.crumbed.CrumbMMO.itemReg;

public class CGiveTabComplete implements TabCompleter {

    private CrumbMMO plugin;

    public CGiveTabComplete (CrumbMMO plugin) { this.plugin = plugin; }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) { return null; }
        Player player = (Player) sender;

        if (args.length == 1) {
            HashMap<String, ItemStack> items = itemReg.getItems();
            List<String> options = new ArrayList<>(items.keySet());
            return options;
        } else if (args.length == 2) {
            List<String> options = new ArrayList<>();
            options.add("<amount>");
            return options;
        }

        return null;
    }
}
