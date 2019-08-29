package net.silentchaos512.gems.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class AltarTransmutationRecipe extends SingleItemRecipe {
    public static final IRecipeType<AltarTransmutationRecipe> RECIPE_TYPE = new IRecipeType<AltarTransmutationRecipe>() {
        @Override
        public String toString() {
            return "silentgems:altar_transmutation";
        }
    };
    public static final Serializer SERIALIZER = new Serializer();

    private final Ingredient catalyst;
    private int chaosGenerated = 5000;
    private int processTime = 200;

    public AltarTransmutationRecipe(ResourceLocation id, Ingredient ingredient, Ingredient catalyst, ItemStack result) {
        super(RECIPE_TYPE, SERIALIZER, id, "", ingredient, result);
        this.catalyst = catalyst;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        ItemStack inputItem = inv.getStackInSlot(0);
        ItemStack catalystItem = inv.getStackInSlot(1);
        return this.ingredient.test(inputItem) && this.catalyst.test(catalystItem);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.ingredient);
        list.add(this.catalyst);
        return list;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Ingredient getCatalyst() {
        return catalyst;
    }

    public int getChaosGenerated() {
        return chaosGenerated;
    }

    public int getProcessTime() {
        return processTime;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AltarTransmutationRecipe> {
        @Override
        public AltarTransmutationRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            Ingredient catalyst = Ingredient.deserialize(json.get("catalyst"));
            ItemStack resultStack = ShapedRecipe.deserializeItem(json.getAsJsonObject("result"));

            AltarTransmutationRecipe recipe = new AltarTransmutationRecipe(recipeId, ingredient, catalyst, resultStack);
            recipe.chaosGenerated = JSONUtils.getInt(json, "chaosGenerated", recipe.chaosGenerated);
            recipe.processTime = JSONUtils.getInt(json, "processTime", recipe.processTime);
            return recipe;
        }

        @Override
        public AltarTransmutationRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            Ingredient catalyst = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            AltarTransmutationRecipe recipe = new AltarTransmutationRecipe(recipeId, ingredient, catalyst, result);
            recipe.chaosGenerated = buffer.readVarInt();
            recipe.processTime = buffer.readVarInt();
            return recipe;
        }

        @Override
        public void write(PacketBuffer buffer, AltarTransmutationRecipe recipe) {
            recipe.ingredient.write(buffer);
            recipe.catalyst.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeVarInt(recipe.chaosGenerated);
            buffer.writeVarInt(recipe.processTime);
        }
    }
}
