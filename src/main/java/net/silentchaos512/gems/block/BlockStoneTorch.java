package net.silentchaos512.gems.block;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;

public class BlockStoneTorch extends BlockTorch {
    public BlockStoneTorch() {
        this.setHardness(0.0f);
        this.setLightLevel(0.9375f);
        this.setSoundType(SoundType.STONE);
    }
}
