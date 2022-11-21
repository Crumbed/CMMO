package com.crumbed.utils;

import com.crumbed.CrumbMMO;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ActionBarManager {

    private HashMap<UUID, TextComponent> actionBars = new HashMap<>();

    public ActionBarManager(CrumbMMO plugin) {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (UUID uuid : actionBars.keySet()){
                    if (Bukkit.getPlayer(uuid) == null) { continue; }
                    Bukkit.getPlayer(uuid).spigot().sendMessage(ChatMessageType.ACTION_BAR, actionBars.get(uuid));
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void setActionBar(UUID uuid, TextComponent text) {
        TextComponent aBar = new TextComponent(
                ChatColor.RED+""+(int)Bukkit.getPlayer(uuid).getHealth()+"/"+(int)Bukkit.getPlayer(uuid).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()
        );
        aBar.addExtra(text);
        actionBars.put(uuid, aBar);
    }
    public void removeActionBar(UUID uuid) {
        actionBars.remove(uuid);
    }


}
