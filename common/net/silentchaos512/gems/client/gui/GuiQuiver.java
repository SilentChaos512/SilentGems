package net.silentchaos512.gems.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.inventory.ContainerQuiver;
import net.silentchaos512.gems.tile.TileMaterialGrader;

public class GuiQuiver extends GuiContainer {

  static final ResourceLocation TEXTURE = new ResourceLocation(
      SilentGems.RESOURCE_PREFIX + "textures/gui/quiver.png");

  private ContainerQuiver container;

  public GuiQuiver(ContainerQuiver containerIn) {

    super(containerIn);

    this.container = containerIn;
    // xSize = container.getWidth();
    // ySize = container.getHeight();
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    fontRenderer.drawString(
        SilentGems.localizationHelper
            .getLocalizedString(ModItems.quiver.getUnlocalizedName() + ".name"),
        container.getBorderSide() + 1, 6, 0x404040);
    int invTitleX = container.getPlayerInvXOffset() + 1;
    int invTitleY = container.getBorderTop() + container.getContainerInvHeight() + 3;
    // fontRenderer.drawString(I18n.format("container.inventory"), invTitleX, invTitleY, 0x404040);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    GlStateManager.color(1f, 1f, 1f, 1f);
    mc.getTextureManager().bindTexture(TEXTURE);

    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

    if (GemsConfig.DEBUG_MODE) {
      drawDebugInfo();
    }
  }

  private void drawDebugInfo() {

    // TODO
  }
}
