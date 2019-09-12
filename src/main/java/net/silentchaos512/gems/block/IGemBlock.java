package net.silentchaos512.gems.block;

import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.gems.lib.IGem;

public interface IGemBlock extends IGem {
    ITextComponent getGemBlockName();
}
