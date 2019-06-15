package net.silentchaos512.gems.crafting.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.soul.Soul;

import java.util.stream.Stream;

public class SoulGemIngredient extends Ingredient {
    private final ResourceLocation soulId;

    public SoulGemIngredient(ResourceLocation soulId) {
        super(getStream(soulId));
        this.soulId = soulId;
    }

    private static Stream<? extends IItemList> getStream(ResourceLocation soulId) {
        Soul soul = Soul.from(soulId);
        return soul != null ? Stream.of(new SingleItemList(soul.getSoulGem())) : Stream.of();
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static final class Serializer implements IIngredientSerializer<SoulGemIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation NAME = SilentGems.getId("soul_gem");

        @Override
        public SoulGemIngredient parse(PacketBuffer buffer) {
            ResourceLocation soulId = buffer.readResourceLocation();
            return new SoulGemIngredient(soulId);
        }

        @Override
        public SoulGemIngredient parse(JsonObject json) {
            String soulIdStr = JSONUtils.getString(json, "soul");
            ResourceLocation soulId = new ResourceLocation(soulIdStr);
            return new SoulGemIngredient(soulId);
        }

        @Override
        public void write(PacketBuffer buffer, SoulGemIngredient ingredient) {
            buffer.writeResourceLocation(NAME);
            buffer.writeResourceLocation(ingredient.soulId);
        }

        public static void register() {
            CraftingHelper.register(NAME, INSTANCE);
        }
    }
}
