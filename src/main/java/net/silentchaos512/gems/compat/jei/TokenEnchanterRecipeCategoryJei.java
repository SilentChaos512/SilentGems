package net.silentchaos512.gems.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.silentchaos512.gems.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.gems.init.GemsBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokenEnchanterRecipeCategoryJei implements IRecipeCategory<TokenEnchanterRecipe> {
    private static final int GUI_START_X = 0;
    private static final int GUI_START_Y = 37;
    private static final int GUI_WIDTH = 133;
    private static final int GUI_HEIGHT = 50;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final String localizedName;

    public TokenEnchanterRecipeCategoryJei(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(SilentGemsPlugin.GUI_TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(GemsBlocks.TOKEN_ENCHANTER));
        arrow = guiHelper.drawableBuilder(SilentGemsPlugin.GUI_TEXTURE, 73, 14, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        localizedName = I18n.format("category.silentgems.token_enchanting");
    }

    @Override
    public ResourceLocation getUid() {
        return SilentGemsPlugin.TOKEN_ENCHANTING;
    }

    @Override
    public Class<? extends TokenEnchanterRecipe> getRecipeClass() {
        return TokenEnchanterRecipe.class;
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
    public void setIngredients(TokenEnchanterRecipe recipe, IIngredients ingredients) {
        List<Ingredient> inputs = new ArrayList<>();
        inputs.add(recipe.getToken());
        inputs.addAll(recipe.getIngredientMap().keySet());
        ingredients.setInputIngredients(inputs);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, TokenEnchanterRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 0, 10);
        itemStacks.init(1, true, 26, 0);
        itemStacks.init(2, true, 44, 0);
        itemStacks.init(3, true, 62, 0);
        itemStacks.init(4, true, 26, 18);
        itemStacks.init(5, true, 44, 18);
        itemStacks.init(6, true, 62, 18);
        itemStacks.init(7, false, 111, 10);

        itemStacks.set(0, Arrays.asList(recipe.getToken().getMatchingStacks()));
        List<List<ItemStack>> inputs = new ArrayList<>();
        recipe.getIngredientMap().forEach((ingredient, count) -> {
            List<ItemStack> list = Arrays.asList(ingredient.getMatchingStacks());
            list.forEach(stack -> stack.setCount(count));
            inputs.add(list);
        });
        for (int i = 0; i < inputs.size(); ++i) {
            itemStacks.set(i + 1, inputs.get(i));
        }
        itemStacks.set(7, recipe.getResult());
    }

    @Override
    public void draw(TokenEnchanterRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 81, 11);

        Minecraft mc = Minecraft.getInstance();
        // Chaos emission rate
        int chaos = recipe.getChaosGenerated();
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(chaos);
        String str = emissionRate.getEmissionText(chaos).getString();
        mc.fontRenderer.drawStringWithShadow(matrixStack, str, 1, GUI_HEIGHT - mc.fontRenderer.FONT_HEIGHT - 1, -1);
    }
}
