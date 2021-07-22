package net.silentchaos512.gems.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.silentchaos512.gems.util.Gems;

import javax.annotation.Nullable;

public class GemGlassBlock extends StainedGlassBlock implements IGemBlock {
    private final Gems gem;

    public GemGlassBlock(Gems gem, Properties properties) {
        super(DyeColor.WHITE, properties);
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return gem;
    }

    @Override
    public IFormattableTextComponent getGemBlockName() {
        return new TranslationTextComponent("block.silentgems.gem_glass", this.gem.getDisplayName());
    }

    @Override
    public IFormattableTextComponent getName() {
        return getGemBlockName();
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos) {
        return this.gem.getColorArray();
    }
}
