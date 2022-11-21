package com.crumbed.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class SkinsGui {

    private Inventory inv;
    private HashMap<String, String> skins;

    public SkinsGui() {
        inv = Bukkit.createInventory(null, 27, ChatColor.BLACK+""+ChatColor.BOLD+"Skin Anvil");
        ItemStack menu_glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = menu_glass.getItemMeta();
        meta.setDisplayName(ChatColor.BLACK+".");
        menu_glass.setItemMeta(meta);
        for (int i = 0; i < 27; i++) {
            if (i == 10 || i == 12) continue;
            if (i == 14) {
                ItemStack applyButton = new ItemStack(Material.GREEN_CONCRETE);
                ItemMeta buttonMeta = applyButton.getItemMeta();
                buttonMeta.setDisplayName(ChatColor.GREEN+"Apply Skin");
                applyButton.setItemMeta(buttonMeta);

                inv.setItem(i, applyButton);
                continue;
            }
            if (i == 16) {
                ItemStack result = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta resultMeta = result.getItemMeta();
                resultMeta.setDisplayName(ChatColor.RED+"No Result");
                result.setItemMeta(resultMeta);

                inv.setItem(i, result);
                continue;
            }

            inv.setItem(i, menu_glass);

        }
    }

}
