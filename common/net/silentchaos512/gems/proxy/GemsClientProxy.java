package net.silentchaos512.gems.proxy;

import org.apache.commons.lang3.NotImplementedException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiChaosBar;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.client.render.ModBlockRenderers;
import net.silentchaos512.gems.client.render.entity.RenderChaosProjectile;
import net.silentchaos512.gems.client.render.entity.RenderEntityChaosTransfer;
import net.silentchaos512.gems.client.render.particle.EntityFXChaos;
import net.silentchaos512.gems.entity.EntityChaosProjectile;
import net.silentchaos512.gems.entity.EntityChaosTransfer;
import net.silentchaos512.gems.event.GemsClientEvents;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.SRegistry;

public class GemsClientProxy extends net.silentchaos512.gems.proxy.GemsCommonProxy {

  @Override
  public void preInit(SRegistry registry) {

    super.preInit(registry);
    OBJLoader.INSTANCE.addDomain(SilentGems.MOD_ID);
    FMLCommonHandler.instance().bus().register(KeyTracker.INSTANCE);
    MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
    MinecraftForge.EVENT_BUS.register(new GemsClientEvents());
    MinecraftForge.EVENT_BUS.register(GuiChaosBar.INSTANCE);
    MinecraftForge.EVENT_BUS.register(ModItems.toolRenderHelper);
    registry.clientPreInit();
  }

  @Override
  public void init(SRegistry registry) {

    super.init(registry);
    registry.clientInit();
    registerRenderers();
    registerColorHandlers();
  }

  @Override
  public void postInit(SRegistry registry) {

    super.postInit(registry);
    registry.clientPostInit();
  }

  private void registerRenderers() {

    ModBlockRenderers.init();

//    RenderingRegistry.registerEntityRenderingHandler(EntityChaosProjectile.class,
//        new RenderDragonFireball(Minecraft.getMinecraft().getRenderManager()));
    RenderingRegistry.registerEntityRenderingHandler(EntityChaosProjectile.class,
        new RenderChaosProjectile());
    RenderingRegistry.registerEntityRenderingHandler(EntityChaosTransfer.class,
        new RenderEntityChaosTransfer());
  }

  private void registerColorHandlers() {

    ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        return ToolHelper.getColorForPass(stack, tintIndex);
      };
    }, ModItems.tools.toArray(new Item[ModItems.tools.size()]));

    itemColors.registerItemColorHandler(new IItemColor() {

      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {

        return stack.getItemDamage() > 15 ? 0x999999 : 0xFFFFFF;
      }
    }, ModItems.gemShard);
  }

  // Particles

  @Override
  public void spawnParticles(EnumModParticles type, int color, World world, double x, double y,
      double z, double motionX, double motionY, double motionZ) {

    EntityFX fx = null;

    float r = ((color >> 16) & 255) / 255f;
    float g = ((color >> 8) & 255) / 255f;
    float b = (color & 255) / 255f;

    switch (type) {
      case CHAOS:
        fx = new EntityFXChaos(world, x, y, z, motionX, motionY, motionZ, 2.0f, 25, r, g, b);
        break;
      case CHAOS_PROJECTILE_BODY:
        fx = new EntityFXChaos(world, x, y, z, 0f, 0f, 0f, 3.0f, 1, r, g, b);
        break;
      default:
        throw new NotImplementedException("Unknown particle type: " + type);
    }

    if (fx != null) {
      Minecraft.getMinecraft().effectRenderer.addEffect(fx);
    }
  }

  @Override
  public int getParticleSettings() {

    return Minecraft.getMinecraft().gameSettings.particleSetting;
  }

  @Override
  public EntityPlayer getClientPlayer() {

    return Minecraft.getMinecraft().thePlayer;
  }
}
