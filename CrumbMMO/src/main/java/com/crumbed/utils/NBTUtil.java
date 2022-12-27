package com.crumbed.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crumbed.utils.ConfigurationSerializableByteArray;
import com.crumbed.utils.ConfigurationSerializableIntArray;
import com.crumbed.utils.ConfigurationSerializableLongArray;

import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class NBTUtil {

    public static final int END = 0;
    public static final int BYTE = 1;
    public static final int SHORT = 2;
    public static final int INT = 3;
    public static final int LONG = 4;
    public static final int FLOAT = 5;
    public static final int DOUBLE = 6;
    public static final int BYTE_ARRAY = 7;
    public static final int STRING = 8;
    public static final int LIST = 9;
    public static final int COMPOUND = 10;
    public static final int INT_ARRAY = 11;
    public static final int LONG_ARRAY = 12;

    protected NBTUtil() {}


    // ============== IO ==============

    public static void writeNBTTagCompound(File saveFile, CompoundTag compound) throws IOException {
        File tempFile = new File(saveFile.getAbsolutePath() + ".tmp");
        if (tempFile.exists()) tempFile.delete();
        tempFile.createNewFile();

        NbtIo.writeCompressed(compound, Files.newOutputStream(tempFile.toPath()));
        tempFile.renameTo(saveFile);
    }

    public static CompoundTag readNBTTagCompound(File saveFile) throws IOException {
        CompoundTag compound = NbtIo.readCompressed(Files.newInputStream(saveFile.toPath()));
        return compound;
    }


    // ============== MinecraftKey ==============

    public static CompoundTag serializeKey(ResourceLocation key) {
        CompoundTag compound = new CompoundTag();
        compound.putString("namespace", key.getNamespace());
        compound.putString("key", key.getPath());
        return compound;
    }

    public static ResourceLocation deserializeKey(CompoundTag compound) {
        return new ResourceLocation(compound.getString("namespace"), compound.getString("key"));
    }


    // ============== ItemStack ==============

    public static CompoundTag serializeItemStack(ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        stack.save(tag);
        return tag;
    }

    /*public static ItemStack deserializeItemStack(CompoundTag tag) {
        return new ItemStack(tag);
    }*/


    // ============== RecipeItemStack ==============

    public static CompoundTag serializeRecipeItemStack(Ingredient recipeItemStack) {
        if (recipeItemStack == Ingredient.EMPTY) return new CompoundTag(); //empty ingredient -> empty compound

        CompoundTag compound = new CompoundTag();
        if (recipeItemStack.itemStacks.length == 0) return compound; //not the empty ingredient, but no choices

        ListTag nbtChoices = new ListTag();
        for (ItemStack choice : recipeItemStack.itemStacks) {
            CompoundTag choiceCompound = new CompoundTag();
            choice.save(choiceCompound);
            nbtChoices.add(choiceCompound);
        }
        compound.put("choices", nbtChoices);

        return compound;
    }

    /*
    public static RecipeItemStack deserializeRecipeItemStack(NBTTagCompound compound) {
        if (compound.isEmpty()) return RecipeItemStack.a; //empty compound -> empty ingredient

        if (compound.hasKeyOfType("choices", LIST)) {
            NBTTagList choicesList = compound.getList("choices", COMPOUND);
            List<ItemStack> itemsList = new ArrayList<>();
            for (int i = 0; i < choicesList.size(); i++) {
                NBTTagCompound choiceCompound = choicesList.get(i);
                ItemStack stack = new ItemStack(choiceCompound);
                itemsList.add(stack);
            }

            ItemStack[] choices = itemsList.toArray(new ItemStack[itemsList.size()]);
            return RecipeItemStack.a(choices);
        }

        return RecipeItemStack.a(new ItemStack[0]); //not an empty compound, but no choices.
    }*/



    // ============== ConfigurationSerializable Conversion ==============

    public static Map<String, Object> toMap(CompoundTag compound) {
        Map<String, Object> map = new HashMap<>();
        for (String key : compound.getAllKeys()) { //c() => keyset
            Tag value = compound.get(key);
            map.put(key, toObject(value));
        }
        return map;
    }

    public static CompoundTag fromMap(Map<String, ?> map) {
        CompoundTag compound = new CompoundTag();
        map.forEach((key, value) -> compound.put(key, fromObject(value)));
        return compound;
    }

    public static ListTag fromList(List<?> list) {
        ListTag nbtList = new ListTag();
        list.forEach(o -> nbtList.add(fromObject(o)));
        return nbtList;
    }

    public static List toList(ListTag nbtList) {
        List list = new ArrayList();
        for (int i = 0; i < nbtList.size(); i++) {
            list.add(toObject(nbtList.get(i)));
        }
        return list;
    }

    public static Object toObject(Tag nbtBase) {
        if (nbtBase instanceof EndTag) {
            return null;
        } else if (nbtBase instanceof ByteTag) {
            return ((ByteTag) nbtBase).getAsByte();
        } else if (nbtBase instanceof ShortTag) {
            return ((ShortTag) nbtBase).getAsShort();
        } else if (nbtBase instanceof IntTag) {
            return ((IntTag) nbtBase).getAsInt();
        } else if (nbtBase instanceof LongTag) {
            return ((LongTag) nbtBase).getAsLong();
        } else if (nbtBase instanceof FloatTag) {
            return ((FloatTag) nbtBase).getAsFloat();
        } else if (nbtBase instanceof DoubleTag) {
            return ((DoubleTag) nbtBase).getAsDouble();
        } else if (nbtBase instanceof ByteArrayTag) {
            byte[] bytes = ((ByteArrayTag) nbtBase).getAsByteArray();
            return new ConfigurationSerializableByteArray(bytes);
        } else if (nbtBase instanceof IntArrayTag) {
            int[] ints = ((IntArrayTag) nbtBase).getAsIntArray();
            return new ConfigurationSerializableIntArray(ints);
        } else if (nbtBase instanceof LongArrayTag) {
            LongArrayTag array = (LongArrayTag) nbtBase;
            long[] longs = (long[]) ReflectionUtil.getDeclaredFieldValue(array, "b");
            return new ConfigurationSerializableLongArray(longs);
        } else if (nbtBase instanceof StringTag) {
            return ((StringTag) nbtBase).getAsString();
        } else if (nbtBase instanceof ListTag) {
            return toList((ListTag) nbtBase);
        } else if (nbtBase instanceof CompoundTag) {
            return toMap((CompoundTag) nbtBase);
        }

        throw new IllegalArgumentException("Unrecognized NBT type: " + nbtBase);
    }

    public static Tag fromObject(Object o) {

        if (o instanceof EndTag) {
            return null;
        } else if (o instanceof Byte) {
            return ByteTag.valueOf((Byte) o);
        } else if (o instanceof Short) {
            return ShortTag.valueOf((Short) o);
        } else if (o instanceof Integer) {
            return IntTag.valueOf((Integer) o);
        } else if (o instanceof Long) {
            return LongTag.valueOf((Long) o);
        } else if (o instanceof Float) {
            return FloatTag.valueOf((Float) o);
        } else if (o instanceof Double) {
            return DoubleTag.valueOf((Double) o);
        } else if (o instanceof byte[]) {
            return new ByteArrayTag((byte[]) o);
        } else if (o instanceof ConfigurationSerializableByteArray) {
            return new ByteArrayTag(((ConfigurationSerializableByteArray) o).getBytes());
        } else if (o instanceof int[]) {
            return new IntArrayTag((int[]) o);
        } else if (o instanceof ConfigurationSerializableIntArray) {
            return new IntArrayTag(((ConfigurationSerializableIntArray) o).getInts());
        } else if (o instanceof long[]) {
            return new LongArrayTag((long[]) o);
        } else if (o instanceof ConfigurationSerializableLongArray) {
            return new LongArrayTag(((ConfigurationSerializableLongArray) o).getLongs());
        } else if (o instanceof String) {
            return StringTag.valueOf((String) o);
        } else if (o instanceof List) {
            return fromList((List) o);
        } else if (o instanceof Map) {
            return fromMap((Map) o);
        }

        throw new IllegalArgumentException("Object not nbt deserializable: " + o);
    }


}