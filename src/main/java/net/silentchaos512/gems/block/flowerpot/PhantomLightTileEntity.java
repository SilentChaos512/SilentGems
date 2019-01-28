package net.silentchaos512.gems.block.flowerpot;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.silentchaos512.gems.init.ModTileEntities;
import net.silentchaos512.lib.util.TimeUtils;

public class PhantomLightTileEntity extends TileEntity implements ITickable {
    private static final int SPAWNER_CHECK_FREQUENCY = TimeUtils.ticksFromSeconds(60);

    private BlockPos spawnerPos = null;
    private boolean playerPlaced = false;
    private int ticksExisted = 0;

    public PhantomLightTileEntity() {
        super(ModTileEntities.PHANTOM_LIGHT.type());
    }

    @Override
    public void tick() {
        if (!world.isRemote && ++ticksExisted % SPAWNER_CHECK_FREQUENCY == 0) {
            if (shouldRemove()) {
                world.removeBlock(this.pos);
            }
        }
    }

    private boolean shouldRemove() {
        if (playerPlaced) return false;
        if (spawnerPos == null) return true;

        TileEntity tile = world.getTileEntity(spawnerPos);
        return tile == null || !(tile instanceof LuminousFlowerPotTileEntity);
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
