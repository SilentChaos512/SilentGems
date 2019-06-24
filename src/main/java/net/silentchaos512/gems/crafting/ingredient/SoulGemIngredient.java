package net.silentchaos512.gems.crafting.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.lib.soul.Soul;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class SoulGemIngredient extends Ingredient {
    // Souls don't exist yet, so we store the ID and get the soul when testing
    private final ResourceLocation soulId;

    public SoulGemIngredient(ResourceLocation soulId) {
        super(Stream.of(new SingleItemList(SoulGemItem.getStack(soulId))));
        this.soulId = soulId;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        Soul soul = Soul.from(soulId);
        return soul != null && stack.getItem() instanceof SoulGemItem && soul == SoulGemItem.getSoul(stack);
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
            buffer.writeResourceLocation(ingredient.soulId);
        }

        public static void register() {
            CraftingHelper.register(NAME, INSTANCE);
        }
    }
}
