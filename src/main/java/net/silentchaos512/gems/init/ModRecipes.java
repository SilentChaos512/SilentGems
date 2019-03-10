package net.silentchaos512.gems.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.crafting.ApplyEnchantmentTokenRecipe;
import net.silentchaos512.gems.crafting.ModifySoulUrnRecipe;
import net.silentchaos512.gems.crafting.SoulGemIngredient;
import net.silentchaos512.gems.crafting.SoulUrnRecipe;
import net.silentchaos512.lib.util.GameUtil;

public final class ModRecipes {
    private ModRecipes() {}

    public static void init() {
        SoulGemIngredient.Serializer.register();

        RecipeSerializers.register(ApplyEnchantmentTokenRecipe.Serializer.INSTANCE);
        RecipeSerializers.register(ModifySoulUrnRecipe.Serializer.INSTANCE);
        RecipeSerializers.register(SoulUrnRecipe.Serializer.INSTANCE);

        if (GameUtil.isDeobfuscated()) {
            //MinecraftForge.EVENT_BUS.addListener(ModRecipes::onPlayerJoinServer);
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
