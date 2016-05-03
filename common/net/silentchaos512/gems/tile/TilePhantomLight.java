package net.silentchaos512.gems.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;

public class TilePhantomLight extends TileEntity implements ITickable {

  public static final int SPAWNER_CHECK_FREQUENCY = 600; // 1200?

  BlockPos spawnerPos = null;
  boolean playerPlaced = false;
  int ticksExisted = 0;

  @Override
  public void update() {

    if (++ticksExisted % SPAWNER_CHECK_FREQUENCY == 0 && !worldObj.isRemote) {
      if (!checkSpawnerStillExists()) {
        String line = "Phantom light at %s removed. Spawner at %s.";
        line = String.format(line, this.pos, spawnerPos);
        SilentGems.instance.logHelper.info(line);

        worldObj.setBlockToAir(this.pos);
      }
    }
  }

  private boolean checkSpawnerStillExists() {

    if (playerPlaced) {
      return true;
    }
    if (spawnerPos == null) {
      return false;
    }
    IBlockState state = worldObj.getBlockState(spawnerPos);
    TileEntity tile = worldObj.getTileEntity(spawnerPos);
    return state.getBlock() == ModBlocks.chaosFlowerPot && tile != null
        && tile instanceof TileChaosFlowerPot
        && ((TileChaosFlowerPot) tile).getFlowerItemStack() != null;
  }

  public void setSpawnerPos(BlockPos pos) {

    spawnerPos = pos;
  }

  public void setPlacedByPlayer(boolean value) {

    playerPlaced = value;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    spawnerPos = new BlockPos(compound.getInteger("spawnerX"), compound.getInteger("spawnerY"),
        compound.getInteger("spawnerZ"));
    playerPlaced = compound.getBoolean("placedByPlayer");
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setInteger("spawnerX", spawnerPos != null ? spawnerPos.getX() : 0);
    compound.setInteger("spawnerY", spawnerPos != null ? spawnerPos.getY() : 0);
    compound.setInteger("spawnerZ", spawnerPos != null ? spawnerPos.getZ() : 0);
    compound.setBoolean("placedByPlayer", playerPlaced);
  }
}
