package com.crumbed;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.crumbed.abilities.EnderRage;
import com.crumbed.commands.*;
import com.crumbed.crafting.RecipeRegistry;
import com.crumbed.customItems.ItemManager;
import com.crumbed.customItems.ItemRegistry;
import com.crumbed.abilities.AOTE;
import com.crumbed.customMobs.MobManager;
import com.crumbed.customMobs.SpawnMob;
import com.crumbed.enchants.CustomEnchants;
import com.crumbed.events.*;
import com.crumbed.guis.*;
import com.crumbed.stats.StatsManager;
import com.crumbed.utils.ActionBarManager;
import com.crumbed.utils.PacketManager;
import com.crumbed.utils.PacketManager1_19_R1;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;

public final class CrumbMMO extends JavaPlugin implements Listener {

    private static CrumbMMO plugin;
    public static ActionBarManager abm;
    public static StatsManager statsManager;
    public static ItemManager itemManager;
    public static ItemRegistry itemReg;
    public static MobManager mobManager;
    public static HeadDatabaseAPI headApi;
    public static RecipeRegistry recipeRegistry;
    public static ProtocolManager manager;


    @Override
    public void onEnable() {
        plugin = this;
        PluginManager pm = this.getServer().getPluginManager();

        manager = ProtocolLibrary.getProtocolManager();
        pm.registerEvents(this, this);

        saveDefaultConfig();
        CustomEnchants.register();

        itemManager = new ItemManager(this);
        itemManager.getItemConfig().addDefault("registered-ids", new ArrayList<String>());
        itemManager.getItemConfig().options().copyDefaults(true);
        itemManager.saveItemConfig();

        itemReg = new ItemRegistry(this);

        mobManager = new MobManager(this);
        mobManager.getMobConfig().addDefault("registered-ids", new ArrayList<String>());
        mobManager.getMobConfig().options().copyDefaults(true);
        mobManager.saveMobConfig();

        TrailsGui trailsGui = new TrailsGui();
        trailsGui.register();

        abm = new ActionBarManager(this);
        statsManager = new StatsManager(this);
        PacketManager packetManager = PacketManager1_19_R1.make();


        pm.registerEvents(new SkinEvent(this), this);
        pm.registerEvents(new SpawnMob(), this);
        pm.registerEvents(new PlayerMovement(packetManager), this);
        pm.registerEvents(new TrailsEvent(this, packetManager), this);
        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new PlayerLeave(), this);
        pm.registerEvents(new Damage(this, packetManager), this);
        pm.registerEvents(new AOTE(), this);
        pm.registerEvents(new EnderRage(), this);
        pm.registerEvents(new BlockPlace(), this);
        StatsCmd statsCmds = new StatsCmd(this);
        StatsTabComplete statsTabComplete = new StatsTabComplete(this);

        CGive cGive = new CGive(this);
        CGiveTabComplete cGiveTabComplete = new CGiveTabComplete(this);

        Reload reload = new Reload(this);

        Trails trails = new Trails();

        Skins skins = new Skins();

        Craft craft = new Craft();

        Objects.requireNonNull(getCommand("stats")).setTabCompleter(statsTabComplete);
        Objects.requireNonNull(getCommand("stats")).setExecutor(statsCmds);

        Objects.requireNonNull(getCommand("cgive")).setTabCompleter(cGiveTabComplete);
        Objects.requireNonNull(getCommand("cgive")).setExecutor(cGive);

        Objects.requireNonNull(getCommand("creload")).setExecutor(reload);

        Objects.requireNonNull(getCommand("trails")).setExecutor(trails);

        Objects.requireNonNull(getCommand("skins")).setExecutor(skins);

        Objects.requireNonNull(getCommand("craft")).setExecutor(craft);
        Objects.requireNonNull(getCommand("carft")).setExecutor(craft);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        itemManager.saveCustomItems(this);
        itemManager.saveItemConfig();
        mobManager.saveCustomMobs();
        mobManager.saveMobConfig();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            statsManager.removeFuckedStats(player.getUniqueId());
            statsManager.removeUuid(player.getUniqueId());
        }

    }


    public static CrumbMMO getInstance() { return plugin; }


    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent event) {
        headApi = new HeadDatabaseAPI();
        try {
            ItemStack item = headApi.getItemHead("7129");
            Bukkit.getLogger().info(headApi.getItemID(item));
            Bukkit.getLogger().info("HeadDatabase Hooked");
            itemManager.loadCustomItems(this);
            recipeRegistry = new RecipeRegistry(this);
            recipeRegistry.getCraftConfig().addDefault("registered-ids", new ArrayList<String>());
            recipeRegistry.getCraftConfig().options().copyDefaults(true);
            recipeRegistry.saveCraftConfig();

        } catch (NullPointerException e) {
            Bukkit.getLogger().info("Could not find the head you were looking for");
        }
    }

}
