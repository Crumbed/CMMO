package com.crumbed.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

import static com.crumbed.utils.Namespaces.*;

public class BlockPlace implements Listener {

    public static ArrayList<String> preventIds = new ArrayList<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack placedItem = event.getItemInHand();
        if (!placedItem.hasItemMeta()) return;
        ItemMeta meta = placedItem.getItemMeta();
        if (!meta.getPersistentDataContainer().has(idKey, PersistentDataType.STRING)) return;

        //event.getPlayer().sendMessage(meta.getPersistentDataContainer().get(idKey, PersistentDataType.STRING));

        if (preventIds.contains(meta.getPersistentDataContainer().get(idKey, PersistentDataType.STRING))) {
            event.setCancelled(true);
        }

    }

}
