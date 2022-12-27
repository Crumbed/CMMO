package com.crumbed.crafting;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.crumbed.CrumbMMO.itemReg;

public class CMMORecipe {

    private ArrayList<ItemStack> materials;
    private ItemStack result;
    private String type;
    private String key;

    public CMMORecipe(ArrayList<ItemStack> mats, ItemStack result, String type, String key) {
        this.key = key;
        this.materials = mats;
        this.result = result;
        this.type = type;
        if (result.equals(itemReg.getItems().get("leviathan_axe"))) {
            Bukkit.getServer().getConsoleSender().sendMessage("Created CMMORecipe for "+result.getItemMeta().getDisplayName());
            Bukkit.getServer().getConsoleSender().sendMessage(String.valueOf(mats));
        }
    }

    public String getType() {
        return type;
    }
    public ItemStack getResult() {
        return result;
    }
    public ArrayList<ItemStack> getMaterials() {
        return materials;
    }
    public String getKey() { return key; }

}
