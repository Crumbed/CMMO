package com.crumbed.utils;

import com.crumbed.guis.TrailsGui;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class ParticleData {

    private static HashMap<UUID, Integer> TRAILS = new HashMap<>();
    private final UUID uuid;

    public ParticleData(UUID uuid) {
        this.uuid = uuid;
    }

    public void setID(int id) {
        TRAILS.put(uuid, id);
    }

    public int getID() {
        return TRAILS.get(uuid);
    }

    public boolean hasID() {
        return TRAILS.containsKey(uuid);
    }

    public void removeID() {
        TRAILS.remove(uuid);
    }

    public void endTask() {
        if (getID() == 1) return;
        Bukkit.getScheduler().cancelTask(getID());
    }

    public static boolean hasFakeID(UUID uuid) {
        if (TRAILS.containsKey(uuid))
            if (TRAILS.get(uuid) == 1 || TRAILS.get(uuid) == 0) return true;
        return false;
    }
    public static int getFakeID(UUID uuid) {
        if (hasFakeID(uuid)) return TRAILS.get(uuid);
        return -1;
    }

}
