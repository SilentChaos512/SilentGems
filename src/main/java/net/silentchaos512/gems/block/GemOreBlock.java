package net.silentchaos512.gems.block;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.valueproviders.UniformInt;
import net.silentchaos512.gems.setup.Gems;

public class GemOreBlock extends OreBlockSG implements IGemBlock {
    private final Gems gem;
    private final String translationKey;

    public GemOreBlock(Gems gem, int harvestLevelIn, String translationKey, Properties properties) {
        super(gem::getItem, harvestLevelIn, UniformInt.of(1, 5), properties);
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
    public MutableComponent getName() {
        return getGemBlockName();
    }
}
