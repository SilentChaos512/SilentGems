package net.silentchaos512.gems.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.crafting.ingredient.SoulGemIngredient;
import net.silentchaos512.gems.crafting.recipe.*;

public final class GemsRecipeInit {
    private GemsRecipeInit() {}

    public static void init() {
        SoulGemIngredient.Serializer.register();

       register(ApplyChaosRuneRecipe.NAME, ApplyChaosRuneRecipe.SERIALIZER);
       register(ApplyEnchantmentTokenRecipe.NAME, ApplyEnchantmentTokenRecipe.SERIALIZER);
       register(ApplyGearSoulRecipe.NAME, ApplyGearSoulRecipe.SERIALIZER);
       register(GearSoulRecipe.NAME, GearSoulRecipe.SERIALIZER);
       register(ModifySoulUrnRecipe.NAME, ModifySoulUrnRecipe.SERIALIZER);
       register(SoulUrnRecipe.NAME, SoulUrnRecipe.SERIALIZER);

        if (SilentGems.isDevBuild()) {
            MinecraftForge.EVENT_BUS.addListener(GemsRecipeInit::onPlayerJoinServer);
        }
    }

    private static void register(ResourceLocation name, IRecipeSerializer<?> serializer) {
        IRecipeSerializer.register(name.toString(), serializer);
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
