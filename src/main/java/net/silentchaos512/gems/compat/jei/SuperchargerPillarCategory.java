package net.silentchaos512.gems.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.block.supercharger.SuperchargerPillarStructure;
import net.silentchaos512.gems.init.GemsBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuperchargerPillarCategory implements IRecipeCategory<SuperchargerPillarStructure> {
    private static final int GUI_START_X = 0;
    private static final int GUI_START_Y = 87;
    private static final int GUI_WIDTH = 120;
    private static final int GUI_HEIGHT = 30;

    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public SuperchargerPillarCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(SilentGemsPlugin.GUI_TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(GemsBlocks.SUPERCHARGER));
        localizedName = I18n.format("category.silentgems.supercharger_pillar");
    }

    @Override
    public ResourceLocation getUid() {
        return SilentGemsPlugin.SUPERCHARGER_PILLAR;
    }

    @Override
    public Class<? extends SuperchargerPillarStructure> getRecipeClass() {
        return SuperchargerPillarStructure.class;
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
    public void setIngredients(SuperchargerPillarStructure recipe, IIngredients ingredients) {
        List<Ingredient> inputs = new ArrayList<>(recipe.getBlocks());
        ingredients.setInputIngredients(inputs);
        ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(GemsBlocks.SUPERCHARGER));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SuperchargerPillarStructure recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        for (int i = 0; i < 5; ++i) {
            itemStacks.init(i, true, 1 + 16 * i, 1);
        }

        List<List<ItemStack>> inputs = new ArrayList<>();
        recipe.getBlocks().forEach(ingredient -> {
            List<ItemStack> list = Arrays.asList(ingredient.getMatchingStacks());
            inputs.add(list);
        });
        for (int i = 0; i < inputs.size(); ++i) {
            itemStacks.set(i, inputs.get(i));
        }
    }

    @Override
    public void draw(SuperchargerPillarStructure recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        // Tier
        int tier = recipe.getTier();
        String str = I18n.format("block.silentgems.supercharger.tier", String.valueOf(tier));
        mc.fontRenderer.drawStringWithShadow(matrixStack, str, 2, GUI_HEIGHT - mc.fontRenderer.FONT_HEIGHT - 1, -1);
    }
}
