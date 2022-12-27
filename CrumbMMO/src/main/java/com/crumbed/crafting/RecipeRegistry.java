package com.crumbed.crafting;


import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.recipes.CustomRecipe;
import me.wolfyscript.customcrafting.recipes.items.Result;
import me.wolfyscript.customcrafting.registry.RegistryRecipes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import me.wolfyscript.utilities.util.NamespacedKey;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.crumbed.CrumbMMO.itemReg;
import static com.crumbed.utils.Namespaces.*;

public class RecipeRegistry {

    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;
    public static HashMap<String, CMMORecipe> recipes = new HashMap<>();
    public static ArrayList<String> customRecipes = new ArrayList<>();

    public RecipeRegistry (JavaPlugin plugin) {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), "CustomCrafting.yml");
        if(!file.exists()) {try { file.createNewFile(); } catch (IOException e) {}}

        config = YamlConfiguration.loadConfiguration(file);
        loadRecipes();
    }

    public FileConfiguration getCraftConfig() {
        return config;
    }

    public void saveCraftConfig() {
        try {config.save(file);} catch (IOException e) {}
    }

    public void reloadCraftConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void loadRecipes() {
        ArrayList<String> ids = (ArrayList<String>) config.getStringList("registered-ids");

        for (String id : ids) {
            String configId = id+".";
            String group = config.getString(configId+"namespace");
            String type = config.getString(configId+"type");
            int resultAmount = config.getInt(configId+"result-amount");
            String resultId = config.getString(configId+"result-id");
            ArrayList<String> recMaterials = (ArrayList<String>) config.getStringList(configId+"recipe");
            ArrayList<Integer> matCounts = (ArrayList<Integer>) config.getIntegerList(configId+"amounts");

            ArrayList<ItemStack> mats = new ArrayList<>();

            for (int i = 0; i < recMaterials.size(); i++) {
                if (recMaterials.get(i).equals("none")) { mats.add(null); continue; }
                ItemStack item;
                if (itemReg.getItems().containsKey(recMaterials.get(i))) {
                    item = itemReg.getItems().get(recMaterials.get(i));
                    item.setAmount(matCounts.get(i));
                } else {
                    item = new ItemStack(Material.getMaterial(recMaterials.get(i)), matCounts.get(i));
                }

                mats.add(item);
            }
            ItemStack item;

            if (itemReg.getItems().containsKey(resultId)) {
                item = itemReg.getItems().get(resultId);
                item.setAmount(resultAmount);
            } else {
                item = new ItemStack(Material.getMaterial(resultId), resultAmount);
            }

            CMMORecipe recipe = new CMMORecipe(mats, item, type, group+"/"+id);
            recipes.put(group+"/"+id, recipe);
            customRecipes.add(group+"/"+id);

        }

        RegistryRecipes registryRecipes = CustomCrafting.inst().getRegistries().getRecipes();

        for (CustomRecipe<?> recipe : registryRecipes.get("custom")) {
            if (!customRecipes.contains(recipe.getNamespacedKey().getKey())) { registryRecipes.remove(recipe.getNamespacedKey()); continue; }
        }

        for (String key : customRecipes) {
            if (!registryRecipes.has(new NamespacedKey("customcrafting", key))) { createRecipe(key); continue; }

            CustomRecipe<?> recipe = registryRecipes.get(new NamespacedKey("customcrafting", key));
            ItemStack result = recipe.getResult().getItemStack();
            ItemStack itemRegItem = itemReg.getItems().get(result.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING));



        }
    }

    private void createRecipe(String key) {

        CMMORecipe recipe = recipes.get(key);

        String json = "\"@type\": \"customcrafting:crafting_"+recipe.getType()+"\", group: \"\", hidden: false, vanillaBook: false, priority: NORMAL, checkNBT: false,";
        if (key.substring(0, 5).equalsIgnoreCase("admin")) json += "conditions {values: [{key: \"customcrafting:permission\", option: EXACT, permission: \"craft.admin\"}]},";
        else json += "conditions {values: []},";

        String result = "result {items: [{\"custom_amount\": "+recipe.getResult().getAmount()+", item {"+recipe.getResult().toString()+"}}], tags: [], extensions: []}";

        if (recipe.getType().equalsIgnoreCase("shaped")) {
            json += "symmetry {horizontal: false, vertical: false, rotate: false}, keepShapeAsIs: false,";

            Ingredients ingredients = new Ingredients(recipe.getMaterials());
            json += ingredients.getShape();
            json += ingredients.toString();
        }
        json += "settings: {},";
        json += result;
        if (recipe.getType().equalsIgnoreCase("shapeless")) {
            json += ", ingredients: [";
            for (ItemStack item : recipe.getMaterials()) {
                json += "{items: [{custom_amount: "+item.getAmount()+", item {"+item+"}}], tags: [], replaceWithRemains: true, allowEmpty: false},";
            }
            json = json.substring(0, json.length()-1) + "]";
        }

        String folder = key.split("/")[0];
        String fileName = key.split("/")[1];

        File file = new File(CustomCrafting.inst().getDataFolder(), "data/"+folder+"/recipes/"+fileName+".conf");
        if(!file.exists()) {try { file.createNewFile(); } catch (IOException e) {}}
        try{
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {}

    }

}
