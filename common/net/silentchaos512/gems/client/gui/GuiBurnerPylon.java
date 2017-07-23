package net.silentchaos512.gems.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.inventory.ContainerBurnerPylon;
import net.silentchaos512.gems.tile.TileChaosPylon;
import net.silentchaos512.lib.SilentLib;

public class GuiBurnerPylon extends GuiContainer {

  private static final ResourceLocation guiTextures = new ResourceLocation("silentgems:textures/gui/burnerpylon.png");
  private TileChaosPylon tilePylon;

  public GuiBurnerPylon(InventoryPlayer playerInventory, TileChaosPylon pylon) {
    super(new ContainerBurnerPylon(playerInventory, pylon));
    this.tilePylon = pylon;
  }

  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    if (SilentLib.getMCVersion() < 12) {
      super.drawScreen(mouseX, mouseY, partialTicks);
    } else {
      this.drawDefaultBackground();
      super.drawScreen(mouseX, mouseY, partialTicks);
      this.renderHoveredToolTip(mouseX, mouseY);
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int par1, int par2) {

    if (GemsConfig.DEBUG_MODE) {
      String str = "%,d / %,d";
      str = String.format(str, tilePylon.getCharge(), tilePylon.getMaxCharge()); // doesn't work
      fontRenderer.drawString(str, 5, 5, 0x404040);
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(guiTextures);
    int xPos = (this.width - this.xSize) / 2;
    int yPos = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

    if (tilePylon.isBurningFuel()) {
      int i1 = this.tilePylon.getBurnTimeRemainingScaled(13);
      this.drawTexturedModalRect(xPos + 81, yPos + 55 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
    }
  }
}
