package com.crumbed.stats;

import com.crumbed.CrumbMMO;
import com.crumbed.enchants.CustomEnchants;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static com.crumbed.CrumbMMO.abm;
import static com.crumbed.utils.Namespaces.*;

public class StatsManager {

    private CrumbMMO plugin;
    private HashMap<UUID, Stats> entityStats = new HashMap<>();
    public HashMap<UUID, Stats> playerAddonStats = new HashMap<>();
    public HashMap<Player, Double> playerHealthScales = new HashMap<>();
    public StatsManager (CrumbMMO plugin) {
        this.plugin = plugin;
        regen(plugin);
        checkEquip(plugin);
    }

    public void updateStats(UUID uuid, Stats stats) {

        stats.addStats(playerAddonStats.get(uuid), Bukkit.getPlayer(uuid));

        entityStats.put(uuid, stats);

        PersistentDataContainer data = Bukkit.getPlayer(uuid).getPersistentDataContainer();

        data.set(maxManaKey, PersistentDataType.INTEGER, stats.getMaxMana());
        data.set(manaKey, PersistentDataType.INTEGER, stats.getMana());

        data.set(defenseKey, PersistentDataType.INTEGER, stats.getDefense());

        data.set(dmgKey, PersistentDataType.INTEGER, stats.getDamage());
        data.set(strKey, PersistentDataType.INTEGER, stats.getStrength());
        data.set(critDmgKey, PersistentDataType.INTEGER, stats.getCritDamage());
        data.set(critChanceKey, PersistentDataType.FLOAT, stats.getCritChance());

        int maxMana = data.get(maxManaKey, PersistentDataType.INTEGER);
        int mana = data.get(manaKey, PersistentDataType.INTEGER);
        int defense = data.get(defenseKey, PersistentDataType.INTEGER);
        float atkSpeed = 4.0f - stats.getAtkSpeed();

        if (Bukkit.getPlayer(uuid).getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack item = Bukkit.getPlayer(uuid).getInventory().getItemInMainHand();
            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "yes", (4.0 - atkSpeed), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            item.getItemMeta().addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
        }

        Bukkit.getPlayer(uuid).getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);

        TextComponent actionBar = new TextComponent(
                "     "+ChatColor.GREEN+""+defense+"     "+ChatColor.AQUA+""+mana+"/"+maxMana
        );
        abm.setActionBar(uuid, actionBar);
    }

    public void useMana (UUID uuid, int manaCost, String name) {

        PersistentDataContainer data = Bukkit.getPlayer(uuid).getPersistentDataContainer();

        data.set(manaKey, PersistentDataType.INTEGER, entityStats.get(uuid).getMana());

        int maxMana = data.get(maxManaKey, PersistentDataType.INTEGER);
        int mana = data.get(manaKey, PersistentDataType.INTEGER);

        TextComponent actionBar = new TextComponent(
                "     "+ChatColor.AQUA+"-"+manaCost+" ("+ChatColor.GOLD+name+ChatColor.AQUA+")     "+ChatColor.AQUA+""+mana+"/"+maxMana
        );
        abm.setActionBar(uuid, actionBar);
    }

    public void regen (CrumbMMO plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : entityStats.keySet()){
                    Stats stats = entityStats.get(uuid);
                    Player player = Bukkit.getPlayer(uuid);
                    if((player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() && stats.getMana() == stats.getMaxMana()) || player.getHealth() <= 0) { continue; }
                    if(!(player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())) {
                        try {
                            player.setHealth(player.getHealth() + (int) (1.5 + (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.01) + 0.5));
                        } catch (IllegalArgumentException e) {
                            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                        }

                    }
                    if(!(stats.getMana() == stats.getMaxMana())) {
                        stats.setMana(stats.getMana() + (int) (stats.getMaxMana() * 0.02 + 0.5));
                        if(stats.getMana() > stats.getMaxMana()) { stats.setMana(stats.getMaxMana()); }
                    }
                    updateStats(uuid, stats);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void checkEquip (CrumbMMO plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    ItemStack mainHand = player.getInventory().getItemInMainHand();
                    ItemStack offHand = player.getInventory().getItemInOffHand();
                    ItemStack[] armor = player.getInventory().getArmorContents();

                    ArrayList<ItemStack> items = new ArrayList<>();
                    items.add(mainHand);
                    items.add(offHand);
                    items.addAll(Arrays.asList(armor));

                    Stats addStats = new Stats(true);

                    for (int i = 0; i < items.size(); i++) {
                        ItemStack item = items.get(i);
                        if (item == null) { continue; }
                        if (!item.hasItemMeta()) { continue; }
                        ItemMeta meta = item.getItemMeta();
                        PersistentDataContainer customMeta = meta.getPersistentDataContainer();

                        int dmg = 0;
                        int str = 0;
                        int cDmg = 0;
                        float cChance = 0.0f;
                        int hp = 0;
                        int def = 0;
                        int mana = 0;
                        float speed = 0.0f;
                        float atkSpeed = 0.0f;

                        if (customMeta.has(dmgKey, PersistentDataType.INTEGER))         dmg = customMeta.get(dmgKey, PersistentDataType.INTEGER);
                        if (customMeta.has(strKey, PersistentDataType.INTEGER))         str = customMeta.get(strKey, PersistentDataType.INTEGER);
                        if (customMeta.has(critDmgKey, PersistentDataType.INTEGER))     cDmg = customMeta.get(critDmgKey, PersistentDataType.INTEGER);
                        if (customMeta.has(critChanceKey, PersistentDataType.FLOAT))    cChance = customMeta.get(critChanceKey, PersistentDataType.FLOAT);
                        if (customMeta.has(healthKey, PersistentDataType.INTEGER))      hp = customMeta.get(healthKey, PersistentDataType.INTEGER);
                        if (customMeta.has(defenseKey, PersistentDataType.INTEGER))     def = customMeta.get(defenseKey, PersistentDataType.INTEGER);
                        if (customMeta.has(manaKey, PersistentDataType.INTEGER))        mana = customMeta.get(manaKey, PersistentDataType.INTEGER);
                        if (customMeta.has(speedKey, PersistentDataType.FLOAT))         speed = customMeta.get(speedKey, PersistentDataType.FLOAT);
                        if (customMeta.has(atkSpeedKey, PersistentDataType.FLOAT))      atkSpeed = customMeta.get(atkSpeedKey, PersistentDataType.FLOAT);

                        if (meta.hasEnchant(CustomEnchants.HEALTH))                     hp += (15 * meta.getEnchantLevel(CustomEnchants.HEALTH));
                        if (meta.hasEnchant(CustomEnchants.CPROTECTION))                def += (5 * meta.getEnchantLevel(CustomEnchants.CPROTECTION));
                        if (meta.hasEnchant(CustomEnchants.SPEED))                      speed += (0.025 * meta.getEnchantLevel(CustomEnchants.SPEED));
                        if (meta.hasEnchant(CustomEnchants.BIGBRAIN))                   mana += (5 * meta.getEnchantLevel(CustomEnchants.BIGBRAIN));
                        if (meta.hasEnchant(CustomEnchants.CSHARPNESS))                 dmg += (dmg * (0.05 * meta.getEnchantLevel(CustomEnchants.CSHARPNESS)));
                        if (meta.hasEnchant(CustomEnchants.CRITICAL))                   cDmg += (10 * meta.getEnchantLevel(CustomEnchants.CRITICAL));

                        if (dmg != 0)           addStats.setDamage(addStats.getDamage()+dmg);
                        if (str != 0)           addStats.setStrength(addStats.getStrength()+str);
                        if (cDmg != 0)          addStats.setCritDamage(addStats.getCritDamage()+cDmg);
                        if (cChance != 0)       addStats.setCritChance(addStats.getCritChance()+cChance);
                        if (hp != 0)            addStats.setHealth(addStats.getHealth()+hp);
                        if (def != 0)           addStats.setDefense(addStats.getDefense()+def);
                        if (mana != 0)          addStats.setMana(addStats.getMana()+mana);
                        if (speed != 0)         addStats.setSpeed(addStats.getSpeed()+speed);
                        if (atkSpeed != 0)      addStats.setAtkSpeed(addStats.getAtkSpeed()+atkSpeed);

                    }

                    int maxHealth = (int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                    double healthScale = 20;

                    if (maxHealth <= 100) {
                        healthScale = 20;
                    } else if (maxHealth > 1000) {
                        healthScale = 40;
                    } else {
                        healthScale = maxHealth / 50.0 + 20;
                    }
                    if (healthScale % 2 > 0 && !(maxHealth % 100 == 50)) {
                        healthScale = Math.round(healthScale / 2) * 2;
                    }


                    if (playerHealthScales.containsKey(player) && !playerHealthScales.get(player).equals(healthScale)) { player.setHealthScale(healthScale); playerHealthScales.put(player, healthScale); }
                    //player.sendMessage(""+healthScale+", "+playerHealthScales.get(player));

                    if (!playerAddonStats.get(player.getUniqueId()).equals(addStats)) {
                        updateStats(player.getUniqueId(), entityStats.get(player.getUniqueId()));
                        playerAddonStats.put(player.getUniqueId(), addStats);
                    }


                }
            }
        }.runTaskTimer(plugin, 0, 5);
    }

    public Stats getStats(UUID uuid) {
        return entityStats.get(uuid);
    }

    public void removeUuid(UUID uuid) {
        entityStats.remove(uuid);
        abm.removeActionBar(uuid);
    }

    // when player leave prevent stat dupe
    public void removeFuckedStats(UUID uuid) {
        Stats playerStats = entityStats.get(uuid);
        Stats fuckedStats = playerAddonStats.get(uuid);
        playerStats.removeAddStats(fuckedStats, Bukkit.getPlayer(uuid));
        updateStats(uuid, playerStats);
        playerAddonStats.remove(uuid);
    }

    public boolean hasUuid(UUID uuid) {
        return entityStats.containsKey(uuid);
    }

}
