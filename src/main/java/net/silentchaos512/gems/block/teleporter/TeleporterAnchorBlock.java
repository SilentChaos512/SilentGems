package net.silentchaos512.gems.block.teleporter;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.silentchaos512.utils.Lazy;

public final class TeleporterAnchor extends GemTeleporterBlock {
    public static final Lazy<TeleporterAnchor> INSTANCE = Lazy.of(TeleporterAnchor::new);

    private TeleporterAnchor() {
        super(null, true);
    }

    @Override
    public ITextComponent getNameTextComponent() {
        return new TextComponentTranslation("block.silentgems.teleporter_anchor");
    }
}
