package net.silentchaos512.gems.compat.rei;

import me.shedaniel.rei.api.IREIPlugin;
import me.shedaniel.rei.api.ItemRegistry;
import me.shedaniel.rei.api.REIPlugin;
import me.shedaniel.rei.api.RecipeHelper;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.crafting.tokenenchanter.TokenEnchanterRecipeManager;

@IREIPlugin(identifier = SilentGems.RESOURCE_PREFIX + "default_plugin")
public class ReiPluginGems implements REIPlugin {
    public static final ResourceLocation TOKEN_ENCHANTING = SilentGems.getId("plugins/token_enchanting");

    @Override
    public void registerItems(ItemRegistry itemRegisterer) {
        SilentGems.LOGGER.debug("REI registerItems");
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        SilentGems.LOGGER.debug("REI registerPluginCategories");

        recipeHelper.registerCategory(new TokenEnchanterRecipeCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        SilentGems.LOGGER.debug("REI registerRecipeDisplays");

        TokenEnchanterRecipeManager.getValues().forEach(recipe ->
                recipeHelper.registerDisplay(TOKEN_ENCHANTING, new TokenEnchanterRecipeDisplay(recipe)));
    }

    @Override
    public void registerSpeedCraft(RecipeHelper recipeHelper) {
        SilentGems.LOGGER.debug("REI registerSpeedCraft");

        // TODO: Speed crafting (nice to have, but low priority)
    }
}
