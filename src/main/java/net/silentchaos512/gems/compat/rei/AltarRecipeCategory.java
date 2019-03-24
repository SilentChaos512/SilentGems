package net.silentchaos512.gems.compat.rei;

import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.gui.widget.IWidget;
import me.shedaniel.rei.gui.widget.ItemSlotWidget;
import me.shedaniel.rei.gui.widget.LabelWidget;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.altar.AltarBlock;
import net.silentchaos512.gems.crafting.altar.AltarRecipe;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class AltarRecipeCategory implements RecipeCategory<AltarRecipeDisplay> {
    private static final ResourceLocation DISPLAY_TEXTURE = SilentGems.getId("textures/gui/recipe_display.png");

    @Override
    public ResourceLocation getLocation() {
        return ReiPluginGems.ALTAR_TRANSMUTATION;
    }

    @Override
    public ItemStack getCategoryIcon() {
        return new ItemStack(AltarBlock.INSTANCE.get());
    }

    @Override
    public String getCategoryName() {
        return I18n.format("category.silentgems.altar_transmutation");
    }

    @Override
    public List<IWidget> setupDisplay(Supplier<AltarRecipeDisplay> recipeDisplaySupplier, Rectangle bounds) {
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

        AltarRecipe altarRecipe = recipeDisplaySupplier.get().getAltarRecipe();
        List<List<ItemStack>> input = recipeDisplaySupplier.get().getInput();
        // Input
        widgets.add(new ItemSlotWidget(startPoint.x + 1, startPoint.y + 1, input.get(0), true, true, true));
        // Catalyst
        widgets.add(new ItemSlotWidget(startPoint.x + 28, startPoint.y + 20, input.get(1), true, true, true) {
            @Override
            protected String getItemCountOverlay(ItemStack currentStack) {
                return altarRecipe.getCatalystConsumed() + "%";
            }
        });
        // Output
        widgets.add(new ItemSlotWidget(startPoint.x + 56, startPoint.y + 1, recipeDisplaySupplier.get().getOutput(), false, true, true) {
            @Override
            protected String getItemCountOverlay(ItemStack currentStack) {
                if (altarRecipe.getResult().getCount() > 1) {
                    return String.valueOf(altarRecipe.getResult().getCount());
                }
                return super.getItemCountOverlay(currentStack);
            }
        });
        // Labels
        int x = startPoint.x + 40;
        widgets.add(new ChaosEmissionLabelWidget(x, startPoint.y + 36, altarRecipe.getChaosGenerated()));
        ITextComponent text = new TextComponentTranslation("misc.silentgems.processTime", String.valueOf(altarRecipe.getProcessTime()));
        widgets.add(new LabelWidget(x, startPoint.y + 46, text.getFormattedText()));
        return widgets;
    }
}
