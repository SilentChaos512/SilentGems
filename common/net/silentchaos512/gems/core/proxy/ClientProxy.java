package net.silentchaos512.gems.core.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.client.particle.EntityFXChaosCharge;
import net.silentchaos512.gems.client.particle.EntityFXChaosTrail;
import net.silentchaos512.gems.client.particle.EntityParticleFXChaosTransfer;
import net.silentchaos512.gems.client.renderers.ModRenderers;
import net.silentchaos512.gems.client.renderers.entity.RenderProjectileChaosOrb;
import net.silentchaos512.gems.client.renderers.tool.ToolItemRenderer;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.entity.projectile.EntityProjectileChaosOrb;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;

public class ClientProxy extends CommonProxy {

  public static final boolean USE_TOOL_IITEMRENDERER = false;

  @Override
  public void registerRenderers() {

    registerRenderersBlocks();
    registerRenderersItems();
    registerRenderersMobs();
    registerRenderersProjectiles();

    // added by M4thG33k
    ModRenderers.init();
  }

  private void registerRenderersProjectiles() {

    RenderingRegistry.registerEntityRenderingHandler(EntityProjectileChaosOrb.class,
        new RenderProjectileChaosOrb());
  }

  private void registerRenderersMobs() {

    // TODO Auto-generated method stub

  }

  private void registerRenderersItems() {

    if (USE_TOOL_IITEMRENDERER) {
      Item[] swords = SRegistry.getAllItemsOfType(GemSword.class);
      Item[] pickaxes = SRegistry.getAllItemsOfType(GemPickaxe.class);
      Item[] shovels = SRegistry.getAllItemsOfType(GemShovel.class);
      Item[] axes = SRegistry.getAllItemsOfType(GemAxe.class);
      Item[] hoes = SRegistry.getAllItemsOfType(GemHoe.class);
      Item[] sickles = SRegistry.getAllItemsOfType(GemSickle.class);

      registerToolRenderers(swords);
      registerToolRenderers(pickaxes);
      registerToolRenderers(shovels);
      registerToolRenderers(axes);
      registerToolRenderers(hoes);
      registerToolRenderers(sickles);
    }
  }

  private void registerToolRenderers(Item[] items) {

    for (Item item : items) {
      MinecraftForgeClient.registerItemRenderer(item, new ToolItemRenderer());
    }
  }

  private void registerRenderersBlocks() {

    // TODO Auto-generated method stub

  }

  @Override
  public void registerTileEntities() {

    super.registerTileEntities();
  }

  @Override
  public void registerKeyHandlers() {

    KeyTracker.init();
  }

  public static final String FX_CHAOS_TRANSFER = "chaosTransfer";
  public static final String FX_CHAOS_NO_ALTAR = "chaosNoAltar";
  public static final String FX_CHAOS_CHARGE = "chaosCharge";
  public static final String FX_CHAOS_TRAIL = "chaosTrail";

  @Override
  public void spawnParticles(String type, World world, double x, double y, double z, double motionX,
      double motionY, double motionZ) {

    spawnParticles(type, -1, world, x, y, z, motionX, motionY, motionZ);
  }

  @Override
  public void spawnParticles(String type, int color, World world, double x, double y, double z,
      double motionX, double motionY, double motionZ) {

    EntityFX particleFX = null;
    float red = (color >> 16 & 255) / 255f;
    float green = (color >> 8 & 255) / 255f;
    float blue = (color & 255) / 255f;

    if (type.equals(FX_CHAOS_TRANSFER)) {
      particleFX = new EntityParticleFXChaosTransfer(world, x, y, z, motionX, motionY, motionZ);
    } else if (type.equals(FX_CHAOS_NO_ALTAR)) {
      particleFX = new EntityParticleFXChaosTransfer(world, x, y, z, motionX, motionY, motionZ,
          EntityParticleFXChaosTransfer.MAX_SCALE, EntityParticleFXChaosTransfer.MAX_AGE, 1.0f,
          0.0f, 0.0f);
    } else if (type.equals(FX_CHAOS_CHARGE)) {
      particleFX = new EntityFXChaosCharge(world, x, y, z, motionX, motionY, motionZ);
    } else if (type.equals(FX_CHAOS_TRAIL)) {
      if (color > -1) {
        particleFX = new EntityFXChaosTrail(world, x, y, z, motionX, motionY, motionZ,
            EntityFXChaosTrail.MAX_SCALE, EntityFXChaosTrail.MAX_AGE * 4, red, green, blue);
      } else {
        particleFX = new EntityFXChaosTrail(world, x, y, z, motionX, motionY, motionZ);
      }
    }

    if (particleFX != null) {
      Minecraft.getMinecraft().effectRenderer.addEffect(particleFX);
    }
  }
}
