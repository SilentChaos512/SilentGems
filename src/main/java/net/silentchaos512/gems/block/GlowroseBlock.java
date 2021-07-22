package net.silentchaos512.gems.block;

import net.minecraft.block.*;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.util.Gems;

import net.minecraft.block.AbstractBlock.Properties;

public class GlowroseBlock extends FlowerBlock implements IGemBlock {
    private final Gems gem;

    public GlowroseBlock(Gems gem, Properties properties) {
        super(Effects.GLOWING, 8, properties);
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return gem;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return super.mayPlaceOn(state, worldIn, pos)
                || block.is(BlockTags.BASE_STONE_NETHER)
                || block == Blocks.NETHER_QUARTZ_ORE
                || block.is(BlockTags.NYLIUM)
                || block.is(Tags.Blocks.END_STONES);
    }

    @Override
    public IFormattableTextComponent getName() {
        return getGemBlockName();
    }

    @Override
    public IFormattableTextComponent getGemBlockName() {
        return new TranslationTextComponent("block.silentgems.glowrose", this.gem.getDisplayName());
    }
}
