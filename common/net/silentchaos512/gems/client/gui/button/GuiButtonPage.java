package net.silentchaos512.gems.client.gui.button;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import net.silentchaos512.gems.client.gui.GuiGemsManual;
import net.silentchaos512.gems.client.renderers.RenderHelper;
import net.silentchaos512.gems.core.util.LocalizationHelper;

public class GuiButtonPage extends GuiButtonManual {

  public static final String NEXT_PAGE = LocalizationHelper.getGuiString("NextPage");
  public static final String PREV_PAGE = LocalizationHelper.getGuiString("PrevPage");

  boolean right;

  public GuiButtonPage(int par1, int par2, int par3, boolean right) {

    super(par1, par2, par3, 18, 10, "");
    this.right = right;
  }

  @Override
  public void drawButton(Minecraft par1Minecraft, int par2, int par3) {

    if(enabled) {
      field_146123_n = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
      int k = getHoverState(field_146123_n);

      par1Minecraft.renderEngine.bindTexture(GuiGemsManual.texture);
      GL11.glColor4f(1F, 1F, 1F, 1F);
      drawTexturedModalRect(xPosition, yPosition, k == 2 ? 18 : 0, right ? 180 : 190, 18, 10);

      if(k == 2) {
        RenderHelper.renderTooltip(par2, par3, Arrays.asList(right ? NEXT_PAGE : PREV_PAGE));
      }
    }
  }
}
