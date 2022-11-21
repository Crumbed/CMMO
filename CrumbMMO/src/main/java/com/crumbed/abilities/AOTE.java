package com.crumbed.abilities;

import com.crumbed.CrumbMMO;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


import static com.crumbed.CrumbMMO.statsManager;
import static com.crumbed.utils.Namespaces.*;

public class AOTE extends Ability implements Listener {

    public AOTE() {
        super(
                "Instant Transmission",
                new String[] {
                        ChatColor.GOLD+"Item Ability: Instant Transmission "+ChatColor.GREEN+ChatColor.BOLD+"RIGHT CLICK",
                        ChatColor.GRAY+"Teleport "+ChatColor.GREEN+"8 blocks "+ChatColor.GRAY+"ahead of",
                        ChatColor.GRAY+"you and gain +50 "+ChatColor.WHITE+"Speed",
                        ChatColor.GRAY+"for "+ChatColor.GREEN+"3 seconds.",
                        ChatColor.DARK_GRAY+"Mana Cost: "+ChatColor.GRAY+"50"
                }
        );
    }

    @EventHandler
    public void teleport(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().hasItemMeta()) {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer customMeta = meta.getPersistentDataContainer();
                if (customMeta.has(abilityKey, PersistentDataType.STRING) && customMeta.get(abilityKey, PersistentDataType.STRING).equals("Instant Transmission")) {
                    if (statsManager.getStats(player.getUniqueId()).getMana() < customMeta.get(manaCostKey, PersistentDataType.INTEGER))
                    { player.sendMessage(ChatColor.RED+"You don't have enough mana to use this!"); return; }

                    int n = 8;

                    Block b = player.getTargetBlock(null, n);
                    Location loc = new Location(b.getWorld(), b.getX(), b.getY(), b.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                    player.teleport(loc);
                    player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

                    player.setFallDistance(0);
                    statsManager.getStats(player.getUniqueId()).setMana(statsManager.getStats(player.getUniqueId()).getMana() - customMeta.get(manaCostKey, PersistentDataType.INTEGER));
                    statsManager.useMana(player.getUniqueId(), customMeta.get(manaCostKey, PersistentDataType.INTEGER), name);
                }
            }
        }
    }

}
