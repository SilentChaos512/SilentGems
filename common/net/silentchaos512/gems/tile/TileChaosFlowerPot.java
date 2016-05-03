package net.silentchaos512.gems.tile;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.lib.util.Color;

public class TileChaosFlowerPot extends TileEntityFlowerPot implements ITickable {

  public static final int TRY_LIGHT_DELAY = 80;
  public static final int LIGHT_DISTANCE = 8;

  int ticksExisted = 0;

  @Override
  public void update() {

    final int delay = ticksExisted < 600 ? 10 : TRY_LIGHT_DELAY;
    if (++ticksExisted % delay == 0) {
      boolean result = tryPlacePhantomLight();
    }
  }

  private boolean tryPlacePhantomLight() {

    if (worldObj.isRemote || getFlowerItemStack() == null) {
      return false;
    }

    Random rand = SilentGems.instance.random;

    // final int step = ticksExisted / TRY_LIGHT_DELAY - 1;
    final boolean longRange = rand.nextFloat() < 0.5f; // step > 7
    // SilentGems.instance.logHelper.debug(ticksExisted, step, longRange);
    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();
    int dist = 8 + (longRange ? 6 : 0);

    // Select a random angle to rotate target position around center.
    final int k = longRange ? 8 : 4;
    final int angleFactor = rand.nextInt(2 * k);
    Vec3d vec = new Vec3d(dist, 0, 0);
    // vec = vec.rotateYaw(rand.nextInt(2 * k) / (float) k * (float) Math.PI);
    // final int angleFactor = step - (longRange ? 8 : 0);
    vec = vec.rotateYaw(angleFactor / (float) k * (float) Math.PI);

    x += Math.round(vec.xCoord);
    y += 2; // rand.nextInt(5) - 2;
    z += Math.round(vec.zCoord);

    MutableBlockPos tryPos = new MutableBlockPos(x, y, z);

    // Debug particles
    if (SilentGems.proxy.isClientPlayerHoldingDebugItem()) {
      // Debug particles: try position
      Color debugColor = new Color(0.4f, 0f, 1f);
      for (int i = 0; i < 100; ++i) {
        SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS, debugColor, worldObj, x + 0.5, y,
            z + 0.5, 0.005f * rand.nextGaussian(), 0.25f * rand.nextGaussian(),
            0.005f * rand.nextGaussian());
      }
      // Debug particles: ring
      for (float f = 0; f < 2 * Math.PI; f += Math.PI / 32) {
        Vec3d v = new Vec3d(dist, 0, 0).rotateYaw(f);
        SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS, debugColor, worldObj,
            pos.getX() + 0.5 + v.xCoord, pos.getY() + 0.5, pos.getZ() + 0.5 + v.zCoord, 0, 0, 0);
      }
    }

    if (canPlacePhantomLightAt(tryPos)) {
      placePhantomLightAt(tryPos);
      return true;
    }

    for (int ty = y + 1; ty > y - 2; --ty) {
      for (int tx = x - 1; tx < x + 2; ++tx) {
        for (int tz = z - 1; tz < z + 2; ++tz) {
          tryPos.set(tx, ty, tz);
          if (canPlacePhantomLightAt(tryPos)) {
            placePhantomLightAt(tryPos);
            return true;
          }
        }
      }
    }

    return false;
  }

  private boolean canPlacePhantomLightAt(BlockPos target) {

    // SilentGems.instance.logHelper.debug(worldObj.getLightFor(EnumSkyBlock.BLOCK, pos));
    return worldObj.isAirBlock(target) && worldObj.getLightFor(EnumSkyBlock.BLOCK, target) < 10;
  }

  private void placePhantomLightAt(BlockPos target) {

    worldObj.setBlockState(target, ModBlocks.phantomLight.getDefaultState());
    TileEntity tile = worldObj.getTileEntity(target);
    if (tile instanceof TilePhantomLight) {
      TilePhantomLight tileLight = (TilePhantomLight) tile;
      tileLight.setSpawnerPos(this.pos);
    }
  }

  @Override
  public void setFlowerPotData(Item potItem, int potData) {

    super.setFlowerPotData(potItem, potData);
    ticksExisted = 0;
  }
}
