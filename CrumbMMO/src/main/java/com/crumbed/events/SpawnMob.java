package com.crumbed.events;

import com.crumbed.customMobs.CMMOZombie;
import net.minecraft.world.level.Level;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.crumbed.utils.Namespaces.*;

public class SpawnMob implements Listener {

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.getEntity() instanceof ArmorStand) return;
        if (event.getEntity().getPersistentDataContainer().has(idKey, PersistentDataType.STRING)) {
            Entity entity = event.getEntity();
            if (event.getEntity().getPersistentDataContainer().get(idKey, PersistentDataType.STRING).equals("cmmo_zombie")) {
                Zombie zombie = (Zombie) entity;
                PersistentDataContainer zombieStats = zombie.getPersistentDataContainer();

                zombieStats.set(dmgKey, PersistentDataType.INTEGER, 40);
                zombieStats.set(strKey, PersistentDataType.INTEGER, 0);
                zombieStats.set(defenseKey, PersistentDataType.INTEGER, 0);
                zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
                zombie.setHealth(100);
                return;
            }


        }



        if (event.getEntity() instanceof Zombie) {
            event.setCancelled(true);
            CMMOZombie zombie = new CMMOZombie(event.getLocation());

            Zombie customZombie = (Zombie) zombie.getBukkitEntity();
            customZombie.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, "cmmo_zombie");
            zombie = (CMMOZombie) ((CraftEntity) customZombie).getHandle();
            Level world = ((CraftWorld) event.getLocation().getWorld()).getHandle();

            world.addFreshEntity(zombie);
            return;
        }

    }

}
