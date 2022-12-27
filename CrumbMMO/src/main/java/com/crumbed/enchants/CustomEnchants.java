package com.crumbed.enchants;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomEnchants {

    public static final Enchantment HEALTH = new EnchantWrapper("health", "Health", 5);
    public static final Enchantment CPROTECTION = new EnchantWrapper("cprotection", "Protection", 5);
    public static final Enchantment SPEED = new EnchantWrapper("speed", "Speed", 4);
    public static final Enchantment BIGBRAIN = new EnchantWrapper("big_brain", "Big Brain", 10);
    public static final Enchantment CSHARPNESS = new EnchantWrapper("csharpness", "Sharpness", 5);
    public static final Enchantment FIRST_STRIKE = new EnchantWrapper("first_strike", "First Strike", 1);
    public static final Enchantment LIFE_STEAL = new EnchantWrapper("life_steal", "Life Steal", 5);
    public static final Enchantment CRITICAL = new EnchantWrapper("critical", "Critical", 5);

    public static void register() {
        boolean healthRegistered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(HEALTH);
        boolean cprotRegistered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(CPROTECTION);
        boolean speedRegistered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(SPEED);
        boolean bigbrainRegistered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(BIGBRAIN);
        boolean csharpRegistered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(CSHARPNESS);
        boolean firstStrikeRegistered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(FIRST_STRIKE);
        boolean lifeStealRegistered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(LIFE_STEAL);
        boolean criticalRegistered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(CRITICAL);

        if (!healthRegistered) registerEnchant(HEALTH);
        if (!cprotRegistered) registerEnchant(CPROTECTION);
        if (!speedRegistered) registerEnchant(SPEED);
        if (!bigbrainRegistered) registerEnchant(BIGBRAIN);
        if (!csharpRegistered) registerEnchant(CSHARPNESS);
        if (!firstStrikeRegistered) registerEnchant(FIRST_STRIKE);
        if (!lifeStealRegistered) registerEnchant(LIFE_STEAL);
        if (!criticalRegistered) registerEnchant(CRITICAL);

    }

    public static void registerEnchant(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if (registered) {
            Bukkit.getServer().getConsoleSender().sendMessage("Enchantment "+enchantment.getKey()+" has been registered.");
        }
    }

}
