package net.silentchaos512.gems.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.client.particle.EntityParticleFXChaosTransfer;
import net.silentchaos512.gems.client.renderers.ModRenderers;
import net.silentchaos512.gems.client.renderers.tool.ToolItemRenderer;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.lib.EnumGem;

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

    // TODO Auto-generated method stub

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
    } else if (type.equals("chaosNoAltar")) {
      particleFX = new EntityParticleFXChaosTransfer(world, x, y, z, motionX, motionY, motionZ,
          1.0f, 15, 1.0f, 0.0f, 0.0f);
    }

    if (particleFX != null) {
      Minecraft.getMinecraft().effectRenderer.addEffect(particleFX);
    }
  }
}
