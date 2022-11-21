package com.crumbed.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;

import static com.crumbed.utils.Namespaces.idKey;

public class TrailsGui {

    private static HashMap<Player, Inventory> playerMenus = new HashMap<>();
    private static HashMap<ItemStack, String> trails;
    private ArrayList<ItemStack> playerTrails;

    public void register() {
        trails = new HashMap<>();
        playerTrails = new ArrayList<>();

        ItemStack item = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        PersistentDataContainer customMeta = meta.getPersistentDataContainer();
        customMeta.set(idKey, PersistentDataType.STRING, "admin_trail");
        meta.setDisplayName(ChatColor.RED+"Admin Trail");
        ArrayList<String> itemLore = new ArrayList<>();
        itemLore.add("");
        itemLore.add(ChatColor.RED+""+ChatColor.BOLD+"SPECIAL TRAIL");
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        trails.put(item, "trails.admin");

        item = new ItemStack(Material.DIAMOND);
        meta = item.getItemMeta();
        assert meta != null;
        customMeta = meta.getPersistentDataContainer();
        customMeta.set(idKey, PersistentDataType.STRING, "divine_trail");
        meta.setDisplayName(ChatColor.AQUA+"Divine Trail");
        itemLore = new ArrayList<>();
        itemLore.add("");
        itemLore.add(ChatColor.GRAY+"Obtained with the purchase of the");
        itemLore.add(ChatColor.AQUA+"[DIVINE] "+ChatColor.GRAY+"rank.");
        itemLore.add("");
        itemLore.add(ChatColor.AQUA+""+ChatColor.BOLD+"DIVINE TRAIL");
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        trails.put(item, "trails.divine");

        item = new ItemStack(Material.EMERALD);
        meta = item.getItemMeta();
        assert meta != null;
        customMeta = meta.getPersistentDataContainer();
        customMeta.set(idKey, PersistentDataType.STRING, "legendary_trail");
        meta.setDisplayName(ChatColor.GOLD+"Legendary Trail");
        itemLore = new ArrayList<>();
        itemLore.add("");
        itemLore.add(ChatColor.GRAY+"Obtained with the purchase of the");
        itemLore.add(ChatColor.GOLD+"[LEGENDARY] "+ChatColor.GRAY+"rank.");
        itemLore.add("");
        itemLore.add(ChatColor.GOLD+""+ChatColor.BOLD+"LEGENDARY TRAIL");
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        trails.put(item, "trails.legendary");

    }

    public Inventory getInv(Player player) {
        return playerMenus.get(player);
    }

    public void openInv(Player player) {
        Inventory inv = Bukkit.createInventory(player, 9, ChatColor.BLACK+""+ChatColor.BOLD+"Particle Trails");
        for (int i = 0; i < playerTrails.size(); i++) {
            ItemStack item = playerTrails.get(i);
            inv.setItem(i, item);
        }
        playerMenus.put(player, inv);

        player.openInventory(inv);
    }

    public HashMap<ItemStack, String> getTrails() {
        return trails;
    }
    public void setPlayerTrails (ArrayList<ItemStack> pt) {
        playerTrails = pt;
    }


}
