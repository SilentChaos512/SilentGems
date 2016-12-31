package net.silentchaos512.gems.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.inventory.ContainerMaterialGrader;
import net.silentchaos512.gems.tile.TileMaterialGrader;

public class GuiMaterialGrader extends GuiContainer {

  private static final ResourceLocation TEXTURE = new ResourceLocation(SilentGems.MODID,
      "textures/gui/materialgrader.png");
  private final InventoryPlayer playerInventory;
  private IInventory tileInventory;

  public GuiMaterialGrader(InventoryPlayer playerInventory, IInventory tileInventory) {

    super(new ContainerMaterialGrader(playerInventory, tileInventory));
    this.playerInventory = playerInventory;
    this.tileInventory = tileInventory;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    GlStateManager.color(1f, 1f, 1f, 1f);
    mc.getTextureManager().bindTexture(TEXTURE);

    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    int i1;

    // Progress arrow
    i1 = getAnalyzeProgress(24);
    drawTexturedModalRect(k + 49, l + 34, 176, 14, i1 + 1, 16);

    // Chaos stored
    int chaos = tileInventory.getField(0);
    i1 = 24 * chaos / TileMaterialGrader.MAX_CHARGE;
    drawTexturedModalRect(k + 49, l + 34, 176, 31, i1, 17);

    if (GemsConfig.DEBUG_MODE)
      drawDebugInfo();
  }

  private int getAnalyzeProgress(int scale) {

    int progress = tileInventory.getField(1);
    int totalTime = TileMaterialGrader.ANALYZE_TIME;
    return totalTime != 0 && progress > 0 && progress < totalTime ? progress * scale / totalTime
        : 0;
  }

  private void drawDebugInfo() {

    if (!(tileInventory instanceof TileMaterialGrader)) {
      return;
    }

    TileMaterialGrader tile = (TileMaterialGrader) tileInventory;
    FontRenderer fontRender = mc.fontRendererObj;
    int x = 5;
    int y = 5;
    int yIncrement = 10;
    int color = 0xFFFFFF;

    GlStateManager.pushMatrix();
    float scale = 1f;
    GlStateManager.scale(scale, scale, 1f);
    for (String str : tile.getDebugLines()) {
      fontRender.drawStringWithShadow(str, x, y, color);
      y += yIncrement;
    }
    GlStateManager.popMatrix();
  }
}
