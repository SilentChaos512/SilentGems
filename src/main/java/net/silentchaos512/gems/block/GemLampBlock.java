package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.silentchaos512.gems.util.Gems;

import java.util.Random;

public class GemLampBlock extends GemBlock {
    public enum State {
        OFF(false, false, ""),
        ON(true, false, "_on"),
        INVERTED_ON(true, true, "_inverted_on"),
        INVERTED_OFF(false, true, "_inverted");

        private final boolean lit;
        private final boolean inverted;
        private final String suffix;

        State(boolean lit, boolean inverted, String suffix) {
            this.lit = lit;
            this.inverted = inverted;
            this.suffix = suffix;
        }

        public boolean lit() {
            return lit;
        }

        public boolean inverted() {
            return inverted;
        }

        public boolean hasItem() {
            return lit == inverted;
        }

        public State withPower(boolean powered) {
            if (this.inverted)
                return powered ? INVERTED_OFF : INVERTED_ON;
            else {
                return powered ? ON : OFF;
            }
        }
    }

    private final State lampState;

    public GemLampBlock(Gems gem, State lampState, Properties properties) {
        super(gem, "gem_lamp" + (lampState.inverted ? "_inverted": ""), properties);
        this.lampState = lampState;
    }

    private void checkAndUpdateState(World world, BlockPos pos) {
        if (!world.isRemote) {
            boolean powered = world.isBlockPowered(pos);
            State newLampState = this.lampState.withPower(powered);

            if (newLampState != this.lampState) {
                BlockState newState = this.gem.getLamp(newLampState).getDefaultState();
                world.setBlockState(pos, newState, 2);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean p_220082_5_) {
        checkAndUpdateState(worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_) {
        checkAndUpdateState(worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        checkAndUpdateState(world, pos);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.lampState.hasItem()) {
            super.fillItemGroup(group, items);
        }
    }
}
