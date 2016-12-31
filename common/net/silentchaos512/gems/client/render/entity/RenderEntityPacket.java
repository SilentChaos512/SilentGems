package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EntityChaosProjectile;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket;

public class RenderEntityPacket extends Render<EntityChaosNodePacket> {

  public static final ResourceLocation TEXTURE = new ResourceLocation(SilentGems.MODID,
      "textures/effects/chaosorb.png");

  public RenderEntityPacket(RenderManager manager) {

    super(manager);
  }

  @Override
  public void doRender(EntityChaosNodePacket entity, double x, double y, double z, float entityYaw,
      float partialTicks) {

  }

  @Override
  protected ResourceLocation getEntityTexture(EntityChaosNodePacket entity) {

    return TEXTURE;
  }

  public static class Factory implements IRenderFactory<EntityChaosNodePacket> {

    @Override
    public Render<? super EntityChaosNodePacket> createRenderFor(RenderManager manager) {

      return new RenderEntityPacket(manager);
    }
  }
}
