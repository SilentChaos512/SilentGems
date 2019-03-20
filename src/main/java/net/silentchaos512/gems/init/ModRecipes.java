package net.silentchaos512.gems.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.crafting.*;

public final class ModRecipes {
    private ModRecipes() {}

    public static void init() {
        SoulGemIngredient.Serializer.register();

        RecipeSerializers.register(ApplyChaosRuneRecipe.Serializer.INSTANCE);
        RecipeSerializers.register(ApplyEnchantmentTokenRecipe.Serializer.INSTANCE);
        RecipeSerializers.register(ApplyGearSoulRecipe.Serializer.INSTANCE);
        RecipeSerializers.register(GearSoulRecipe.Serializer.INSTANCE);
        RecipeSerializers.register(ModifySoulUrnRecipe.Serializer.INSTANCE);
        RecipeSerializers.register(SoulUrnRecipe.Serializer.INSTANCE);

        if (SilentGems.isDevBuild()) {
            MinecraftForge.EVENT_BUS.addListener(ModRecipes::onPlayerJoinServer);
        }
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
