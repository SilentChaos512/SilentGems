package net.silentchaos512.gems.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
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
      int recipeIndex = tileAltar.getField(2);
      RecipeChaosAltar recipe =
          recipeIndex >= 0 && recipeIndex < RecipeChaosAltar.ALL_RECIPES.size() ?
              RecipeChaosAltar.ALL_RECIPES.get(recipeIndex) : null;

      String format = "%s: %,d / %,d";
      // Chaos stored
      this.fontRenderer.drawString(
          String.format(format, "Chaos", tileAltar.getCharge(), tileAltar.getMaxCharge()), 8, 6,
          0x404040);

      // Transmute progress
      this.fontRenderer.drawString(String.format(format, "Trnsm", tileAltar.getField(1),
          recipe == null ? 0 : recipe.getChaosCost()), 8, 16, 0x404040);

      // Recipe info
      this.fontRenderer.drawString("Recipe: " + recipeIndex +
              (recipe == null ? "" : " (" + recipe.getOutput().getDisplayName() + ")"), 8, 26,
          0x404040);
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(guiTextures);
    int xPos = (this.width - this.xSize) / 2;
    int yPos = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

    // Progress arrow
    RecipeChaosAltar recipe = tileAltar.getActiveRecipe();
    if (recipe != null) {
      int progress = tileAltar.getField(1);
      int cost = recipe.getChaosCost();
      int length = cost != 0 && progress > 0 && progress < cost ? progress * 24 / cost : 0;
      drawTexturedModalRect(xPos + 79, yPos + 34, 176, 14, length + 1, 16);
    }

    // Chaos stored
    int chaos = tileAltar.getField(0);
    drawTexturedModalRect(xPos + 79, yPos + 34, 176, 31,
        24 * chaos / TileChaosAltar.MAX_CHAOS_STORED, 17);
  }
}
