package net.silentchaos512.gems.core.proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosPylon;
import net.silentchaos512.gems.tile.TileTeleporter;

public class CommonProxy {

  public CommonProxy() {

  }

  public void registerRenderers() {

  }

  public void registerTileEntities() {

    String prefix = "tile.silentgems:";
    GameRegistry.registerTileEntity(TileTeleporter.class, prefix + Names.TELEPORTER);
    GameRegistry.registerTileEntity(TileChaosAltar.class, prefix = Names.CHAOS_ALTAR);
    GameRegistry.registerTileEntity(TileChaosPylon.class, prefix + Names.CHAOS_PYLON);
  }

  public void registerKeyHandlers() {

  }

  public void doNEICheck(ItemStack stack) {

  }

  public void spawnParticles(String type, World world, double x, double y, double z, double motionX,
      double motionY, double motionZ) {

  }
}
