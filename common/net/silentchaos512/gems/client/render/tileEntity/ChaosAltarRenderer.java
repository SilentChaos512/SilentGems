package net.silentchaos512.gems.client.render.tileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.client.render.handlers.ClientTickHandler;
import net.silentchaos512.gems.tile.TileChaosAltar;

import java.util.Random;

public class ChaosAltarRenderer extends TileEntitySpecialRenderer<TileChaosAltar> {

  public static Minecraft mc = Minecraft.getMinecraft();


  @Override
  public void renderTileEntityAt(TileChaosAltar te, double x, double y, double z,
      float partialTicks, int destroyStage) {

    ItemStack stack = te.getStackInSlot(0);
    if (stack!=null)
    {
      double worldTime = (te.getWorld()==null) ? 0 : (double)(ClientTickHandler.ticksInGame + partialTicks) + new Random(te.getPos().hashCode()).nextInt(360);

      GlStateManager.pushMatrix();
      GlStateManager.color(1f,1f,1f,1f);
      GlStateManager.scale(0.5,0.5,0.5);
      GlStateManager.translate(x+0.5,y+0.5+2,z+0.5);
      mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

      mc.getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND); //don't worry about the deprecation - it's fine in this context (the nice folks on IRC told me so)
      GlStateManager.popMatrix();
    }
  }
}
