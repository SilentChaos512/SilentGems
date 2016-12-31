package net.silentchaos512.gems.client.render.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.tile.TileMaterialGrader;

public class RenderTileMaterialGrader extends TileEntitySpecialRenderer<TileMaterialGrader> {

  @Override
  public void renderTileEntityAt(TileMaterialGrader te, double x, double y, double z,
      float partialTicks, int destroyStage) {

    if (te == null || te.getWorld() == null || !te.getWorld().isBlockLoaded(te.getPos(), false))
      return;

    ItemStack input = te.getStackInSlot(0);
    if (input != null) {
      World world = te.getWorld();
      IBlockState state = world.getBlockState(te.getPos());
      int meta = state.getBlock().getMetaFromState(state);
      GlStateManager.pushMatrix();
      GlStateManager.translate(x + 0.5, y + 0.3, z + 0.5);
      renderItem(world, input, -90 * meta);
      GlStateManager.popMatrix();
    }
  }

  private void renderItem(World world, ItemStack stack, int rotationYaw) {

    RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
    if (stack != null) {
      // GlStateManager.translate(0.5, 0.5, 0.5);
      EntityItem entityitem = new EntityItem(world, 0.0D, 0.0D, 0.0D, stack.copy());
      entityitem.getEntityItem().setCount(1);
      entityitem.hoverStart = 0.0F;
      GlStateManager.pushMatrix();
      GlStateManager.disableLighting();

      Vec3d vec = new Vec3d(0.0, 0.0, 0.0);
      GlStateManager.translate(vec.xCoord, vec.yCoord, vec.zCoord);
      GlStateManager.rotate(rotationYaw, 0.0F, 1.0F, 0);
      GlStateManager.rotate(-90, 1.0F, 0.0F, 0);

      float scale = 0.5f;
      GlStateManager.scale(scale, scale, scale);
      GlStateManager.pushAttrib();
      RenderHelper.enableStandardItemLighting();
      itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.popAttrib();

      GlStateManager.enableLighting();
      GlStateManager.popMatrix();
    }
  }
}
