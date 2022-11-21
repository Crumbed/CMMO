package com.crumbed.utils;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;


public class Effects {

    private int taskID;
    private final Player player;
    private final JavaPlugin plugin;
    private final PacketManager packetManager;

    public Effects(Player player, JavaPlugin plugin, PacketManager packetManager) {
        this.player = player;
        this.plugin = plugin;
        this.packetManager = packetManager;
    }

    public void startOwnerTrail() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            double var = 0;
            Location loc;
            Vector first, second;
            Vector pLoc, o1, o2;
            ParticleData particle = new ParticleData(player.getUniqueId());

            @Override
            public void run() {

                if (!particle.hasID()) particle.setID(taskID);

                var += Math.PI / 16;
                loc = player.getLocation();
                pLoc = loc.clone().toVector();

                first = loc.clone().add(Math.cos(var)/1.5, 0.1, Math.sin(var)/1.5).toVector();
                second = loc.clone().add(Math.cos(var + Math.PI)/1.5, 0.1, Math.sin(var + Math.PI)/1.5).toVector();

                o1 = first.clone();
                o2 = second.clone();
                o1 = o1.clone().subtract(pLoc);
                o2 = o2.clone().subtract(pLoc);
                //first = loc.clone().add(Math.cos(var), (Math.sin(var)/2)+0.5, Math.sin(var));
                //                second = loc.clone().add(Math.cos(var + Math.PI), (Math.sin(var)/2)+0.5, Math.sin(var + Math.PI));

                List<Player> packetRecipients = new ArrayList<>();
                packetRecipients.add(player);

                for (Entity nearbyEntity : player.getNearbyEntities(16, 16, 16)) {
                    if (nearbyEntity instanceof Player) packetRecipients.add((Player) nearbyEntity);
                }

                for (Player player : packetRecipients) {
                    packetManager.sendPacket(new ClientboundLevelParticlesPacket(
                            ParticleTypes.SOUL_FIRE_FLAME, true, first.getX(), first.getY(), first.getZ(), (float)o1.getX(), 0.8f, (float)o1.getZ(), 0.05F, 0
                    ), player);
                    packetManager.sendPacket(new ClientboundLevelParticlesPacket(
                            ParticleTypes.SOUL_FIRE_FLAME, true, second.getX(), second.getY(), second.getZ(), (float)o2.getX(), 0.8f, (float)o2.getZ(), 0.05F, 0
                    ), player);
                }


            }
        }, 0, 1);
    }

    public void startCrimsonIsleTrail() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            double var = 0;
            Location loc, first, second;
            ParticleData particle = new ParticleData(player.getUniqueId());

            @Override
            public void run() {

                if (!particle.hasID()) particle.setID(taskID);

                var += Math.PI / 16;
                loc = player.getLocation();

                first = loc.clone().add(Math.cos(var)/1.5, 0.1, Math.sin(var)/1.5);
                second = loc.clone().add(Math.cos(var + Math.PI)/1.5, 0.1, Math.sin(var + Math.PI)/1.5);

                List<Player> packetRecipients = new ArrayList<>();
                packetRecipients.add(player);

                for (Entity nearbyEntity : player.getNearbyEntities(16, 16, 16)) {
                    if (nearbyEntity instanceof Player) packetRecipients.add((Player) nearbyEntity);
                }

                for (Player player : packetRecipients) {
                    packetManager.sendPacket(new ClientboundLevelParticlesPacket(
                            ParticleTypes.SOUL_FIRE_FLAME, true, first.getX(), first.getY(), first.getZ(), 0f, 2f, 0f, 0.08F, 0
                    ), player);
                    packetManager.sendPacket(new ClientboundLevelParticlesPacket(
                            ParticleTypes.SOUL_FIRE_FLAME, true, second.getX(), second.getY(), second.getZ(), 0f, 2f, 0f, 0.08F, 0
                    ), player);
                }


            }
        }, 0, 1);
    }

    public void startAdminTrail() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            double var = 0;
            Location loc;
            Vector first, second;
            Vector pLoc, o1, o2;
            ParticleData particle = new ParticleData(player.getUniqueId());

            @Override
            public void run() {

                if (!particle.hasID()) particle.setID(taskID);

                var += Math.PI / 16;
                loc = player.getLocation();
                pLoc = loc.clone().toVector();

                first = loc.clone().add(Math.cos(var)/1.5, 0.1, Math.sin(var)/1.5).toVector();
                second = loc.clone().add(Math.cos(var + Math.PI)/1.5, 0.1, Math.sin(var + Math.PI)/1.5).toVector();

                o1 = first.clone();
                o2 = second.clone();
                o1 = o1.clone().subtract(pLoc);
                o2 = o2.clone().subtract(pLoc);

                List<Player> packetRecipients = new ArrayList<>();
                packetRecipients.add(player);

                for (Entity nearbyEntity : player.getNearbyEntities(16, 16, 16)) {
                    if (nearbyEntity instanceof Player) packetRecipients.add((Player) nearbyEntity);
                }

                for (Player player : packetRecipients) {
                    packetManager.sendPacket(new ClientboundLevelParticlesPacket(
                            ParticleTypes.FLAME, true, first.getX(), first.getY(), first.getZ(), (float)o1.getX(), 0.8f, (float)o1.getZ(), 0.05F, 0
                    ), player);
                    packetManager.sendPacket(new ClientboundLevelParticlesPacket(
                            ParticleTypes.FLAME, true, second.getX(), second.getY(), second.getZ(), (float)o2.getX(), 0.8f, (float)o2.getZ(), 0.05F, 0
                    ), player);
                }


            }
        }, 0, 1);
    }

    public void startBlakeTrail() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            double var = 0;
            Location loc, first, second;
            ParticleData particle = new ParticleData(player.getUniqueId());

            @Override
            public void run() {

                if (!particle.hasID()) particle.setID(taskID);

                var += Math.PI / 16;
                loc = player.getLocation();
                first = loc.clone().add(Math.cos(var)/2, 2.3, Math.sin(var)/2);
                second = loc.clone().add(Math.cos(var + Math.PI)/2, 2.3, Math.sin(var + Math.PI)/2);

                List<Player> packetRecipients = new ArrayList<>();
                packetRecipients.add(player);

                for (Entity nearbyEntity : player.getNearbyEntities(16, 16, 16)) {
                    if (nearbyEntity instanceof Player) packetRecipients.add((Player) nearbyEntity);
                }



                for (Player player : packetRecipients) {
                    packetManager.sendPacket(new ClientboundLevelParticlesPacket(
                            ParticleTypes.SMOKE, true, first.getX(), first.getY(), first.getZ(), 0f, 0f, 0f, 1F, 0
                    ), player);
                    packetManager.sendPacket(new ClientboundLevelParticlesPacket(
                            ParticleTypes.SMOKE, true, second.getX(), second.getY(), second.getZ(), 0f, 0f, 0f, 1F, 0
                    ), player);
                }


            }
        }, 0, 1);
    }

}