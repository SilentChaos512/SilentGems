package net.silentchaos512.gems.block.teleporter;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.utils.Lazy;

public final class TeleporterAnchorBlock extends GemTeleporterBlock {
    public static final Lazy<TeleporterAnchorBlock> INSTANCE = Lazy.of(TeleporterAnchorBlock::new);

    private TeleporterAnchorBlock() {
        super(null, true);
    }

    @Override
    public ITextComponent getNameTextComponent() {
        return new TranslationTextComponent("block.silentgems.teleporter_anchor");
    }
}
