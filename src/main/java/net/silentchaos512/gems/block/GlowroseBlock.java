package net.silentchaos512.gems.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.util.Gems;

public class GlowroseBlock extends FlowerBlock implements IGemBlock {
    private final Gems gem;

    public GlowroseBlock(Gems gem, Properties properties) {
        super(MobEffects.GLOWING, 8, properties);
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return gem;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return super.mayPlaceOn(state, worldIn, pos)
                || state.is(BlockTags.BASE_STONE_NETHER)
                || state.getBlock() == Blocks.NETHER_QUARTZ_ORE
                || state.is(BlockTags.NYLIUM)
                || state.is(Tags.Blocks.END_STONES);
    }

    @Override
    public MutableComponent getName() {
        return getGemBlockName();
    }

    @Override
    public MutableComponent getGemBlockName() {
        return new TranslatableComponent("block.silentgems.glowrose", this.gem.getDisplayName());
    }
}
