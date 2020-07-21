package net.silentchaos512.gems.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.JSONUtils;
import net.silentchaos512.gems.init.GemsLoot;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.lib.soul.Soul;

public class SetSoulFunction extends LootFunction {
    public static final Serializer SERIALIZER = new Serializer();

    private final String soulId;

    protected SetSoulFunction(ILootCondition[] conditionsIn, String soulId) {
        super(conditionsIn);
        this.soulId = soulId;
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {
        ItemStack result = stack.copy();
        Soul soul = Soul.from(soulId);
        if (soul != null) {
            SoulGemItem.setSoul(result, soul);
        }
        return result;
    }

    public static ILootFunction.IBuilder builder(Soul soul) {
        return builder(conditions -> new SetSoulFunction(conditions, soul.getId().toString()));
    }

    @Override
    public LootFunctionType func_230425_b_() {
        return GemsLoot.SET_SOUL;
    }

    public static class Serializer extends LootFunction.Serializer<SetSoulFunction> {
        @Override
        public void func_230424_a_(JsonObject json, SetSoulFunction value, JsonSerializationContext serializationContext) {
            json.addProperty("soul", value.soulId);
        }

        @Override
        public SetSoulFunction deserialize(JsonObject json, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            String soulId = JSONUtils.getString(json, "soul");
            return new SetSoulFunction(conditionsIn, soulId);
        }
    }
}
