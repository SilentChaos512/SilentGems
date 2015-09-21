package net.silentchaos512.gems.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.client.particle.EntityParticleFXChaosTransfer;
import net.silentchaos512.gems.client.renderers.ModRenderers;

public class ClientProxy extends CommonProxy {

  @Override
  public void registerRenderers() {

    registerRenderersBlocks();
    registerRenderersItems();
    registerRenderersMobs();
    registerRenderersProjectiles();
    
    //added by M4thG33k
    ModRenderers.init();
  }

  private void registerRenderersProjectiles() {

    // TODO Auto-generated method stub

  }

  private void registerRenderersMobs() {

    // TODO Auto-generated method stub

  }

  private void registerRenderersItems() {

    // TODO Auto-generated method stub

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

  @Override
  public void doNEICheck(ItemStack stack) {

    // if (Minecraft.getMinecraft().thePlayer != null) {
    // Iterator modIT = Loader.instance().getModList().iterator();
    // ModContainer modc;
    // while (modIT.hasNext()) {
    // modc = (ModContainer) modIT.next();
    // if ("Not Enough Items".equals(modc.getName().trim())) {
    // codechicken.nei.api.API.hideItem(new ItemStack(SRegistry.getBlock(Names.FLUFFY_PLANT)));
    // for (int i = 0; i < EnumGem.all().length; ++i) {
    // codechicken.nei.api.API.hideItem(new ItemStack(SRegistry.getBlock(Names.GEM_LAMP_LIT), 1, i));
    // }
    // return;
    // }
    // }
    // }
  }

  @Override
  public void spawnParticles(String type, World world, double x, double y, double z, double motionX,
      double motionY, double motionZ) {

    EntityFX particleFX = null;

    if (type.equals("chaosTransfer")) {
      particleFX = new EntityParticleFXChaosTransfer(world, x, y, z, motionX, motionY, motionZ);
    }

    if (particleFX != null) {
      Minecraft.getMinecraft().effectRenderer.addEffect(particleFX);
    }
  }
}
