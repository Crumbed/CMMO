package com.crumbed.commands;

import com.crumbed.CrumbMMO;
import com.crumbed.stats.Stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


import static com.crumbed.CrumbMMO.statsManager;
import static com.crumbed.utils.Namespaces.*;

public class ResetStats {

    public static boolean reset(CommandSender sender, String[] args) {

        Player playerToReset = Bukkit.getPlayer(args[2]);

        PersistentDataContainer data = playerToReset.getPersistentDataContainer();

        Stats stats = new Stats(false);

        data.set(baseManaKey, PersistentDataType.INTEGER, stats.getBaseMana());
        data.set(maxManaKey, PersistentDataType.INTEGER, stats.getMaxMana());
        data.set(manaKey, PersistentDataType.INTEGER, stats.getMana());

        data.set(baseHealthKey, PersistentDataType.INTEGER, stats.getBaseHealth());
        playerToReset.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
        playerToReset.setHealth(100);
        playerToReset.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);

        data.set(baseDefenseKey, PersistentDataType.INTEGER, stats.getBaseDefense());
        data.set(defenseKey, PersistentDataType.INTEGER, stats.getDefense());

        data.set(dmgKey, PersistentDataType.INTEGER, stats.getDamage());
        data.set(strKey, PersistentDataType.INTEGER, stats.getStrength());
        data.set(critDmgKey, PersistentDataType.INTEGER, stats.getCritDamage());
        data.set(critChanceKey, PersistentDataType.FLOAT, stats.getCritChance());

        int baseMana = data.get(baseManaKey, PersistentDataType.INTEGER);
        int maxMana = data.get(maxManaKey, PersistentDataType.INTEGER);
        int mana = data.get(manaKey, PersistentDataType.INTEGER);
        int baseHealth = data.get(baseHealthKey, PersistentDataType.INTEGER);
        int baseDefense = data.get(baseDefenseKey, PersistentDataType.INTEGER);
        int defense = data.get(defenseKey, PersistentDataType.INTEGER);

        int dmg = data.get(dmgKey, PersistentDataType.INTEGER);
        int str = data.get(strKey, PersistentDataType.INTEGER);
        int cDmg = data.get(critDmgKey, PersistentDataType.INTEGER);
        float cChance = data.get(critChanceKey, PersistentDataType.FLOAT);

        statsManager.updateStats(playerToReset.getUniqueId(), new Stats(baseHealth, baseDefense, defense, baseMana, maxMana, mana, dmg, str, cDmg, cChance, 0.1f));

        sender.sendMessage(ChatColor.GREEN + "Successfully reset "+args[2]+"'s stats.");

        return true;
    }

}
