package com.crumbed.utils;

import com.crumbed.utils.NMSAccessException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
//import org.bukkit.entity.ArmorStand;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Implementation of the packet manager for the 1.19 minecraft java version.
 * The implementation uses a mixture of direct calls against the re-obfuscated server internals and reflection.
 */
public final class PacketManager1_19_R1 implements PacketManager {

    private final Method entityGetIdMethod;
    private final Method entityGetDataWatcherMethod;
    private final Method entityGetHandleMethod;
    private final Method entityGetBukkitEntityMethod;
    private final Method worldGetHandleMethod;
    private final Method playerConnectionSendPacketMethod;

    private final Field entityPlayerPlayerConnectionField;

    public PacketManager1_19_R1(final @NotNull Method entityGetIdMethod,
                                final @NotNull Method entityGetDataWatcherMethod,
                                final @NotNull Method entityGetHandleMethod,
                                final @NotNull Method entityGetBukkitEntityMethod,
                                final @NotNull Method worldGetHandleMethod,
                                final @NotNull Method playerConnectionSendPacketMethod,
                                final @NotNull Field entityPlayerPlayerConnectionField) {
        this.entityGetIdMethod = entityGetIdMethod;
        this.entityGetDataWatcherMethod = entityGetDataWatcherMethod;
        this.entityGetHandleMethod = entityGetHandleMethod;
        this.entityGetBukkitEntityMethod = entityGetBukkitEntityMethod;
        this.worldGetHandleMethod = worldGetHandleMethod;
        this.playerConnectionSendPacketMethod = playerConnectionSendPacketMethod;
        this.entityPlayerPlayerConnectionField = entityPlayerPlayerConnectionField;
    }

    @NotNull
    public static PacketManager1_19_R1 make() {
        try {
            return new PacketManager1_19_R1(
                    getMojangClass("world.entity.Entity").getMethod("ae"),
                    getMojangClass("world.entity.Entity").getMethod("ai"),
                    getCBClass("entity.CraftEntity").getMethod("getHandle"),
                    getMojangClass("world.entity.Entity").getMethod("getBukkitEntity"),
                    getCBClass("CraftWorld").getMethod("getHandle"),
                    getMojangClass("server.network.PlayerConnection")
                            .getMethod("a", getMojangClass("network.protocol.Packet")),
                    getMojangClass("server.level.EntityPlayer").getField("b")
            );
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create version specific server accessor", e);
        }
    }

    @NotNull
    private static Class<?> getMojangClass(@NotNull final String className) throws ClassNotFoundException {
        return Class.forName("net.minecraft." + className);
    }

    @NotNull
    private static Class<?> getCBClass(@NotNull final String className) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getName().split("\\.")[3]
                + "." + className);
    }

    @NotNull
    @Override
    public Object buildEntitySpawnPacket(@NotNull Object entity) {
        return new ClientboundAddEntityPacket((LivingEntity) entity);
    }

    @NotNull
    @Override
    public Object buildEntityMetadataPacket(@NotNull Object entity, boolean forceUpdateAll) {
        try {
            final int entityId = (int) this.entityGetIdMethod.invoke(entity);
            final Object dataWatcher = this.entityGetDataWatcherMethod.invoke(entity);
            return new ClientboundSetEntityDataPacket(entityId, (SynchedEntityData) dataWatcher, forceUpdateAll);
        } catch (final ReflectiveOperationException e) {
            throw new NMSAccessException("Failed to create entity metadata packet", e);
        }
    }

    @NotNull
    @Override
    public Object buildEntityDestroyPacket(@NotNull Object entity) {
        try {
            final int entityId = (int) this.entityGetIdMethod.invoke(entity);
            return new ClientboundRemoveEntitiesPacket(entityId);
        } catch (final ReflectiveOperationException e) {
            throw new NMSAccessException("Failed to create entity destroy packet", e);
        }
    }

    @NotNull
    @Override
    public Object buildEntityArmorStand(@NotNull Location location, @NotNull String name) {
        try {
            final World world = location.getWorld();
            final Level worldServer = ((CraftWorld) world).getHandle();

            final Object entityArmorStand = new ArmorStand(
                    worldServer,
                    location.getX(), location.getY(), location.getZ()
            );
            final org.bukkit.entity.ArmorStand armorStand = (org.bukkit.entity.ArmorStand) this.entityGetBukkitEntityMethod.invoke(entityArmorStand);
            armorStand.setMarker(true);
            armorStand.setInvisible(true);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(name);

            return entityArmorStand;
        } catch (final ReflectiveOperationException e) {
            throw new NMSAccessException("Failed to create new entity armor stand", e);
        }
    }

    @Override
    public void sendPacket(@NotNull Object packet, @NotNull Player player) {
        try {
            final Object handle = this.entityGetHandleMethod.invoke(player);
            final Object playerConnection = this.entityPlayerPlayerConnectionField.get(handle);
            this.playerConnectionSendPacketMethod.invoke(playerConnection, packet);
        } catch (final ReflectiveOperationException e) {
            throw new NMSAccessException(String.format("Failed to send packet to player %s", player.getUniqueId()), e);
        }
    }
}