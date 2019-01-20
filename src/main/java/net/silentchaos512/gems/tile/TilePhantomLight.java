package net.silentchaos512.gems.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TilePhantomLight extends TileEntity implements ITickable {
    private static final int SPAWNER_CHECK_FREQUENCY = 1200;

    private BlockPos spawnerPos = null;
    private boolean playerPlaced = false;
    private int ticksExisted = 0;

    public TilePhantomLight(TileEntityType<?> tileEntityTypeIn) {
        // FIXME
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (!world.isRemote && ++ticksExisted % SPAWNER_CHECK_FREQUENCY == 0) {
            if (!checkSpawnerStillExists()) {
                world.removeBlock(this.pos);
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
        IBlockState state = world.getBlockState(spawnerPos);
        TileEntity tile = world.getTileEntity(spawnerPos);
        return tile instanceof TileChaosFlowerPot;
    }

    void setSpawnerPos(BlockPos pos) {
        spawnerPos = pos;
    }

    public void setPlacedByPlayer(boolean value) {
        playerPlaced = value;
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        spawnerPos = new BlockPos(
                compound.getInt("spawnerX"),
                compound.getInt("spawnerY"),
                compound.getInt("spawnerZ"));
        playerPlaced = compound.getBoolean("placedByPlayer");
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        compound.setInt("spawnerX", spawnerPos != null ? spawnerPos.getX() : 0);
        compound.setInt("spawnerY", spawnerPos != null ? spawnerPos.getY() : 0);
        compound.setInt("spawnerZ", spawnerPos != null ? spawnerPos.getZ() : 0);
        compound.setBoolean("placedByPlayer", playerPlaced);
        return compound;
    }
}
