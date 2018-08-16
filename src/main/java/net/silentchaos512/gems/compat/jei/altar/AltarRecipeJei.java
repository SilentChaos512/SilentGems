package net.silentchaos512.gems.compat.jei.altar;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.recipe.altar.RecipeChaosAltar;

import javax.annotation.Nonnull;

public class AltarRecipeJei implements IRecipeWrapper {
    @Nonnull
    private final RecipeChaosAltar recipe;

    public AltarRecipeJei(@Nonnull RecipeChaosAltar recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients arg0) {
        arg0.setInputs(ItemStack.class, Lists.newArrayList(recipe.getInput(), recipe.getCatalyst()));
        arg0.setOutput(ItemStack.class, recipe.getOutput());
    }

    @Override
    public void drawInfo(@Nonnull Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (recipe.getCatalyst() != null) {
            FontRenderer fontRender = mc.fontRenderer;
            String str = SilentGems.i18n.translate("jei", "recipe.altar.catalyst");
            int width = fontRender.getStringWidth(str);
            fontRender.drawStringWithShadow(str, 40 - width - 2, 30, 0xFFFFFF);
        }
    }
}
