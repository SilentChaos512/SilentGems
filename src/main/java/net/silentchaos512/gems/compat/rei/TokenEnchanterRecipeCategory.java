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
import net.silentchaos512.gems.api.chaos.ChaosEmissionRate;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterBlock;
import net.silentchaos512.gems.crafting.tokenenchanter.TokenEnchanterRecipe;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class TokenEnchanterRecipeCategory implements RecipeCategory<TokenEnchanterRecipeDisplay> {
    private static final ResourceLocation DISPLAY_TEXTURE = SilentGems.getId("textures/gui/recipe_display.png");
    @Override
    public ResourceLocation getLocation() {
        return ReiPluginGems.TOKEN_ENCHANTING;
    }

    @Override
    public ItemStack getCategoryIcon() {
        return new ItemStack(TokenEnchanterBlock.INSTANCE.get());
    }

    @Override
    public String getCategoryName() {
        return I18n.format("category.silentgems.token_enchanting");
    }

    @Override
    public List<IWidget> setupDisplay(Supplier<TokenEnchanterRecipeDisplay> recipeDisplaySupplier, Rectangle bounds) {
        Point startPoint = new Point((int) bounds.getCenterX() - 41, (int) bounds.getCenterY() - 27);
        List<IWidget> widgets = new LinkedList<>(Collections.singletonList(new RecipeBaseWidget(bounds) {
            @Override
            public void draw(int mouseX, int mouseY, float partialTicks) {
                super.draw(mouseX, mouseY, partialTicks);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderHelper.disableStandardItemLighting();
                Minecraft.getInstance().getTextureManager().bindTexture(DISPLAY_TEXTURE);
                drawTexturedModalRect(startPoint.x - 25, startPoint.y, 0, 37, 133, 36);
                // Arrow
                int width = MathHelper.ceil((System.currentTimeMillis() / 250 % 24d) / 1f);
                drawTexturedModalRect(startPoint.x + 56, startPoint.y + 11, 73, 14, width, 17);
            }
        }));

        List<List<ItemStack>> input = recipeDisplaySupplier.get().getInput();
        // Token
        widgets.add(new ItemSlotWidget(startPoint.x - 24, startPoint.y + 11, input.get(0), true, true, true));
        // Ingredients
        for (int i = 1; i < input.size(); ++i) {
            int otherIndex = i - 1;
            int x = startPoint.x + 2 + 18 * (otherIndex % 3);
            int y = startPoint.y + 1 + 18 * (otherIndex / 3);
            widgets.add(new ItemSlotWidget(x, y, input.get(i), true, true, true) {
                @Override
                protected String getItemCountOverlay(ItemStack currentStack) {
                    return String.valueOf(currentStack.getCount());
                }
            });
        }
        // Output
        widgets.add(new ItemSlotWidget(startPoint.x + 88, startPoint.y + 11, recipeDisplaySupplier.get().getOutput(), false, true, true));
        // Labels
        int x = startPoint.x + 40;
        TokenEnchanterRecipe tokenRecipe = recipeDisplaySupplier.get().getTokenRecipe();
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(tokenRecipe.getChaosGenerated());
        widgets.add(new LabelWidget(x, startPoint.y + 36, emissionRate.getEmissionText().getFormattedText()));
        ITextComponent text = new TextComponentTranslation("misc.silentgems.processTime", String.valueOf(tokenRecipe.getProcessTime()));
        widgets.add(new LabelWidget(x, startPoint.y + 46, text.getFormattedText()));
        return widgets;
    }
}
