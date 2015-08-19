package net.silentchaos512.gems.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.inventory.ContainerBurnerPylon;
import net.silentchaos512.gems.tile.TileChaosPylon;

public class GuiBurnerPylon extends GuiContainer {

  private static final ResourceLocation guiTextures = new ResourceLocation(
      "silentgems:textures/gui/BurnerPylon.png");
  private final InventoryPlayer playerInventory;
  private TileChaosPylon tilePylon;

  public GuiBurnerPylon(InventoryPlayer playerInventory, TileChaosPylon pylon) {

    super(new ContainerBurnerPylon(playerInventory, pylon));
    this.playerInventory = playerInventory;
    this.tilePylon = pylon;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(guiTextures);
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

    if (tilePylon.isBurningFuel()) {
      int i1 = this.tilePylon.getBurnTimeRemainingScaled(13);
      this.drawTexturedModalRect(k + 81, l + 55 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
    }
  }
}
