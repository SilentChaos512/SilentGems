package net.silentchaos512.gems.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;

@SideOnly(Side.CLIENT)
public class GuiCrosshairs extends GuiIngame {

  public static final GuiCrosshairs INSTANCE = new GuiCrosshairs(Minecraft.getMinecraft());

  public static final ResourceLocation TEXTURE_CROSSHAIRS = new ResourceLocation(SilentGems.MOD_ID,
      "textures/gui/Crosshairs.png");

  public GuiCrosshairs(Minecraft mcIn) {

    super(mcIn);
  }

  public void renderOverlay(RenderGameOverlayEvent event, int type) {

    GlStateManager.color(1f, 1f, 1f, 1f);
    mc.getTextureManager().bindTexture(TEXTURE_CROSSHAIRS);
//    mc.getTextureManager().bindTexture(icons);
    GlStateManager.enableBlend();
    renderCrosshairs(event.getPartialTicks(), event.getResolution(), type);
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
        GlStateManager.DestFactor.ZERO);
  }

  protected void renderCrosshairs(float partialTicks, ScaledResolution resolution, int type) {

//    SilentGems.instance.logHelper.derp();
    GameSettings gamesettings = this.mc.gameSettings;

    if (gamesettings.thirdPersonView == 0) {
      if (this.mc.playerController.isSpectator() && this.mc.pointedEntity == null) {
        RayTraceResult raytraceresult = this.mc.objectMouseOver;

        if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
          return;
        }

        BlockPos blockpos = raytraceresult.getBlockPos();

        net.minecraft.block.state.IBlockState state = this.mc.theWorld.getBlockState(blockpos);
        if (!state.getBlock().hasTileEntity(state)
            || !(this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory)) {
          return;
        }
      }

      int width = resolution.getScaledWidth();
      int height = resolution.getScaledHeight();

      if (gamesettings.showDebugInfo && !gamesettings.hideGUI
          && !this.mc.thePlayer.hasReducedDebug() && !gamesettings.reducedDebugInfo) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (width / 2), (float) (height / 2), this.zLevel);
        Entity entity = this.mc.getRenderViewEntity();
        GlStateManager.rotate(
            entity.prevRotationPitch
                + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks,
            -1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(
            entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks,
            0.0F, 1.0F, 0.0F);
        GlStateManager.scale(-1.0F, -1.0F, -1.0F);
        OpenGlHelper.renderDirections(10);
        GlStateManager.popMatrix();
      } else {
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
            GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO);
        GlStateManager.enableAlpha();
        this.drawTexturedModalRect(width / 2 - 7, height / 2 - 7, 0 + 16 * type, 0, 16, 16);

        if (this.mc.gameSettings.attackIndicator == 1) {
          mc.getTextureManager().bindTexture(ICONS);
          float f = this.mc.thePlayer.getCooledAttackStrength(0.0F);

          if (f < 1.0F) {
            int i = height / 2 - 7 + 16;
            int j = width / 2 - 7;
            int k = (int) (f * 17.0F);
            this.drawTexturedModalRect(j, i, 36, 94, 16, 4);
            this.drawTexturedModalRect(j, i, 52, 94, k, 4);
          }
        }
      }
    }
  }
}
