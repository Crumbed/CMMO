package com.crumbed.events;

import com.crumbed.stats.Stats;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.crumbed.CrumbMMO.itemManager;
import static com.crumbed.CrumbMMO.statsManager;
import static com.crumbed.utils.Namespaces.*;

public class PlayerJoin implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if (!player.hasPlayedBefore()) {

            Stats stats = new Stats(false);

            data.set(baseManaKey, PersistentDataType.INTEGER, stats.getBaseMana());
            data.set(maxManaKey, PersistentDataType.INTEGER, stats.getMaxMana());
            data.set(manaKey, PersistentDataType.INTEGER, stats.getMana());

            data.set(baseHealthKey, PersistentDataType.INTEGER, stats.getBaseHealth());
            player.setHealthScale(20);
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);

            data.set(baseDefenseKey, PersistentDataType.INTEGER, stats.getBaseDefense());
            data.set(defenseKey, PersistentDataType.INTEGER, stats.getDefense());

            data.set(dmgKey, PersistentDataType.INTEGER, stats.getDamage());
            data.set(strKey, PersistentDataType.INTEGER, stats.getStrength());
            data.set(critDmgKey, PersistentDataType.INTEGER, stats.getCritDamage());
            data.set(critChanceKey, PersistentDataType.FLOAT, stats.getCritChance());
        }

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
        float speed = (float) player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        float atkSpeed = (float) player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue();

        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(-1000);
        player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(-1000);
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(-1000);

        player.setHealthScale(10);

        statsManager.playerAddonStats.put(player.getUniqueId(), new Stats(true));
        statsManager.playerHealthScales.put(player, player.getHealthScale());

        statsManager.updateStats(player.getUniqueId(), new Stats(baseHealth, baseDefense, defense, baseMana, maxMana, mana, dmg, str, cDmg, cChance, speed, atkSpeed));

        itemManager.checkForOutdatedItems(player);

    }

}
