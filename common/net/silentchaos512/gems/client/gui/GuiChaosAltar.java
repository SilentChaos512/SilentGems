package net.silentchaos512.gems.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.inventory.ContainerChaosAltar;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileMaterialGrader;

public class GuiChaosAltar extends GuiContainer {

  private static final ResourceLocation guiTextures = new ResourceLocation(
      "silentgems:textures/gui/ChaosAltar.png");
  private final InventoryPlayer playerInventory;
  private TileChaosAltar tileAltar;

  public GuiChaosAltar(InventoryPlayer playerInventory, TileChaosAltar altar) {

    super(new ContainerChaosAltar(playerInventory, altar));
    this.playerInventory = playerInventory;
    this.tileAltar = altar;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int par1, int par2) {

    // String str = "%d / %d";
    // str = String.format(str, tileAltar.getCharge(), tileAltar.getMaxCharge());
    // this.fontRendererObj.drawString(str, 8, 6, 0x404040);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(guiTextures);
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

    // Chaos stored
    int chaos = tileAltar.getField(0);
    int i1 = 24 * chaos / TileChaosAltar.MAX_CHAOS_STORED;
    drawTexturedModalRect(k + 79, l + 34, 176, 31, i1, 17);
  }
}
