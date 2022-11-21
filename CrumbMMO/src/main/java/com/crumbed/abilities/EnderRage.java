package com.crumbed.abilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.crumbed.utils.Namespaces.*;

public class EnderRage extends Ability implements Listener {

    public EnderRage() {
        super(
                "Ender Rage",
                new String[] {
                        ChatColor.GOLD+"Item Ability: Ender Rage "+ChatColor.GREEN+ChatColor.BOLD+"RIGHT CLICK",
                        ChatColor.GRAY+"Grants "+ChatColor.WHITE+"+50% speed "+ChatColor.GRAY+"and",
                        ChatColor.RED+"+100 strength "+ChatColor.GRAY+"for 3 seconds."
                }
        );
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().hasItemMeta()) {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer customMeta = meta.getPersistentDataContainer();
                if (customMeta.has(abilityKey, PersistentDataType.STRING) && customMeta.get(abilityKey, PersistentDataType.STRING).equals("Ender Rage")) {
                    player.sendMessage(ChatColor.RED + "Ability not in game yet bozo");
                }
            }
        }
    }

}
