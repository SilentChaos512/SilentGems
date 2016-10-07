package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.silentchaos512.gems.entity.EntityThrownTomahawk;

public class RenderThrownTomahawk extends Render<EntityThrownTomahawk> {

  public RenderThrownTomahawk() {

    super(Minecraft.getMinecraft().getRenderManager());
    // TODO Auto-generated constructor stub
  }

  @Override
  protected ResourceLocation getEntityTexture(EntityThrownTomahawk entity) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void doRender(EntityThrownTomahawk entity, double x, double y, double z, float entityYaw,
      float partialTicks) {

    if (entity == null) return;

    ItemStack stack = entity.getThrownStack();
    if (stack == null || stack.getItem() == null)
      return;

    RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

    EntityItem entityitem = new EntityItem(entity.worldObj, entity.posX, entity.posY, entity.posZ,
        stack);
    entityitem.getEntityItem().stackSize = 1;
    entityitem.hoverStart = 0.0F;
    GlStateManager.pushMatrix();
    GlStateManager.disableLighting();

    Vec3d vec = new Vec3d(0.0, 0.25, 0.0);
    GlStateManager.translate(x, y, z);

    GlStateManager.rotate(90 - entity.throwYaw, 0, 1, 0);
    float rotation = entity.spin + (entity.inAir ? entity.SPIN_RATE * partialTicks : 0);
    GlStateManager.rotate(rotation, 0, 0, 1);

    GlStateManager.pushAttrib();
    RenderHelper.enableStandardItemLighting();
    itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
    RenderHelper.disableStandardItemLighting();
    GlStateManager.popAttrib();

    GlStateManager.enableLighting();
    GlStateManager.popMatrix();
  }
}
