package net.silentchaos512.gems.block;

import net.minecraft.util.text.IFormattableTextComponent;
import net.silentchaos512.gems.util.IGem;

public interface IGemBlock extends IGem {
    IFormattableTextComponent getGemBlockName();
}
