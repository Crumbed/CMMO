package com.crumbed.customMobs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;



public class MobManager {

    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;

    public MobManager (JavaPlugin plugin) {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), "CustomMobs.yml");
        if(!file.exists()) {try { file.createNewFile(); } catch (IOException e) {}}

        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getMobConfig() {
        return config;
    }

    public void saveMobConfig() {
        try {config.save(file);} catch (IOException e) {}
    }

    public void reloadMobConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
