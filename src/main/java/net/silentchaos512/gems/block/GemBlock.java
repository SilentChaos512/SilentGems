package net.silentchaos512.gems.block;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.gems.util.Gems;

public class GemBlock extends Block implements IGemBlock {
    protected final Gems gem;
    private final String translationKey;

    public GemBlock(Gems gem, String translationKey, Properties properties) {
        super(properties);
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
