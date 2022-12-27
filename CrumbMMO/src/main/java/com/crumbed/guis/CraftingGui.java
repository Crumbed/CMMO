package com.crumbed.guis;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

public class CraftingGui {

    private Inventory inv;
    public static HashMap<Player, Inventory> playerGuis = new HashMap<>();
    private int[] empty = new int[]{10,11,12,19,20,21,28,29,30};
    private int[] redGlass = new int[]{1,2,3,9,13,16,18,22,24,26,27,31,34,37,38,39};

    public CraftingGui(Player player) {
        inv = Bukkit.createInventory(player, 45, "Crafting Table");
        ItemStack menu_glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = menu_glass.getItemMeta();
        meta.setDisplayName(ChatColor.BLACK+".");
        menu_glass.setItemMeta(meta);
        for (int i = 0; i < 45; i++) {
            if (ArrayUtils.contains(empty, i)) continue;
            if (ArrayUtils.contains(redGlass, i)) {
                ItemStack red_glass = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                red_glass.setItemMeta(meta);
                inv.setItem(i, red_glass);
            }
            if (i == 23) {
                ItemStack arrow = new ItemStack(Material.ARROW);
                ItemMeta arrowMeta = arrow.getItemMeta();
                arrowMeta.setDisplayName(ChatColor.GRAY+"ARROW");
                arrow.setItemMeta(arrowMeta);

                inv.setItem(i, arrow);
                continue;
            } else if (i == 25) {
                ItemStack result = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta resultMeta = result.getItemMeta();
                resultMeta.setDisplayName(ChatColor.RED+"No Result");
                result.setItemMeta(resultMeta);

                inv.setItem(i, result);
                continue;
            }

            inv.setItem(i, menu_glass);
            playerGuis.put(player, inv);
        }
    }

    public Inventory getInv(Player player) {
        return playerGuis.get(player);
    }

}
