package net.silentchaos512.gems.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumLightType;
import net.silentchaos512.gems.SilentGems;

import javax.annotation.Nonnull;
import java.util.Random;

public class TileChaosFlowerPot extends TileEntity implements ITickable {
    public static final int TRY_LIGHT_DELAY = 80;
    public static final int LIGHT_DISTANCE = 8;
    public static final int TE_SEARCH_RADIUS = 7;

    int ticksExisted = 0;
    /**
     * Used for rendering (see RenderTileChaosFlowerPot).
     */
//    @SyncVariable(name = "flower")
    int flowerId = -1;

    public TileChaosFlowerPot(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (ticksExisted == 0) {
            ItemStack stack = getFlowerItemStack();
//            if (!stack.isEmpty())
//                flowerId = stack.getItemDamage();

            // Add a random "salt" value to ticksExisted, so all flower pots don't
            // try to place lights at the same time.
            ticksExisted += SilentGems.random.nextInt(300);
        }

        final int delay = ticksExisted < 600 ? 10 : TRY_LIGHT_DELAY;
        if (++ticksExisted % delay == 0) {
            tryPlacePhantomLight();
        }
    }

    private boolean tryPlacePhantomLight() {
        if (world.isRemote || getFlowerItemStack().isEmpty()) {
            return false;
        }

        Random rand = SilentGems.random;

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

        x += Math.round(vec.x);
        y += 2; // rand.nextInt(5) - 2;
        z += Math.round(vec.z);

        MutableBlockPos tryPos = new MutableBlockPos(x, y, z);

        // Debug particles
//        if (SilentGems.proxy.isClientPlayerHoldingDebugItem()) {
//            // Debug particles: try position
//            Color debugColor = new Color(0.4f, 0f, 1f);
//            for (int i = 0; i < 100; ++i) {
//                SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS, debugColor, world, x + 0.5, y,
//                        z + 0.5, 0.005f * rand.nextGaussian(), 0.25f * rand.nextGaussian(),
//                        0.005f * rand.nextGaussian());
//            }
//            // Debug particles: ring
//            for (float f = 0; f < 2 * Math.PI; f += Math.PI / 32) {
//                Vec3d v = new Vec3d(dist, 0, 0).rotateYaw(f);
//                SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS, debugColor, world,
//                        pos.getX() + 0.5 + v.x, pos.getY() + 0.5, pos.getZ() + 0.5 + v.z, 0, 0, 0);
//            }
//        }

        if (canPlacePhantomLightAt(tryPos)) {
            placePhantomLightAt(tryPos);
            return true;
        }

        for (int ty = y + 1; ty > y - 2; --ty) {
            for (int tx = x - 1; tx < x + 2; ++tx) {
                for (int tz = z - 1; tz < z + 2; ++tz) {
                    tryPos.setPos(tx, ty, tz);
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
        // Check that target pos is air and needs more light.
        if (!world.isAirBlock(target) || world.getLightFor(EnumLightType.BLOCK, target) > 9)
            return false;

        // Check for equal tile entities above and below target pos (ie prevent from spawning inside
        // multiblock tanks).
        TileEntity te;
        BlockPos pos;
        Class clazz = null;

        for (int i = 0; i < TE_SEARCH_RADIUS && clazz == null; ++i) {
            pos = target.up(i);
            te = world.getTileEntity(pos);
            if (te != null)
                clazz = te.getClass();
            else if (!world.isAirBlock(pos))
                return true;
        }

        for (int i = 0; i < TE_SEARCH_RADIUS; ++i) {
            pos = target.down(i);
            te = world.getTileEntity(pos);
            if (te != null && clazz == te.getClass())
                return false;
            else if (!world.isAirBlock(pos))
                return true;
        }

        return true;
    }

    private void placePhantomLightAt(BlockPos target) {
        world.setBlockState(target, getLightBlock());
        TileEntity tile = world.getTileEntity(target);
        if (tile instanceof TilePhantomLight) {
            TilePhantomLight tileLight = (TilePhantomLight) tile;
            tileLight.setSpawnerPos(this.pos);
        }
    }

    private static IBlockState getLightBlock() {
        // FIXME
        return null;
    }

    @Deprecated
    public int getFlowerId() {
        return -1;
    }

    @Nonnull
    public ItemStack getFlowerItemStack() {
//        return getStackInSlot(0);
        return ItemStack.EMPTY;
    }

    public void setFlowerItemStack(@Nonnull ItemStack stack) {
//        setInventorySlotContents(0, stack);
//        flowerId = stack.getItemDamage();
        ticksExisted = 0;
    }
}
