package net.silentchaos512.gems.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.api.recipe.altar.RecipeChaosAltar;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.inventory.ContainerChaosAltar;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.lib.SilentLib;

public class GuiChaosAltar extends GuiContainer {

  private static final ResourceLocation guiTextures = new ResourceLocation(
      "silentgems:textures/gui/chaosaltar.png");
  private TileChaosAltar tileAltar;

  public GuiChaosAltar(InventoryPlayer playerInventory, TileChaosAltar altar) {

    super(new ContainerChaosAltar(playerInventory, altar));
    this.tileAltar = altar;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int par1, int par2) {

    if (GemsConfig.DEBUG_MODE) {
      int recipeIndex = tileAltar.getField(2);
      RecipeChaosAltar recipe = recipeIndex >= 0
          && recipeIndex < RecipeChaosAltar.ALL_RECIPES.size()
              ? RecipeChaosAltar.ALL_RECIPES.get(recipeIndex) : null;

      String format = "%,d / %,d";
      // Chaos stored
      String line = String.format(format, tileAltar.getCharge(), tileAltar.getMaxCharge());
      this.fontRenderer.drawString("Chaos: " + line, 8, 6, 0x404040);

      // Transmute progress
      line = String.format(format, tileAltar.getField(1),
          recipe == null ? 0 : recipe.getChaosCost());
      this.fontRenderer.drawString("Trnsm: " + line, 8, 16, 0x404040);

      // Recipe info
      line = "Recipe: " + recipeIndex
          + (recipe == null ? "" : " (" + recipe.getOutput().getDisplayName() + ")");
      this.fontRenderer.drawString(line, 8, 26, 0x404040);
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(guiTextures);
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

    // Progress arrow
    RecipeChaosAltar recipe = tileAltar.getActiveRecipe();
    if (recipe != null) {
      int progress = tileAltar.getField(1);
      int totalTime = recipe.getChaosCost();
      int length = totalTime != 0 && progress > 0 && progress < totalTime
          ? progress * 24 / totalTime : 0;
      drawTexturedModalRect(k + 79, l + 34, 176, 14, length + 1, 16);
    }

    // Chaos stored
    int chaos = tileAltar.getField(0);
    int i1 = 24 * chaos / TileChaosAltar.MAX_CHAOS_STORED;
    drawTexturedModalRect(k + 79, l + 34, 176, 31, i1, 17);
  }
}
