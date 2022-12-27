package com.crumbed.customMobs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.crumbed.CrumbMMO.itemReg;


public class MobManager {

    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;
    public static HashMap<String, CMMOEntity> MOBS = new HashMap<>();

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
        MOBS = new HashMap<>();
        loadCustomMobs();
    }

    public void loadCustomMobs() {
        config = YamlConfiguration.loadConfiguration(file);
        ArrayList<String> ids = (ArrayList<String>) config.getStringList("registered-ids");


        for (String id : ids) {
            String configId = id+".";

            String name = ChatColor.GRAY + config.getString(configId+"name");

            int dmg = 0;
            int str = 0;
            int def = 0;

            if(config.contains(configId+"damage"))          dmg = config.getInt(configId+"damage");
            if(config.contains(configId+"strength"))        str = config.getInt(configId+"strength");
            if(config.contains(configId+"defense"))         def = config.getInt(configId+"defense");

            int hp = config.getInt(configId+"health");

            MOBS.put(id.toUpperCase(), new CMMOEntity(id, name, dmg, str, def, hp, 0.0f));

        }

    }

    public void saveCustomMobs() {
        ArrayList<String> ids = new ArrayList<>(config.getStringList("registered-ids"));

        config.set("registered-ids", ids);

        for (String id : ids) {
            String configId = id+".";
            CMMOEntity customEntity = MOBS.get(id.toUpperCase());

            config.set(configId+"name", customEntity.getName());

            int dmg = customEntity.getDmg();
            int str = customEntity.getStr();
            int def = customEntity.getDef();
            int hp = customEntity.getHp();
            float speed = customEntity.getSpeed();

            if(dmg != 0)          config.set(configId+"damage", dmg);
            if(str != 0)          config.set(configId+"strength", str);
            if(def != 0)          config.set(configId+"defense", def);
            if(speed != 0)        config.set(configId+"speed", speed);

            config.set(configId+"health", hp);

        }
        try {config.save(file);}catch(IOException e){}
    }

}





















