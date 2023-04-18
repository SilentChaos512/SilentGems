package net.silentchaos512.gems.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.silentchaos512.gems.util.Gems;

public class GemLampBlock extends GemBlock {
    public enum State {
        OFF(false, false),
        ON(true, false),
        INVERTED_ON(true, true),
        INVERTED_OFF(false, true);

        private final boolean lit;
        private final boolean inverted;

        State(boolean lit, boolean inverted) {
            this.lit = lit;
            this.inverted = inverted;
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

    private void checkAndUpdateState(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            boolean powered = world.hasNeighborSignal(pos);
            State newLampState = this.lampState.withPower(powered);

            if (newLampState != this.lampState) {
                BlockState newState = this.gem.getLamp(newLampState).defaultBlockState();
                world.setBlock(pos, newState, 2);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean p_220082_5_) {
        checkAndUpdateState(worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_) {
        checkAndUpdateState(worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        checkAndUpdateState(world, pos);
    }
}
