package net.silentchaos512.gems.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.api.chaos.ChaosEmissionRate;
import net.silentchaos512.gems.block.altar.AltarBlock;
import net.silentchaos512.gems.crafting.recipe.AltarTransmutationRecipe;
import net.silentchaos512.gems.util.TextUtil;
import net.silentchaos512.lib.util.TextRenderUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransmutationAltarRecipeCategoryJei implements IRecipeCategory<AltarTransmutationRecipe> {
    private static final int GUI_START_X = 0;
    private static final int GUI_START_Y = 117;
    private static final int GUI_WIDTH = 119;
    private static final int GUI_HEIGHT = 47;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final String localizedName;

    public TransmutationAltarRecipeCategoryJei(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(SilentGemsPlugin.GUI_TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(AltarBlock.INSTANCE.get()));
        arrow = guiHelper.drawableBuilder(SilentGemsPlugin.GUI_TEXTURE, 73, 14, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        localizedName = I18n.format("category.silentgems.altar_transmutation");
    }

    @Override
    public ResourceLocation getUid() {
        return SilentGemsPlugin.ALTAR_TRANSMUTATION;
    }

    @Override
    public Class<? extends AltarTransmutationRecipe> getRecipeClass() {
        return AltarTransmutationRecipe.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(AltarTransmutationRecipe recipe, IIngredients ingredients) {
        List<Ingredient> inputs = new ArrayList<>();
        inputs.add(recipe.getIngredient());
        inputs.add(recipe.getCatalyst());
        ingredients.setInputIngredients(inputs);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AltarTransmutationRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 19, 0);
        itemStacks.init(1, true, 19, 20);
        itemStacks.init(2, false, 78, 10);

        itemStacks.set(0, Arrays.asList(recipe.getIngredient().getMatchingStacks()));
        itemStacks.set(1, Arrays.asList(recipe.getCatalyst().getMatchingStacks()));
        itemStacks.set(2, recipe.getRecipeOutput());
    }

    @Override
    public void draw(AltarTransmutationRecipe recipe, double mouseX, double mouseY) {
        arrow.draw(43, 128 - GUI_START_Y);

        Minecraft mc = Minecraft.getInstance();
        // Chaos emission rate
        int chaos = recipe.getChaosGenerated();
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(chaos);
        String str = emissionRate.getEmissionText(chaos).getFormattedText();
        TextRenderUtils.renderScaled(mc.fontRenderer, str, 1, GUI_HEIGHT - mc.fontRenderer.FONT_HEIGHT + 2, 0.8f, -1, true);

        // Catalyst label
        String catalystText = TextUtil.translate("jei", "recipe.altar.catalystNotConsumed").getFormattedText();
        TextRenderUtils.renderScaled(mc.fontRenderer, catalystText, 38, GUI_HEIGHT - mc.fontRenderer.FONT_HEIGHT - 5, 0.5f, -1, true);
    }
}
