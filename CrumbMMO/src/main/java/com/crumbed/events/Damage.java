package com.crumbed.events;

import com.crumbed.CrumbMMO;
import com.crumbed.enchants.CustomEnchants;
import com.crumbed.stats.Stats;
import com.crumbed.utils.PacketManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.crumbed.CrumbMMO.statsManager;
import static com.crumbed.utils.Namespaces.*;


public class Damage implements Listener {

    private CrumbMMO plugin;
    private final PacketManager packetManager;
    private final ChatColor[] rarities = new ChatColor[] {
            ChatColor.WHITE,
            ChatColor.GREEN,
            ChatColor.BLUE,
            ChatColor.DARK_PURPLE,
            ChatColor.GOLD
    };
    public Damage (CrumbMMO plugin, @NotNull final PacketManager packetManager) {
        this.plugin = plugin;
        this.packetManager = packetManager;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.getEntity() instanceof ArmorStand) return;
        if (!(event.getDamager() instanceof Player)) {
            Entity damager = event.getDamager();
            if (!damager.getPersistentDataContainer().has(idKey, PersistentDataType.STRING)) return;
            PersistentDataContainer damagerStats = damager.getPersistentDataContainer();

            float dmg = (float) damagerStats.get(dmgKey, PersistentDataType.INTEGER);
            float str = (float) damagerStats.get(strKey, PersistentDataType.INTEGER);

            float finalDmg = (5+dmg) * (1+(str/100));

            float dmgReduc = 0f;
            if (event.getEntity().getPersistentDataContainer().has(defenseKey, PersistentDataType.INTEGER)) {
                float def = (float) event.getEntity().getPersistentDataContainer().get(defenseKey, PersistentDataType.INTEGER);
                dmgReduc = def/(def+100);
            }

            finalDmg -= finalDmg * dmgReduc;
            finalDmg = (float) ((int) (finalDmg + 0.5));

            event.setDamage(finalDmg);
            return;
        }
        final LivingEntity entity = (LivingEntity) event.getEntity();
        final Player player = (Player) event.getDamager();
        Stats playerStats = statsManager.getStats(player.getUniqueId());

        final ThreadLocalRandom random = ThreadLocalRandom.current();

        float dmg = (float) playerStats.getDamage();
        float str = (float) playerStats.getStrength();
        float cDmg = (float) playerStats.getCritDamage();
        double cChance = playerStats.getCritChance();

        float initDmg = (5+dmg) * (1+(str/100));
        float dmgMult = 1;
        float armorBonus = 1;
        float finalDmg = 0;

        boolean crit = false;
        ChatColor color = rarities[0];

        float cooldown = player.getAttackCooldown();

        if (Math.random() <= cChance && cooldown > 0.8) {
            finalDmg = initDmg * dmgMult * armorBonus * (1+(cDmg/100));
            crit = true;
        } else {
            finalDmg = initDmg * dmgMult * armorBonus;
        }

        finalDmg -= finalDmg * (1-cooldown);
        float dmgReduc = 0f;
        if (entity.getPersistentDataContainer().has(defenseKey, PersistentDataType.INTEGER)) {
            float def = (float) entity.getPersistentDataContainer().get(defenseKey, PersistentDataType.INTEGER);
            dmgReduc = def/(def+100);
        }

        finalDmg -= finalDmg * dmgReduc;
        finalDmg = (float) ((int) (finalDmg + 0.5));
        if (player.getInventory().getItemInMainHand().hasItemMeta()) {
            if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.LIFE_STEAL)) {
                int healthToAdd = (int) ((int) finalDmg * (0.02 * player.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(CustomEnchants.LIFE_STEAL)));
                if ((player.getHealth() + healthToAdd) > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                else player.setHealth(player.getHealth() + healthToAdd);
            }
            if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.FIRST_STRIKE)) {
                if (entity.getHealth() == entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())
                    finalDmg += finalDmg * (0.5 * player.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(CustomEnchants.LIFE_STEAL));
            }
        }

        if (finalDmg >= 10000) color = rarities[4];
        else if (finalDmg >= 5000) color = rarities[3];
        else if (finalDmg >= 1000) color = rarities[2];
        else if (finalDmg >= 500) color = rarities[1];

        String dmgDisp;

        if (crit) dmgDisp = ChatColor.GOLD+""+ChatColor.MAGIC+"- "+color+finalDmg+ChatColor.GOLD+""+ChatColor.MAGIC+" -";
        else dmgDisp = color+""+finalDmg;


        Location spawnLocation;
        int attempts = 0;
        do {
            attempts++;
            spawnLocation = entity.getLocation().add(random.nextDouble(0, 2) - 1d, 1, random.nextDouble(0, 2) - 1d);
            if (attempts > 20) {
                spawnLocation = entity.getLocation();
                break;
            }
        } while (!spawnLocation.getBlock().isPassable());

        List<Player> packetRecipients = new ArrayList<>();
        packetRecipients.add(player);

        for (Entity nearbyEntity : player.getNearbyEntities(16, 16, 16)) {
            if (nearbyEntity instanceof Player) packetRecipients.add((Player) nearbyEntity);
        }

        final Location finalSpawnLocation = spawnLocation;

        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin,
                () -> summonAndQueueIndicatorDeletion(
                        finalSpawnLocation,
                        dmgDisp,
                        packetRecipients
                )
        );

        event.setDamage(finalDmg);

    }


    private void summonAndQueueIndicatorDeletion(@NotNull Location location,
                                                 String dispDmg,
                                                 @NotNull List<@NotNull Player> packetRecipients) {
        //Create the entity
        final Object indicatorEntity = packetManager.buildEntityArmorStand(location, dispDmg);

        //Create the packets
        final Object entitySpawnPacket = packetManager.buildEntitySpawnPacket(indicatorEntity);
        final Object entityMetadataPacket = packetManager.buildEntityMetadataPacket(indicatorEntity, true);

        //Send the packets
        for (Player recipient : packetRecipients) {
            packetManager.sendPacket(entitySpawnPacket, recipient);
            packetManager.sendPacket(entityMetadataPacket, recipient);
        }

        this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, () -> {
            //Create the destroy packet
            final Object entityDestroyPacket = packetManager.buildEntityDestroyPacket(indicatorEntity);

            //Send the destroy packet
            for (final Player recipient : packetRecipients) packetManager.sendPacket(entityDestroyPacket, recipient);
        }, 40);
    }


}
