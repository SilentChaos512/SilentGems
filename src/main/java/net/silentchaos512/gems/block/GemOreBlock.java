package net.silentchaos512.gems.block;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.silentchaos512.gems.util.Gems;

public class GemOreBlock extends OreBlockSG implements IGemBlock {
    private final Gems gem;
    private final String translationKey;

    public GemOreBlock(Gems gem, int harvestLevelIn, String translationKey, Properties properties) {
        super(gem::getItem, harvestLevelIn, properties);
        this.gem = gem;
        this.translationKey = translationKey;
    }

    @Override
    public Gems getGem() {
        return gem;
    }

    @Override
    public MutableComponent getGemBlockName() {
        return Component.translatable("block.silentgems." + this.translationKey, this.gem.getDisplayName());
    }

    @Override
    public int getExpRandom(RandomSource random) {
        return Mth.nextInt(random, 1, 5);
    }

    @Override
    public MutableComponent getName() {
        return getGemBlockName();
    }
}
