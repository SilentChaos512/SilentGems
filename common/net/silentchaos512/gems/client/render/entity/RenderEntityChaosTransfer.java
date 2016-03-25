package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EntityChaosTransfer;

public class RenderEntityChaosTransfer extends Render<EntityChaosTransfer> {

  public RenderEntityChaosTransfer() {

    this(Minecraft.getMinecraft().getRenderManager());
  }

  protected RenderEntityChaosTransfer(RenderManager renderManager) {

    super(renderManager);
  }

  public static final ResourceLocation TEXTURE = new ResourceLocation(
      SilentGems.MOD_ID.toLowerCase(), "textures/effects/ChaosOrb.png");

  @Override
  public void doRender(EntityChaosTransfer entity, double x, double y, double z, float entityYaw,
      float partialTicks) {

  }

  @Override
  protected ResourceLocation getEntityTexture(EntityChaosTransfer entity) {

    return TEXTURE;
  }
}
