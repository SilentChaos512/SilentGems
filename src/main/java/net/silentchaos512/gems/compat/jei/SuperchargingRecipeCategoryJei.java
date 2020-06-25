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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.api.parts.IGearPart;
import net.silentchaos512.gems.api.chaos.ChaosEmissionRate;
import net.silentchaos512.gems.block.supercharger.SuperchargerTileEntity;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsEnchantments;
import net.silentchaos512.gems.init.GemsTags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SuperchargingRecipeCategoryJei implements IRecipeCategory<SuperchargingRecipeCategoryJei.Recipe> {
    private static final int GUI_START_X = 0;
    private static final int GUI_START_Y = 117;
    private static final int GUI_WIDTH = 120;
    private static final int GUI_HEIGHT = 48;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final String localizedName;

    public SuperchargingRecipeCategoryJei(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(SilentGemsPlugin.GUI_TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(GemsBlocks.SUPERCHARGER));
        arrow = guiHelper.drawableBuilder(SilentGemsPlugin.GUI_TEXTURE, 73, 14, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        localizedName = I18n.format("category.silentgems.supercharging");
    }

    @Override
    public ResourceLocation getUid() {
        return SilentGemsPlugin.SUPERCHARGING;
    }

    @Override
    public Class<? extends SuperchargingRecipeCategoryJei.Recipe> getRecipeClass() {
        return SuperchargingRecipeCategoryJei.Recipe.class;
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
    public void setIngredients(SuperchargingRecipeCategoryJei.Recipe recipe, IIngredients ingredients) {
        Tag<Item> catalystTag = recipe.getCatalystTag();
        if (catalystTag == null) return;
        Ingredient partMaterial = recipe.part.getMaterials().getNormal();
        ingredients.setInputIngredients(Arrays.asList(
                partMaterial,
                Ingredient.fromTag(catalystTag)
        ));
        List<ItemStack> outputs = new ArrayList<>();
        Arrays.stream(partMaterial.getMatchingStacks()).forEach(stack -> {
            ItemStack copy = stack.copy();
            copy.addEnchantment(GemsEnchantments.supercharged, recipe.tier);
            outputs.add(copy);
        });
        ingredients.setOutputLists(VanillaTypes.ITEM, Collections.singletonList(outputs));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SuperchargingRecipeCategoryJei.Recipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 19, 0);
        itemStacks.init(1, true, 19, 20);
        itemStacks.init(2, false, 78, 10);

        itemStacks.set(ingredients);
    }

    @Override
    public void draw(SuperchargingRecipeCategoryJei.Recipe recipe, double mouseX, double mouseY) {
        arrow.draw(43, 10);

        Minecraft mc = Minecraft.getInstance();
        // Chaos emission rate
        int chaos = SuperchargerTileEntity.getEmissionRate(3, recipe.tier);
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(chaos);
        String str = emissionRate.getEmissionText(chaos).getFormattedText();
        mc.fontRenderer.drawStringWithShadow(str, 1, GUI_HEIGHT - mc.fontRenderer.FONT_HEIGHT + 1, -1);
    }

    static final class Recipe {
        final IGearPart part;
        final int tier;

        Recipe(IGearPart part, int tier) {
            this.part = part;
            this.tier = tier;
        }

        @Nullable
        Tag<Item> getCatalystTag() {
            if (tier == 1) return GemsTags.Items.CHARGING_AGENT_TIER1;
            if (tier == 2) return GemsTags.Items.CHARGING_AGENT_TIER2;
            if (tier == 3) return GemsTags.Items.CHARGING_AGENT_TIER3;
            return null;
        }
    }
}
