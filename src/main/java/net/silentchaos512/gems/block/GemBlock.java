package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.util.Gems;

public class GemBlock extends Block implements IGemBlock {
    private final Gems gem;

    public GemBlock(Gems gem, Properties properties) {
        super(properties);
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return gem;
    }

    @Override
    public IFormattableTextComponent getGemBlockName() {
        return new TranslationTextComponent("block.silentgems.gem_block", this.gem.getDisplayName());
    }

    @Override
    public IFormattableTextComponent getTranslatedName() {
        return getGemBlockName();
    }
}
