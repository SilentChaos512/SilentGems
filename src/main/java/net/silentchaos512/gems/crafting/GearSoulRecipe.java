package net.silentchaos512.gems.crafting;

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
import net.silentchaos512.gems.item.SoulGem;
import net.silentchaos512.gems.lib.soul.GearSoul;
import net.silentchaos512.gems.lib.soul.Soul;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.lib.collection.StackList;

import java.util.Collection;
import java.util.stream.Collectors;

public class GearSoulRecipe extends ShapedRecipe {
    private final ShapedRecipe recipe;

    public GearSoulRecipe(ShapedRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getRecipeOutput());
        this.recipe = recipe;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return recipe.matches(inv, worldIn);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack result = recipe.getCraftingResult(inv);

        // Grab the souls gems, collect the souls, and construct a gear soul
        StackList list = StackList.from(inv);
        Collection<Soul> souls = list.allOfType(SoulGem.class).stream()
                .map(SoulGem::getSoul)
                .collect(Collectors.toList());
        GearSoul gearSoul = GearSoul.construct(souls);
        SoulManager.setSoul(result, gearSoul);

        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return recipe.canFit(width, height);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return recipe.getRecipeOutput();
    }

    @Override
    public ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static final class Serializer implements IRecipeSerializer<GearSoulRecipe> {
        private static final ResourceLocation NAME = SilentGems.getId("gear_soul");
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() { }

        @Override
        public GearSoulRecipe read(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe recipe = RecipeSerializers.CRAFTING_SHAPED.read(recipeId, json);
            return new GearSoulRecipe(recipe);
        }

        @Override
        public GearSoulRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            ShapedRecipe recipe = RecipeSerializers.CRAFTING_SHAPED.read(recipeId, buffer);
            return new GearSoulRecipe(recipe);
        }

        @Override
        public void write(PacketBuffer buffer, GearSoulRecipe recipe) {
            RecipeSerializers.CRAFTING_SHAPED.write(buffer, recipe.recipe);
        }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }
    }
}
