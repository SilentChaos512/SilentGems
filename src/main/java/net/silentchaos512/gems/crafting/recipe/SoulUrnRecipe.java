package net.silentchaos512.gems.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.item.GemItem;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.urn.UrnConst;
import net.silentchaos512.lib.collection.StackList;
import net.silentchaos512.utils.Color;

public final class SoulUrnRecipe extends ShapedRecipe {
    private static final ResourceLocation NAME = new ResourceLocation(SilentGems.MOD_ID, "soul_urn");

    // We're piggybacking on this ShapedRecipe. Just need to add an extra property on top (color).
    private final ShapedRecipe recipe;
    // The clay color of the urn being crafted. Typically matches the terracotta color, could be anything.
    private final int color;

    private SoulUrnRecipe(ShapedRecipe recipe, int color) {
        super(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getRecipeOutput());
        this.recipe = recipe;
        this.color = color;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return recipe.matches(inv, worldIn);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        StackList list = StackList.from(inv);
        ItemStack gemStack = list.firstOfType(GemItem.class);
        Gems gem = Gems.from(gemStack);
        return ModBlocks.soulUrn.getStack(this.color, gem);
    }

    @Override
    public boolean canFit(int width, int height) {
        return recipe.canFit(width, height);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ModBlocks.soulUrn.getStack(this.color, null);
    }

    @Override
    public String getGroup() {
        return recipe.getGroup();
    }

    @Override
    public ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static final class Serializer implements IRecipeSerializer<SoulUrnRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() { }

        @Override
        public SoulUrnRecipe read(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe recipe = RecipeSerializers.CRAFTING_SHAPED.read(recipeId, json);
            int color = Color.from(json, "urn_clay_color", UrnConst.UNDYED_COLOR).getColor();
            return new SoulUrnRecipe(recipe, color);
        }

        @Override
        public SoulUrnRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            ShapedRecipe recipe = RecipeSerializers.CRAFTING_SHAPED.read(recipeId, buffer);
            int color = buffer.readVarInt();
            return new SoulUrnRecipe(recipe, color);
        }

        @Override
        public void write(PacketBuffer buffer, SoulUrnRecipe recipe) {
            RecipeSerializers.CRAFTING_SHAPED.write(buffer, recipe.recipe);
            buffer.writeVarInt(recipe.color);
        }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }
    }
}
