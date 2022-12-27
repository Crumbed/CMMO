package com.crumbed.customMobs;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.crumbed.customMobs.MobManager.MOBS;
import static com.crumbed.utils.Namespaces.*;

public class SpawnMob implements Listener {

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.getEntity() instanceof ArmorStand) return;
        Entity entity = event.getEntity();
        String entityName = entity.getType().name();

        //for debugging
        //Bukkit.getConsoleSender().sendMessage(entityName);

        if (MOBS.containsKey(entityName)) {
            CMMOEntity customEntity = MOBS.get(entityName);
            LivingEntity spawnedEntity = (LivingEntity) entity;
            PersistentDataContainer entityStats = spawnedEntity.getPersistentDataContainer();

            entityStats.set(idKey, PersistentDataType.STRING, "CMMO:"+customEntity.getId());
            entityStats.set(dmgKey, PersistentDataType.INTEGER, customEntity.getDmg());
            entityStats.set(strKey, PersistentDataType.INTEGER, customEntity.getStr());
            entityStats.set(defenseKey, PersistentDataType.INTEGER, customEntity.getDef());
            spawnedEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(customEntity.getHp());
            spawnedEntity.setHealth(spawnedEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            spawnedEntity.setCustomName(customEntity.getName());
            //spawnedEntity.setCustomNameVisible(true);
            //spawnedEntity.getEquipment().setItemInMainHand(null);
        }


        /*
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            PersistentDataContainer zombieStats = zombie.getPersistentDataContainer();

            zombieStats.set(dmgKey, PersistentDataType.INTEGER, 40);
            zombieStats.set(strKey, PersistentDataType.INTEGER, 0);
            zombieStats.set(defenseKey, PersistentDataType.INTEGER, 0);
            zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
            zombie.setHealth(100);
            zombie.setCustomName(ChatColor.GRAY+"Zombie [Lvl: 1]");
            zombie.setCustomNameVisible(true);

        } else if (event.getEntity() instanceof Warden) {
            Warden warden = (Warden) event.getEntity();
            PersistentDataContainer wardenStats = warden.getPersistentDataContainer();

            wardenStats.set(dmgKey, PersistentDataType.INTEGER, 200);
            wardenStats.set(strKey, PersistentDataType.INTEGER, 110);
            wardenStats.set(defenseKey, PersistentDataType.INTEGER, 50);
            warden.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50000);
            warden.setHealth(50000);
            warden.setCustomName(ChatColor.GOLD+"Warden [Lvl: 100]");
            warden.setCustomNameVisible(true);

        }
        */

    }

}
