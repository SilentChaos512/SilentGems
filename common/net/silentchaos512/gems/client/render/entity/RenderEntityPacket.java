package net.silentchaos512.gems.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket;

public class RenderEntityPacket extends Render<EntityChaosNodePacket> {

  public static final ResourceLocation TEXTURE = new ResourceLocation(SilentGems.MOD_ID,
      "textures/effects/ChaosOrb.png");

  public RenderEntityPacket() {

    super(Minecraft.getMinecraft().getRenderManager());
  }

  @Override
  protected ResourceLocation getEntityTexture(EntityChaosNodePacket entity) {

    return TEXTURE;
  }

}
