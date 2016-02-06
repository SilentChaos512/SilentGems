package net.silentchaos512.gems.client.gui.button;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import net.silentchaos512.gems.client.gui.GuiGemsManual;
import net.silentchaos512.gems.client.renderers.RenderHelper;
import net.silentchaos512.gems.core.util.LocalizationHelper;

public class GuiButtonBack extends GuiButtonManual {

  public GuiButtonBack(int id, int posX, int posY, int width, int height, String str) {

    super(id, posX, posY, width, height, str);
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY) {

    if(enabled) {
      field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
      int k = getHoverState(field_146123_n);

      mc.renderEngine.bindTexture(GuiGemsManual.texture);
      GL11.glColor4f(1F, 1F, 1F, 1F);
      drawTexturedModalRect(xPosition, yPosition, 36, k == 2 ? 180 : 189, 18, 9);

      List<String> tooltip = getTooltip();
      int tooltipY = (tooltip.size() - 1) * 10;
      if(k == 2)
        RenderHelper.renderTooltip(mouseX, mouseY + tooltipY, tooltip);
    }
  }

  public List<String> getTooltip() {

    return Arrays.asList(LocalizationHelper.getGuiString("Back"));
  }
}
