package com.crumbed.commands;

import com.crumbed.guis.TrailsGui;
import com.crumbed.utils.ParticleData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.crumbed.utils.Namespaces.idKey;

public class Trails implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use that command!"); return true; }
        if (!cmd.getName().equals("trails")) return true;
        Player player = (Player) sender;
        if (args.length > 0) {
            if (args[0].equals("off")) {
                ParticleData p = new ParticleData(player.getUniqueId());
                if (!p.hasID()) { player.sendMessage(ChatColor.RED+"You don't have any active trails!"); return true; }
                p.endTask();
                p.removeID();
                return true;
            }
        }

        TrailsGui menu = new TrailsGui();
        HashMap<ItemStack, String> trails = menu.getTrails();
        ArrayList<ItemStack> playerTrails = new ArrayList<>();
        //player.sendMessage(player.getName());
        if (player.getName().equals("oBreadier") || player.getName().equals("aCrumb")) {
            ItemStack ownerTrail = new ItemStack(Material.GHAST_TEAR);
            ItemMeta meta = ownerTrail.getItemMeta();
            assert meta != null;
            PersistentDataContainer customMeta = meta.getPersistentDataContainer();
            customMeta.set(idKey, PersistentDataType.STRING, "owner_trail");
            meta.setDisplayName(ChatColor.RED+"Owner Trail");
            ArrayList<String> itemLore = new ArrayList<>();
            itemLore.add("");
            itemLore.add(ChatColor.RED+"All Hail The Crumb.");
            itemLore.add("");
            itemLore.add(ChatColor.RED+""+ChatColor.BOLD+"SPECIAL TRAIL");
            meta.setLore(itemLore);
            ownerTrail.setItemMeta(meta);

            playerTrails.add(ownerTrail);

            ItemStack isleTrail = new ItemStack(Material.BLAZE_POWDER);
            meta = isleTrail.getItemMeta();
            assert meta != null;
            customMeta = meta.getPersistentDataContainer();
            customMeta.set(idKey, PersistentDataType.STRING, "isle_trail");
            meta.setDisplayName(ChatColor.RED+"Grand Searing Rune Trail");
            itemLore = new ArrayList<>();
            itemLore.add("");
            itemLore.add(ChatColor.RED+"57M.");
            itemLore.add("");
            itemLore.add(ChatColor.RED+""+ChatColor.BOLD+"SPECIAL TRAIL");
            meta.setLore(itemLore);
            isleTrail.setItemMeta(meta);

            playerTrails.add(isleTrail);
        }
        if (player.getName().equals("Idk_Blake") || player.getName().equals("oBreadier") || player.getName().equals("aCrumb")) {
            ItemStack item = new ItemStack(Material.BLACK_DYE);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            PersistentDataContainer customMeta = meta.getPersistentDataContainer();
            customMeta.set(idKey, PersistentDataType.STRING, "blake_trail");
            meta.setDisplayName(ChatColor.RED+"Trail / 2");
            ArrayList<String> itemLore = new ArrayList<>();
            itemLore.add("");
            itemLore.add(ChatColor.RED+"Anything / 2 = good.");
            itemLore.add("");
            itemLore.add(ChatColor.RED+""+ChatColor.BOLD+"SPECIAL TRAIL");
            meta.setLore(itemLore);
            item.setItemMeta(meta);
            playerTrails.add(item);
        }
        for (ItemStack item : trails.keySet()) {
            if (player.hasPermission(trails.get(item))) {
                playerTrails.add(item);
            }
        }
        if (playerTrails.isEmpty()) { player.sendMessage(ChatColor.RED+"You don't have any trails!"); return true; }
        //player.sendMessage(Arrays.toString(playerTrails.toArray()));
        menu.setPlayerTrails(playerTrails);
        menu.openInv(player);

        return true;
    }
}
