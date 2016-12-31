package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EntityChaosProjectile;

public class RenderChaosProjectile extends Render<EntityChaosProjectile> {

  protected RenderChaosProjectile(RenderManager renderManager) {

    super(renderManager);
  }

  // private static final ResourceLocation particleTextures = new ResourceLocation(
  // "textures/particle/particles.png");

  public static final ResourceLocation TEXTURE = new ResourceLocation(
      SilentGems.MODID, "textures/effects/chaosorb.png");

  @Override
  public void doRender(EntityChaosProjectile entity, double x, double y, double z, float entityYaw,
      float partialTicks) {

  }

  @Override
  protected ResourceLocation getEntityTexture(EntityChaosProjectile entity) {

    return TEXTURE;
  }

  public static class Factory implements IRenderFactory<EntityChaosProjectile> {

    @Override
    public Render<? super EntityChaosProjectile> createRenderFor(RenderManager manager) {

      return new RenderChaosProjectile(manager);
    }
  }
}
