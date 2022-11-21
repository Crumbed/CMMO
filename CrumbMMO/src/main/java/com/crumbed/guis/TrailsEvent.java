package com.crumbed.guis;

import com.crumbed.utils.Effects;
import com.crumbed.utils.PacketManager;
import com.crumbed.utils.ParticleData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import static com.crumbed.utils.Namespaces.*;

public class TrailsEvent implements Listener {

    private TrailsGui menu;
    private JavaPlugin plugin;
    private final PacketManager packetManager;

    public TrailsEvent (JavaPlugin plugin, PacketManager packetManager) {
        menu = new TrailsGui();
        this.plugin = plugin;
        this.packetManager = packetManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(menu.getInv((Player)event.getWhoClicked()))) return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        if(event.getView().getType() == InventoryType.PLAYER) return;

        ParticleData particle = new ParticleData(player.getUniqueId());

        if (particle.hasID()) {
            particle.endTask();
            particle.removeID();
        }

        Effects trails = new Effects(player, plugin, packetManager);
        Inventory inv = menu.getInv(player);

        switch (event.getSlot()) {
            case 0:
                selectTrail(player, 0, inv, trails, particle);

                player.closeInventory();
                player.updateInventory();
                break;
            case 1:
                selectTrail(player, 1, inv, trails, particle);

                player.closeInventory();
                player.updateInventory();
                break;
            case 2:
                selectTrail(player, 2, inv, trails, particle);

                player.closeInventory();
                player.updateInventory();
                break;
            case 3:
                selectTrail(player, 3, inv, trails, particle);

                player.closeInventory();
                player.updateInventory();
                break;
            case 4:
                selectTrail(player, 4, inv, trails, particle);

                player.closeInventory();
                player.updateInventory();
                break;
            case 5:
                selectTrail(player, 5, inv, trails, particle);

                player.closeInventory();
                player.updateInventory();
                break;
            case 6:
                selectTrail(player, 6, inv, trails, particle);

                player.closeInventory();
                player.updateInventory();
                break;
            case 7:
                selectTrail(player, 7, inv, trails, particle);

                player.closeInventory();
                player.updateInventory();
                break;
            case 8:
                selectTrail(player, 8, inv, trails, particle);

                player.closeInventory();
                player.updateInventory();
                break;
            default:
                break;
        }


    }

    public void selectTrail(Player player, int slot, Inventory inv, Effects trails, ParticleData particle) {
        ItemStack item = inv.getItem(slot);
        if (item == null) { player.sendMessage(ChatColor.RED+"Something went wrong, please try again."); player.closeInventory(); return; }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer customMeta = meta.getPersistentDataContainer();
        String id = customMeta.get(idKey, PersistentDataType.STRING);
        if (id.equals("owner_trail")) trails.startOwnerTrail();
        if (id.equals("isle_trail")) trails.startCrimsonIsleTrail();
        if (id.equals("admin_trail")) trails.startAdminTrail();
        if (id.equals("blake_trail")) trails.startBlakeTrail();
        if (id.equals("divine_trail")) particle.setID(0);
        if (id.equals("legendary_trail")) particle.setID(1);
    }

}
