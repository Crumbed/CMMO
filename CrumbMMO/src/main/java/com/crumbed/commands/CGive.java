package com.crumbed.commands;

import com.crumbed.CrumbMMO;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static com.crumbed.CrumbMMO.itemReg;

public class CGive implements CommandExecutor {

    private CrumbMMO plugin;

    public CGive(CrumbMMO plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this command!"); return true; }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("cgive")) {
            if (args.length == 0) { return help(player); }

            String id = args[0];
            HashMap<String, ItemStack> items = itemReg.getItems();
            if (!(items.containsKey(id)) && !id.equals("menu_glass")) { player.sendMessage(ChatColor.RED+"There is no item with the id \""+id+"\"!"); return true; }

            int count = 1;
            if (args.length == 2) {
                try {
                    count = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED+"Error: \""+args[1]+"\" is not an integer!");
                    return true;
                }
            }

            ItemStack itemToGive;

            if (id.equals("menu_glass")) {
                itemToGive = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                itemToGive.setAmount(count);
                itemToGive.getItemMeta().setDisplayName(ChatColor.BLACK+".");
            } else {
                itemToGive = items.get(id);
                itemToGive.setAmount(count);
            }

            player.getInventory().addItem(itemToGive);

            return true;

        }

        return true;
    }

    private static boolean help(Player player) {

        player.sendMessage(
                ChatColor.GRAY + "Syntax for /cgive: \n"+ChatColor.GREEN
                +"/cgive <item id>\n"
                +"/cgive <item id> <amount>"
        );

        return true;
    }
}
