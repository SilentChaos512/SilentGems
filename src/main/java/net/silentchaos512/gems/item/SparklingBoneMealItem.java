package net.silentchaos512.gems.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.util.Gems;

public class SparklingBoneMealItem extends ItemWithFlavorText {
    public SparklingBoneMealItem(Properties properties) {
        super(properties);
    }

    private boolean isGrass(BlockState state) {
        return state.getBlock() instanceof GrassBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();
        if (!isGrass(level.getBlockState(clickedPos))) {
            return InteractionResult.PASS;
        }
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }

        BlockPos abovePos = clickedPos.above();
        RandomSource rand = SilentGems.RANDOM_SOURCE;

        for (int i = 0; i < 4; ++i) {
            BlockPos center = abovePos;

            Gems gem = Gems.values()[rand.nextInt(Gems.values().length)];
            BlockState glowrose = gem.getGlowrose().defaultBlockState();

            if (level.getBlockState(center).isAir()) {
                for (int j = 0; j < 32; ++j) {
                    BlockPos target = center.offset(rand.nextInt(- 4, 4), rand.nextInt(-3, 3), rand.nextInt(-4, 4));
                    if (isGrass(level.getBlockState(target.below())) && level.getBlockState(target).isAir()) {
                        level.setBlock(target, glowrose, 3);
                    }
                }
            }
        }

        context.getItemInHand().shrink(1);
        return InteractionResult.CONSUME;
    }
}
