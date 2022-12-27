package com.crumbed.crafting;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class Ingredients {

    private String [] characters = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
    private ArrayList<ItemStack> mats;
    private ArrayList<String> shape = new ArrayList<>();

    private String A = "";
    private String B = "";
    private String C = "";
    private String D = "";
    private String E = "";
    private String F = "";
    private String G = "";
    private String H = "";
    private String I = "";

    public Ingredients(ArrayList<ItemStack> mats) {
        this.mats = mats;
        int i = 0;
        String row = "";
        for (ItemStack item : mats) {
            if ((i==2 || i==5 || i==8) && row.length() > 0) { shape.add(row); row = ""; }
            if (item != null) row += characters[i];
            i++;

            if (item != null) continue;

            String ingJson = "{items: [{\"custom_amount\": "+item.getAmount()+", item {"+item+"}}], tags: [], replaceWithRemains: true, allowEmpty: false}";

            if (i==0)   A = "A "+ingJson;
            if (i==1)   B = "B "+ingJson+",";
            if (i==2)   C = "C "+ingJson+",";
            if (i==3)   D = "D "+ingJson+",";
            if (i==4)   E = "E "+ingJson+",";
            if (i==5)   F = "F "+ingJson+",";
            if (i==6)   G = "G "+ingJson+",";
            if (i==7)   H = "H "+ingJson+",";
            if (i==8)   I = "I "+ingJson+",";

        }
    }

    public String getShape() {
        return "shape: "+ Arrays.toString(Arrays.copyOf(shape.toArray(), shape.size(), String[].class))+",";
    }
    public String toString() {
        String json = I+H+G+F+E+D+C+B+A;
        if (json.substring(json.length()-1).equals(",")) json = json.substring(0, json.length()-1);

        return json;
    }

}
