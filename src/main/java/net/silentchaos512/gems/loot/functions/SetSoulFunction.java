package net.silentchaos512.gems.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.functions.ILootFunction;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.lib.soul.Soul;

public class SetSoulFunction implements ILootFunction {
    public static final Serializer SERIALIZER = new Serializer(SilentGems.getId("set_soul"), SetSoulFunction.class);

    private final String soulId;

    public SetSoulFunction(String soulId) {
        this.soulId = soulId;
    }

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        ItemStack result = itemStack.copy();
        Soul soul = Soul.from(soulId);
        if (soul != null) {
            SoulGemItem.setSoul(result, soul);
        }
        return result;
    }

    public static ILootFunction.IBuilder builder(Soul soul) {
        return () -> new SetSoulFunction(soul.getId().toString());
    }

    public static class Serializer extends ILootFunction.Serializer<SetSoulFunction> {
        protected Serializer(ResourceLocation location, Class<SetSoulFunction> clazz) {
            super(location, clazz);
        }

        @Override
        public void serialize(JsonObject json, SetSoulFunction value, JsonSerializationContext serializationContext) {
            json.addProperty("soul", value.soulId);
        }

        @Override
        public SetSoulFunction deserialize(JsonObject json, JsonDeserializationContext context) {
            String soulId = JSONUtils.getString(json, "soul");
            return new SetSoulFunction(soulId);
        }
    }
}
