package net.silentchaos512.gems.crafting.altar;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.util.ResourceManagerBase;

import javax.annotation.Nullable;

public final class AltarRecipeManager extends ResourceManagerBase<AltarRecipe> {
    public static final AltarRecipeManager INSTANCE = new AltarRecipeManager();

    private AltarRecipeManager() {
        super("silentgems/altar_recipes/", "AltarRecipeManager", SilentGems.LOGGER);
    }

    @Override
    public AltarRecipe deserialize(ResourceLocation id, JsonObject json) {
        return AltarRecipe.read(id, json);
    }

    @Nullable
    public static AltarRecipe getMatch(IInventory inv) {
        for (AltarRecipe recipe : INSTANCE.resources.values()) {
            if (recipe.matches(inv)) {
                return recipe;
            }
        }
        return null;
    }
}
