package com.crumbed.utils;

import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

//import net.minecraft.server.v1_12_R1.NBTTagCompound;

public interface NBTSerializable extends ConfigurationSerializable {



    public CompoundTag serializeToNbt();

    public default Map<String, Object> serialize() {
        return NBTUtil.toMap(serializeToNbt());
    }

}