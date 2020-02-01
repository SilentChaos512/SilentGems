package net.silentchaos512.gems.block.flowerpot;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.GlowroseBlock;
import net.silentchaos512.gems.init.GemsTileEntities;
import net.silentchaos512.lib.util.TimeUtils;

import javax.annotation.Nullable;
import java.util.Random;

public class LuminousFlowerPotTileEntity extends TileEntity implements ITickableTileEntity {
    private static final int TRY_LIGHT_DELAY = TimeUtils.ticksFromSeconds(5);
    private static final int TE_SEARCH_RADIUS = 7;
    private static final String NBT_FLOWER = "PottedFlowerItem";

    private int ticksExisted = 0;
    private ItemStack flower = ItemStack.EMPTY;
    private boolean plantedGlowrose = false;

    public LuminousFlowerPotTileEntity() {
        super(GemsTileEntities.CHAOS_FLOWER_POT.type());
    }

    @Override
    public void tick() {
        if (ticksExisted == 0) {
            // Add a random "salt" value to ticksExisted, so all flower pots don't
            // try to place lights at the same time.
            ticksExisted += SilentGems.random.nextInt(300);
        }

        final int delay = ticksExisted < 600 ? 10 : TRY_LIGHT_DELAY;
        if (++ticksExisted % delay == 0) {
            tryPlacePhantomLight();
        }
    }

    private void tryPlacePhantomLight() {
        if (world.isRemote || flower.isEmpty()) return;

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

        BlockPos.Mutable tryPos = new BlockPos.Mutable(x, y, z);

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
            return;
        }

        for (int ty = y + 1; ty > y - 2; --ty) {
            for (int tx = x - 1; tx < x + 2; ++tx) {
                for (int tz = z - 1; tz < z + 2; ++tz) {
                    tryPos.setPos(tx, ty, tz);
                    if (canPlacePhantomLightAt(tryPos)) {
                        placePhantomLightAt(tryPos);
                        return;
                    }
                }
            }
        }
    }

    private boolean canPlacePhantomLightAt(BlockPos target) {
        // Check that target pos is air and needs more light.
        if (!world.isAirBlock(target) || world.getLightFor(LightType.BLOCK, target) > 9)
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
        if (tile instanceof PhantomLightTileEntity) {
            PhantomLightTileEntity tileLight = (PhantomLightTileEntity) tile;
            tileLight.setSpawnerPos(this.pos);
        }
    }

    private static BlockState getLightBlock() {
        return PhantomLightBlock.INSTANCE.get().getDefaultState();
    }

    @Deprecated
    public int getFlowerId() {
        Block flowerBlock = Block.getBlockFromItem(flower.getItem());
        if (!flower.isEmpty() && flowerBlock instanceof GlowroseBlock) {
            return ((GlowroseBlock) flowerBlock).getGem().ordinal();
        }
        return -1;
    }

    ItemStack getFlower() {
        return flower;
    }

    void setFlower(ItemStack stack) {
        if (stack.isEmpty()) return;

        this.flower = stack;
        this.plantedGlowrose = Block.getBlockFromItem(stack.getItem()) instanceof GlowroseBlock;
        ticksExisted = 0;
    }

    @Override
    public void read(CompoundNBT tags) {
        if (tags.contains(NBT_FLOWER)) {
            this.flower = ItemStack.read(tags.getCompound(NBT_FLOWER));
        }
        super.read(tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        if (!flower.isEmpty()) {
            tags.put(NBT_FLOWER, flower.serializeNBT());
        }
        return super.write(tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        if (!flower.isEmpty()) {
            // Create a copy of just the flower item, ignore any NBT it may have
            tags.put(NBT_FLOWER, new ItemStack(flower.getItem()).serializeNBT());
        }
        return tags;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (pkt.getNbtCompound().contains(NBT_FLOWER)) {
            this.flower = ItemStack.read(pkt.getNbtCompound().getCompound(NBT_FLOWER));
        }
        super.onDataPacket(net, pkt);
    }
}
