package com.crumbed.events;

import com.crumbed.utils.PacketManager;
import com.crumbed.utils.ParticleData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerMovement implements Listener {

    private final SimpleParticleType[] particles = new SimpleParticleType[]{
            ParticleTypes.SOUL_FIRE_FLAME,
            ParticleTypes.FLAME
    };
    private final PacketManager packetManager;

    public PlayerMovement (PacketManager packetManager) {
        this.packetManager = packetManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!ParticleData.hasFakeID(event.getPlayer().getUniqueId())) return;
        Player player = event.getPlayer();
        Location loc = player.getLocation();

        List<Player> packetRecipients = new ArrayList<>();
        packetRecipients.add(player);

        for (Entity nearbyEntity : player.getNearbyEntities(16, 16, 16)) {
            if (nearbyEntity instanceof Player) packetRecipients.add((Player) nearbyEntity);
        }

        for (Player p : packetRecipients) {
            packetManager.sendPacket(new ClientboundLevelParticlesPacket(
                    particles[ParticleData.getFakeID(player.getUniqueId())], true, loc.getX(), loc.getY(), loc.getZ(), 0.2f, 0.2f, 0.2f, 0.02F, 3
            ), p);
        }

    }

}
