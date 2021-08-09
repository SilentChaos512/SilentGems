package net.silentchaos512.gems.block;

import net.minecraft.network.chat.MutableComponent;
import net.silentchaos512.gems.util.IGem;

public interface IGemBlock extends IGem {
    MutableComponent getGemBlockName();
}
