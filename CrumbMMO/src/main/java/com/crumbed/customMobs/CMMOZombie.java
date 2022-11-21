package com.crumbed.customMobs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

public class CMMOZombie extends Zombie {

    public CMMOZombie (Location loc) {
        super(EntityType.ZOMBIE, ((CraftWorld) loc.getWorld()).getHandle());

        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        this.setCustomName(Component.nullToEmpty(ChatColor.GRAY+"Zombie [Lvl: 1]"));
        this.setCustomNameVisible(true);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<Player>(this, Player.class, true));
    }

}
