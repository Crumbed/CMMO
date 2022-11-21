package com.crumbed.utils;


import com.crumbed.CrumbMMO;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    private HashMap<UUID, Integer> playerCooldown = new HashMap<>();

    public CooldownManager(CrumbMMO plugin){

        new BukkitRunnable() {

            @Override
            public void run() {
                for (UUID uuid : playerCooldown.keySet()){
                    if (playerCooldown.get(uuid) == 0){
                        playerCooldown.remove(uuid);
                        continue;
                    }

                    playerCooldown.put(uuid, playerCooldown.get(uuid)-1);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void addPlayerToMap (Player player, int time){
        playerCooldown.put(player.getUniqueId(), time);
    }

    public int getPlayerCooldown (Player player) {
        if (!isPlayerOnCooldown(player)){ return 0; }
        return playerCooldown.get(player.getUniqueId());
    }

    public boolean isPlayerOnCooldown (Player player) {
        return playerCooldown.containsKey(player.getUniqueId());
    }
}
