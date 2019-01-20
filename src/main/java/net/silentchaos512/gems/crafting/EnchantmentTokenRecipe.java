package net.silentchaos512.gems.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;

// TODO: Add a block to create enchanted tokens? This would simplify this code a lot. Otherwise, I will need to copy many methods from ShapedRecipe...
public class EnchantmentTokenRecipe extends ShapedRecipe {
    public EnchantmentTokenRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
    }

    public static class Serializer implements IRecipeSerializer<EnchantmentTokenRecipe> {
        private static final ResourceLocation NAME = new ResourceLocation(SilentGems.MOD_ID, "enchantment_token");

        @Override
        public EnchantmentTokenRecipe read(ResourceLocation recipeId, JsonObject json) {
            return null;
        }

        @Override
        public EnchantmentTokenRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return null;
        }

        @Override
        public void write(PacketBuffer buffer, EnchantmentTokenRecipe recipe) {
            //
        }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }
    }
}
