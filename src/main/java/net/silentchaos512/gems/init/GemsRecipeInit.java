package net.silentchaos512.gems.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.crafting.ingredient.SoulElementIngredient;
import net.silentchaos512.gems.crafting.ingredient.SoulGemIngredient;
import net.silentchaos512.gems.crafting.recipe.*;
import net.silentchaos512.lib.crafting.recipe.ExtendedShapedRecipe;

import java.util.function.Supplier;

public final class GemsRecipeInit {
    public static final RegistryObject<AltarTransmutationRecipe.Serializer> ALTAR_TRANSMUTATION = registerSerializer("altar_transmutation", AltarTransmutationRecipe.Serializer::new);
    public static final RegistryObject<SpecialRecipeSerializer<?>> APPLY_CHAOS_RUNE = registerSerializer("apply_chaos_rune", () ->
            new SpecialRecipeSerializer<>(ApplyChaosRuneRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<?>> APPLY_ENCHANTMENT_TOKEN = registerSerializer("apply_enchantment_token", () ->
            new SpecialRecipeSerializer<>(ApplyEnchantmentTokenRecipe::new));
    public static final RegistryObject<IRecipeSerializer<?>> GEAR_SOUL_RECIPE = registerSerializer("gear_soul", () ->
            ExtendedShapedRecipe.Serializer.basic(GearSoulRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<?>> MODIFY_SOUL_URN = registerSerializer("modify_soul_urn", () ->
            new SpecialRecipeSerializer<>(ModifySoulUrnRecipe::new));
    public static final RegistryObject<SoulUrnRecipe.Serializer> SOUL_URN = registerSerializer("soul_urn", SoulUrnRecipe.Serializer::new);
    public static final RegistryObject<TokenEnchanterRecipe.Serializer> TOKEN_ENCHANTING = registerSerializer("token_enchanting", TokenEnchanterRecipe.Serializer::new);

    private GemsRecipeInit() {}

    public static void register() {
        SoulElementIngredient.Serializer.register();
        SoulGemIngredient.Serializer.register();

        registerRecipeType("altar_transmutation", AltarTransmutationRecipe.RECIPE_TYPE);
        registerRecipeType("token_enchanting", TokenEnchanterRecipe.RECIPE_TYPE);
    }

    private static <T extends IRecipeSerializer<? extends IRecipe<?>>> RegistryObject<T> registerSerializer(String name, Supplier<T> serializer) {
        return Registration.RECIPE_SERIALIZERS.register(name, serializer);
    }

    private static void registerRecipeType(String name, IRecipeType<?> recipeType) {
        Registry.register(Registry.RECIPE_TYPE, SilentGems.getId(name), recipeType);
    }
}
