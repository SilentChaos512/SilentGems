package net.silentchaos512.gems.compat.rei;

import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.gui.widget.IWidget;
import me.shedaniel.rei.gui.widget.ItemSlotWidget;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.supercharger.SuperchargerBlock;
import net.silentchaos512.gems.block.supercharger.SuperchargerTileEntity;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class SuperchargerRecipeCategory implements RecipeCategory<SuperchargerRecipeDisplay> {
    private static final ResourceLocation DISPLAY_TEXTURE = SilentGems.getId("textures/gui/recipe_display.png");

    @Override
    public ResourceLocation getLocation() {
        return ReiPluginGems.SUPERCHARGING;
    }

    @Override
    public ItemStack getCategoryIcon() {
        return new ItemStack(SuperchargerBlock.INSTANCE.get());
    }

    @Override
    public String getCategoryName() {
        return I18n.format("category.silentgems.supercharging");
    }

    @Override
    public List<IWidget> setupDisplay(Supplier<SuperchargerRecipeDisplay> recipeDisplaySupplier, Rectangle bounds) {
        Point startPoint = new Point((int) bounds.getCenterX() - 41, (int) bounds.getCenterY() - 27);
        List<IWidget> widgets = new LinkedList<>(Collections.singletonList(new RecipeBaseWidget(bounds) {
            @Override
            public void draw(int mouseX, int mouseY, float partialTicks) {
                super.draw(mouseX, mouseY, partialTicks);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderHelper.disableStandardItemLighting();
                Minecraft.getInstance().getTextureManager().bindTexture(DISPLAY_TEXTURE);
                drawTexturedModalRect(startPoint.x, startPoint.y, 0, 0, 73, 37);
                // Arrow
                int width = MathHelper.ceil((System.currentTimeMillis() / 250 % 24d) / 1f);
                drawTexturedModalRect(startPoint.x + 24, startPoint.y + 1, 73, 14, width, 17);
            }
        }));

        List<List<ItemStack>> input = recipeDisplaySupplier.get().getInput();
        // Input
        widgets.add(new ItemSlotWidget(startPoint.x + 1, startPoint.y + 1, input.get(0), true, true, true));
        // Catalyst
        widgets.add(new ItemSlotWidget(startPoint.x + 28, startPoint.y + 20, input.get(1), true, true, true));
        // Output
        widgets.add(new ItemSlotWidget(startPoint.x + 56, startPoint.y + 1, recipeDisplaySupplier.get().getOutput(), false, true, true));
        // Labels
        int x = startPoint.x + 40;
        // TODO: Assuming part tier of 3, maybe fix this later?
        int chaosGenerated = SuperchargerTileEntity.getEmissionRate(3, recipeDisplaySupplier.get().getTier());
        widgets.add(new ChaosEmissionLabelWidget(x, startPoint.y + 38, chaosGenerated));
        return widgets;
    }
}
