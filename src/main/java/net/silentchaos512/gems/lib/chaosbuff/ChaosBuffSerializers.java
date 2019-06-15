package net.silentchaos512.gems.lib.chaosbuff;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;

import java.util.HashMap;
import java.util.Map;

public final class ChaosBuffSerializers {
    private static final Map<ResourceLocation, IChaosBuffSerializer<?>> REGISTRY = new HashMap<>();

    static {
        register(SimpleChaosBuff.SERIALIZER);
        register(PotionChaosBuff.SERIALIZER);
    }

    private ChaosBuffSerializers() { }

    public static <S extends IChaosBuffSerializer<T>, T extends IChaosBuff> S register(S serializer) {
        if (REGISTRY.containsKey(serializer.getName())) {
            throw new IllegalArgumentException("Duplicate chaos buff serializer " + serializer.getName());
        }
        REGISTRY.put(serializer.getName(), serializer);
        return serializer;
    }

    public static IChaosBuff deserialize(ResourceLocation id, JsonObject json) {
        String typeStr = JSONUtils.getString(json, "type");
        if (!typeStr.contains(":")) typeStr = SilentGems.RESOURCE_PREFIX + typeStr;
        ResourceLocation type = new ResourceLocation(typeStr);

        IChaosBuffSerializer<?> serializer = REGISTRY.get(type);
        if (serializer == null) {
            throw new JsonParseException("Invalid or unsupported trait type " + type);
        }
        return serializer.read(id, json);
    }

    public static IChaosBuff read(PacketBuffer buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        ResourceLocation type = buffer.readResourceLocation();
        IChaosBuffSerializer<?> serializer = REGISTRY.get(type);
        if (serializer == null) {
            throw new IllegalArgumentException("Unknown trait serializer " + type);
        }
        return serializer.read(id, buffer);
    }

    public static <T extends IChaosBuff> void write(T trait, PacketBuffer buffer) {
        buffer.writeResourceLocation(trait.getId());
        buffer.writeResourceLocation(trait.getSerializer().getName());
        IChaosBuffSerializer<T> serializer = (IChaosBuffSerializer<T>) trait.getSerializer();
        serializer.write(buffer, trait);
    }
}
