package com.crumbed.customItems;

import com.crumbed.CrumbMMO;
import com.crumbed.abilities.Ability;
import com.crumbed.abilities.AOTE;
import com.crumbed.abilities.EnderRage;
import com.crumbed.events.BlockPlace;
import com.google.common.collect.ImmutableMultimap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.text.html.CSS;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.crumbed.CrumbMMO.itemReg;
import static com.crumbed.CrumbMMO.headApi;
import static com.crumbed.utils.Namespaces.*;

public class ItemManager {

    private CrumbMMO plugin;
    private Object[] abilities;
    private File file;
    private FileConfiguration config;

    public ItemManager(CrumbMMO plugin) {
        this.plugin = plugin;

        abilities = new Object[]
                {new AOTE(), new EnderRage()};

        file = new File(plugin.getDataFolder(), "CustomItems.yml");
        if(!file.exists()) {try { file.createNewFile(); } catch (IOException e) {}}

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void checkForOutdatedItems(Player player) {
        HashMap<String, ItemStack> items = itemReg.getItems();
        Inventory playerInv = player.getInventory();
        for (int i = 0; i < playerInv.getSize(); i++) {
            if (playerInv.getItem(i) == null) continue;
            if (playerInv.getItem(i).getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING) != null) {
                ItemStack item = playerInv.getItem(i);
                ItemMeta meta = item.getItemMeta();
                Map<Enchantment, Integer> enchants = meta.getEnchants();
                PersistentDataContainer customMeta = meta.getPersistentDataContainer();
                String id = customMeta.get(idKey, PersistentDataType.STRING);
                if (!items.containsKey(id)) {
                    player.getInventory().removeItem(item);
                    player.sendMessage(ChatColor.RED + "One of the items in your inventory was removed from the game,\nplease contact staff if you think this is an error.");
                }
                ItemStack CMMOItem = items.get(id);

                if (!(item.getItemMeta().equals(CMMOItem.getItemMeta()))) {
                    item.setItemMeta(CMMOItem.getItemMeta());
                    if (!enchants.isEmpty()) {
                        item.addUnsafeEnchantments(enchants);
                    }

                }

            }
        }
    }

    public void saveCustomItems(CrumbMMO plugin) {

        HashMap<String, ItemStack> items = itemReg.getItems();
        ArrayList<String> ids = new ArrayList<>(config.getStringList("registered-ids"));

        config.set("registered-ids", ids);

        for (String id : ids) {
            ItemStack item = items.get(id);
            String hexCode = "-";
            if (    item.getType().equals(Material.LEATHER_HELMET) ||
                    item.getType().equals(Material.LEATHER_CHESTPLATE) ||
                    item.getType().equals(Material.LEATHER_LEGGINGS) ||
                    item.getType().equals(Material.LEATHER_BOOTS) ) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) item.getItemMeta();
                int r = leatherArmorMeta.getColor().getRed();
                int g = leatherArmorMeta.getColor().getGreen();
                int b = leatherArmorMeta.getColor().getBlue();
                hexCode = String.format("#%02X%02X%02X", r, g, b);
            }
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer customMeta = meta.getPersistentDataContainer();

            String configId = id+".";

            int dmg = 0;
            int str = 0;
            int cDmg = 0;
            float cChance = 0.0f;
            int hp = 0;
            int def = 0;
            int mana = 0;
            float atkSpeed = 0.0f;
            float speed = 0.0f;
            String headId = "-";

            String name = customMeta.get(nameKey, PersistentDataType.STRING);
            String ability = "-";
            int manaCost = 0;
            int rarity = customMeta.get(rarityKey, PersistentDataType.INTEGER);
            String type = customMeta.get(typeKey, PersistentDataType.STRING);
            String material = customMeta.get(materialKey, PersistentDataType.STRING);
            int placeable = 0;
            ArrayList<String> lore = null;

            if (customMeta.has(skullId, PersistentDataType.STRING))         headId = customMeta.get(skullId, PersistentDataType.STRING);
            if (customMeta.has(preventPlace, PersistentDataType.INTEGER))   placeable = customMeta.get(preventPlace, PersistentDataType.INTEGER);

            if (customMeta.has(dmgKey, PersistentDataType.INTEGER))         dmg = customMeta.get(dmgKey, PersistentDataType.INTEGER);
            if (customMeta.has(strKey, PersistentDataType.INTEGER))         str = customMeta.get(strKey, PersistentDataType.INTEGER);
            if (customMeta.has(critDmgKey, PersistentDataType.INTEGER))     cDmg = customMeta.get(critDmgKey, PersistentDataType.INTEGER);
            if (customMeta.has(critChanceKey, PersistentDataType.FLOAT))    cChance = customMeta.get(critChanceKey, PersistentDataType.FLOAT)*100;
            if (customMeta.has(healthKey, PersistentDataType.INTEGER))      hp = customMeta.get(healthKey, PersistentDataType.INTEGER);
            if (customMeta.has(defenseKey, PersistentDataType.INTEGER))     def = customMeta.get(defenseKey, PersistentDataType.INTEGER);
            if (customMeta.has(manaKey, PersistentDataType.INTEGER))        mana = customMeta.get(manaKey, PersistentDataType.INTEGER);
            if (customMeta.has(atkSpeedKey, PersistentDataType.FLOAT))      atkSpeed = customMeta.get(atkSpeedKey, PersistentDataType.FLOAT);
            if (customMeta.has(speedKey, PersistentDataType.FLOAT))         speed = customMeta.get(speedKey, PersistentDataType.FLOAT);

            if (customMeta.has(abilityKey, PersistentDataType.STRING))      ability = customMeta.get(abilityKey, PersistentDataType.STRING);
            if (customMeta.has(manaCostKey, PersistentDataType.INTEGER))    manaCost = customMeta.get(manaCostKey, PersistentDataType.INTEGER);

            if (customMeta.has(loreKey, PersistentDataType.STRING)) {
                 lore = new ArrayList<>(Arrays.asList(customMeta.get(loreKey, PersistentDataType.STRING).split("\n")));
            }

            config.set                  (configId+"name", name);

            if(dmg != 0)                config.set(configId+"damage", dmg);
            if(str != 0)                config.set(configId+"strength", str);
            if(cDmg != 0)               config.set(configId+"crit-damage", cDmg);
            if(cChance != 0.0f)         config.set(configId+"crit-chance", cChance);
            if(hp != 0)                 config.set(configId+"health", hp);
            if(def != 0)                config.set(configId+"defense", def);
            if(mana != 0)               config.set(configId+"mana", mana);
            if(atkSpeed != 0)           config.set(configId+"atk-speed", atkSpeed);
            if(speed != 0)              config.set(configId+"speed", speed);

            if(lore != null)            config.set(configId+"lore", lore);
            if(!ability.equals("-"))    config.set(configId+"ability.name", ability);
            if(manaCost != 0)           config.set(configId+"ability.mana-cost", manaCost);

            config.set                  (configId+"rarity", rarity);
            config.set                  (configId+"type", type);
            config.set                  (configId+"material", material);

            if(!hexCode.equals("-"))    config.set(configId+"color", hexCode);
            if(!headId.equals("-"))     config.set(configId+"skull-id", headId);
            if(placeable == 1)          config.set(configId+"placeable", placeable);

        }
        try {config.save(file);}catch(IOException e){}
    }

    public void loadCustomItems(JavaPlugin plugin) {
        HashMap<String, ItemStack> items = itemReg.getItems();

        ChatColor[] rarityColors = new ChatColor[] {
                ChatColor.RED,
                ChatColor.WHITE,
                ChatColor.GREEN,
                ChatColor.BLUE,
                ChatColor.DARK_PURPLE,
                ChatColor.GOLD
        };
        String[] rarities = new String[] {
                "SPECIAL",
                "COMMON",
                "UNCOMMON",
                "RARE",
                "EPIC",
                "LEGENDARY"
        };

        ArrayList<String> ids = (ArrayList<String>) config.getStringList("registered-ids");

        for (String id : ids) {
            String configId = id+".";
            String name = config.getString(configId+"name");

            int dmg = 0;
            int str = 0;
            int cDmg = 0;
            float cChance = 0.0f;
            int hp = 0;
            int def = 0;
            int mana = 0;
            float atkSpeed = 0.0f;
            float speed = 0.0f;

            String ability = "-";
            int mana_cost = 0;
            int rarity = config.getInt(configId+"rarity");
            String type = config.getString(configId+"type");
            String material = config.getString(configId+"material");
            String headId = "-";
            String hexCode = "-";
            int placeable = 0;
            ArrayList<String> lore = new ArrayList<>();
            ArrayList<String> enchants = new ArrayList<>();

            if(config.contains(configId+"color"))               hexCode = config.getString(configId+"color");
            if(config.contains(configId+"skull-id"))            headId = config.getString(configId+"skull-id");
            if(config.contains(configId+"placeable"))           placeable = config.getInt(configId+"placeable");

            if(config.contains(configId+"damage"))              dmg = config.getInt(configId+"damage");
            if(config.contains(configId+"strength"))            str = config.getInt(configId+"strength");
            if(config.contains(configId+"crit-damage"))         cDmg = config.getInt(configId+"crit-damage");
            if(config.contains(configId+"crit-chance"))         cChance = (float) config.getDouble(configId+"crit-chance");
            if(config.contains(configId+"health"))              hp = config.getInt(configId+"health");
            if(config.contains(configId+"defense"))             def = config.getInt(configId+"defense");
            if(config.contains(configId+"mana"))                mana = config.getInt(configId+"mana");
            if(config.contains(configId+"atk-speed"))           atkSpeed = (float) config.getDouble(configId+"atk-speed");
            if(config.contains(configId+"speed"))               speed = (float) config.getDouble(configId+"speed");

            if(config.contains(configId+"ability.name"))        ability = config.getString(configId+"ability.name");
            if(config.contains(configId+"ability.mana-cost"))   mana_cost = config.getInt(configId+"ability.mana-cost");
            if(config.contains(configId+"lore"))                lore = (ArrayList<String>) config.getStringList(configId+"lore");
            if(config.contains(configId+"enchants"))            enchants = (ArrayList<String>) config.getStringList(configId+"enchants");

            ItemStack item;
            if (material.equals("PLAYER_HEAD")) item = headApi.getItemHead(headId);
            else item = new ItemStack(Material.matchMaterial(material), 1);
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer customMeta = meta.getPersistentDataContainer();

            if(atkSpeed == 0) {
                if (type.equals("sword")) atkSpeed = 1.6f;
                else if (type.equals("axe")) atkSpeed = 1.8f;
                else if (type.equals("short_sword")) atkSpeed = 1.2f;
                else if (type.equals("long_sword")) atkSpeed = 1.8f;
            }

            ArrayList<String> itemLore = new ArrayList<>();
            String strLore = "-";

            ChatColor color = rarityColors[rarity];
            meta.setDisplayName(color+name);
            if (dmg != 0)       { itemLore.add(ChatColor.GRAY+"Damage: "+ChatColor.RED+"+"+dmg);
                                  customMeta.set(dmgKey, PersistentDataType.INTEGER, dmg); }
            if (str != 0)       { itemLore.add(ChatColor.GRAY+"Strength: "+ChatColor.RED+"+"+str);
                                  customMeta.set(strKey, PersistentDataType.INTEGER, str); }
            if (cDmg != 0)      { itemLore.add(ChatColor.GRAY+"Crit Damage: "+ChatColor.RED+"+"+cDmg+"%");
                                  customMeta.set(critDmgKey, PersistentDataType.INTEGER, cDmg); }
            if (cChance != 0.0) { itemLore.add(ChatColor.GRAY+"Crit Chance: "+ChatColor.RED+"+"+(int)cChance+"%");
                                  customMeta.set(critChanceKey, PersistentDataType.FLOAT, (cChance/100)); }
            if ((hp != 0 || def != 0 || mana != 0 || atkSpeed != 0) && (dmg != 0 || str != 0 || cDmg != 0 || cChance != 0.0))
            {itemLore.add("");}
            if (hp != 0)        { itemLore.add(ChatColor.GRAY+"Health: "+ChatColor.GREEN+"+"+hp);
                                  customMeta.set(healthKey, PersistentDataType.INTEGER, hp); }
            if (def != 0)       { itemLore.add(ChatColor.GRAY+"Defense: "+ChatColor.GREEN+"+"+def);
                                  customMeta.set(defenseKey, PersistentDataType.INTEGER, def); }
            if (mana != 0)      { itemLore.add(ChatColor.GRAY+"Mana: "+ChatColor.GREEN+"+"+mana);
                                  customMeta.set(manaKey, PersistentDataType.INTEGER, mana); }
            if (atkSpeed != 0)  { itemLore.add(ChatColor.GRAY+"Attack Speed: "+ChatColor.GREEN+"+"+atkSpeed);
                                  customMeta.set(atkSpeedKey, PersistentDataType.FLOAT, atkSpeed); }
            if (speed != 0)     { itemLore.add(ChatColor.GRAY+"Speed: "+ChatColor.GREEN+"+"+speed);
                                  customMeta.set(speedKey, PersistentDataType.FLOAT, speed); }
            itemLore.add("");
            if (!lore.isEmpty()){
                itemLore.addAll(lore);
                itemLore.add("");
                strLore = String.join("\n", lore);
            }
            if (!ability.equals("-")) {
                for (Object abilityObj : abilities){
                    if (abilityObj instanceof Ability && ((Ability) abilityObj).name.equals(ability)) {
                        itemLore.addAll(((Ability) abilityObj).description);
                        itemLore.add("");
                    }
                }
            }

            itemLore.add(color+""+ChatColor.BOLD+rarities[rarity]+" "+type.toUpperCase());
            meta.setLore(itemLore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

            for (String ench : enchants) {
                String enchant = ench.substring(0, ench.indexOf(":"));
                int lvl = Integer.parseInt(ench.substring(ench.indexOf(":")+1));
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchant)), lvl, true);
            }

            customMeta.set              (idKey, PersistentDataType.STRING, id);
            customMeta.set              (nameKey, PersistentDataType.STRING, name);
            customMeta.set              (typeKey, PersistentDataType.STRING, type);
            customMeta.set              (rarityKey, PersistentDataType.INTEGER, rarity);
            customMeta.set              (materialKey, PersistentDataType.STRING, material);

            if(!strLore.equals("-"))    customMeta.set(loreKey, PersistentDataType.STRING, strLore);
            if(!ability.equals("-"))    customMeta.set(abilityKey, PersistentDataType.STRING, ability);
            if(mana_cost != 0)          customMeta.set(manaCostKey, PersistentDataType.INTEGER, mana_cost);
            if(!headId.equals("-"))     customMeta.set(skullId, PersistentDataType.STRING, headId);
            if(placeable == 1) {
                customMeta.set(preventPlace, PersistentDataType.INTEGER, placeable);
                BlockPlace.preventIds.add(id);
            }

            meta.setAttributeModifiers(ImmutableMultimap.of());

            if(!hexCode.equals("-")) {
                LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
                int r = java.awt.Color.decode(hexCode).getRed();
                int g = java.awt.Color.decode(hexCode).getGreen();
                int b = java.awt.Color.decode(hexCode).getBlue();
                leatherMeta.setColor(org.bukkit.Color.fromRGB(r, g, b));
                item.setItemMeta(leatherMeta);
            } else {
                item.setItemMeta(meta);
            }

            items.put(id, item);

        }

    }

    public void reloadConfig(JavaPlugin plugin) {
        config = YamlConfiguration.loadConfiguration(file);
        itemReg.setItems(new HashMap<>());
        loadCustomItems(plugin);
    }

    public FileConfiguration getItemConfig() {
        return config;
    }

    public void saveItemConfig() {
        try {config.save(file);} catch (IOException e) {}
    }

}
