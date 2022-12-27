package com.crumbed.guis;

import com.crumbed.utils.PacketManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;

import static com.crumbed.CrumbMMO.headApi;
import static com.crumbed.utils.Namespaces.*;

public class SkinEvent implements Listener {

    private JavaPlugin plugin;

    private HashMap<String, String> skins = new HashMap<>();
    private HashMap<String, String[]> skinTypes = new HashMap<>();


    public SkinEvent (JavaPlugin plugin) {
        this.plugin = plugin;

        skins.put("diamond_wither_skin", "46868");
        skinTypes.put("diamond_wither_skin", new String[] {"wither_helmet"});

    }

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (!event.getInventory().equals(SkinsGui.INV)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getType() == InventoryType.PLAYER) return;
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;
        if (clickedItem.getItemMeta().getDisplayName().equals(ChatColor.BLACK+".")) {
            event.setCancelled(true);
            return;
        } else if (clickedItem.getItemMeta().getDisplayName().equals(ChatColor.RED+"No Result")) {
            event.setCancelled(true);
            return;
        } else if (clickedItem.getItemMeta().getDisplayName().equals(ChatColor.GREEN+"Apply Skin")) {
            event.setCancelled(true);

            ItemStack mainItem = event.getInventory().getItem(10);
            ItemStack skinToApply = event.getInventory().getItem(12);
            if (mainItem == null || skinToApply == null) { player.sendMessage(ChatColor.RED+"Nothing to apply"); return; }
            if (!skinToApply.getItemMeta().getPersistentDataContainer().has(idKey, PersistentDataType.STRING) || !mainItem.getItemMeta().getPersistentDataContainer().has(idKey, PersistentDataType.STRING)) { player.sendMessage(ChatColor.RED+"Cannot apply skin to item"); return; }
            if (!skins.containsKey(skinToApply.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING))) { player.sendMessage(ChatColor.RED+"Not a skin"); return; }

            String skinId = skinToApply.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
            String itemId = mainItem.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
            String[] skinItems = skinTypes.get(skinId);

            if (!Arrays.asList(skinItems).contains(itemId)) { player.sendMessage(ChatColor.RED+"Cannot apply skin to item"); return; }
            ItemStack skinnedItem = mainItem;

            if (skins.get(skinId).charAt(0) == '#') {

            } else {
                skinnedItem = headApi.getItemHead(skins.get(skinId));
                skinnedItem.setItemMeta(mainItem.getItemMeta());
                skinnedItem.getItemMeta().getPersistentDataContainer().set(hasSkin, PersistentDataType.INTEGER, 1);
            }

            event.getInventory().setItem(16, skinnedItem);
            event.getInventory().setItem(10, null);
            event.getInventory().setItem(12, null);

        }

    }

}
