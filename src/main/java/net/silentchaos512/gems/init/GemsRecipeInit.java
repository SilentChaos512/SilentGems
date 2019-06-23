package net.silentchaos512.gems.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.crafting.ingredient.SoulGemIngredient;
import net.silentchaos512.gems.crafting.recipe.*;
import net.silentchaos512.gems.crafting.tokenenchanter.TokenEnchanterRecipe;

public final class GemsRecipeInit {

    private GemsRecipeInit() {}

    public static void init() {
        SoulGemIngredient.Serializer.register();

        registerRecipeType("token_enchanting", TokenEnchanterRecipe.RECIPE_TYPE);
        register(SilentGems.getId("token_enchanting"), TokenEnchanterRecipe.SERIALIZER);

        register(ApplyChaosRuneRecipe.NAME, ApplyChaosRuneRecipe.SERIALIZER);
        register(ApplyEnchantmentTokenRecipe.NAME, ApplyEnchantmentTokenRecipe.SERIALIZER);
        register(GearSoulRecipe.NAME, GearSoulRecipe.SERIALIZER);
        register(ModifySoulUrnRecipe.NAME, ModifySoulUrnRecipe.SERIALIZER);
        register(SoulUrnRecipe.NAME, SoulUrnRecipe.SERIALIZER);

        if (SilentGems.isDevBuild()) {
//            MinecraftForge.EVENT_BUS.addListener(GemsRecipeInit::onPlayerJoinServer);
        }
    }

    private static void register(ResourceLocation name, IRecipeSerializer<?> serializer) {
        IRecipeSerializer.register(name.toString(), serializer);
    }

    private static void registerRecipeType(String name, IRecipeType<?> recipeType) {
        Registry.register(Registry.RECIPE_TYPE, SilentGems.getId(name), recipeType);
    }

    private static void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer().world.isRemote || event.getPlayer().world.getServer() == null) return;

        ResourceLocation[] recipes = event.getPlayer().world.getServer().getRecipeManager().getRecipes()
                .stream()
                .map(IRecipe::getId)
                .filter(name -> name.getNamespace().equals(SilentGems.MOD_ID))
                .toArray(ResourceLocation[]::new);

        SilentGems.LOGGER.info("DEV: Unlocking {} recipes in recipe book", recipes.length);
        event.getPlayer().unlockRecipes(recipes);
    }
}
