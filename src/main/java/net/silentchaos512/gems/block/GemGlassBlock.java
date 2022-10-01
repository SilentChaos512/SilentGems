package net.silentchaos512.gems.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
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
    public MutableComponent getGemBlockName() {
        return Component.translatable("block.silentgems.gem_glass", this.gem.getDisplayName());
    }

    @Override
    public MutableComponent getName() {
        return getGemBlockName();
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
        return this.gem.getColorArray();
    }
}
