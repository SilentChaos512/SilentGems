package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.util.Gems;

public class GemBlock extends Block implements IGemBlock {
    private final Gems gem;
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
    public IFormattableTextComponent getGemBlockName() {
        return new TranslationTextComponent("block.silentgems." + this.translationKey, this.gem.getDisplayName());
    }

    @Override
    public IFormattableTextComponent getTranslatedName() {
        return getGemBlockName();
    }
}
