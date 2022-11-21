package com.crumbed.customItems;

import com.crumbed.CrumbMMO;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ItemRegistry {
    public static HashMap<String, ItemStack> items = new HashMap<>();

    public ItemRegistry(CrumbMMO plugin){}

    public HashMap<String, ItemStack> getItems() {
        return items;
    }
    public void setItems(HashMap<String, ItemStack> ITEMS ) {
        items = ITEMS;
    }


}
