package com.crumbed.events;

import com.crumbed.utils.ParticleData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.crumbed.CrumbMMO.statsManager;

public class PlayerLeave implements Listener {

    @EventHandler
    public static void onPlayerLeave(PlayerQuitEvent event) {
        statsManager.removeFuckedStats(event.getPlayer().getUniqueId());
        statsManager.removeUuid(event.getPlayer().getUniqueId());
        ParticleData p = new ParticleData(event.getPlayer().getUniqueId());
        if (p.hasID()) p.endTask();
    }

}
