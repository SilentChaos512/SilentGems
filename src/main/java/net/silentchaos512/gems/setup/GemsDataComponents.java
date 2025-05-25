package net.silentchaos512.gems.setup;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.util.DimPos;

import java.util.function.Supplier;

public class GemsDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, SilentGems.MOD_ID);

    public static final Supplier<DataComponentType<DimPos>> LINKED_POS = DATA_COMPONENTS.registerComponentType(
            "linked_pos",
            builder -> builder
                    .persistent(DimPos.CODEC)
                    .networkSynchronized(DimPos.STREAM_CODEC)
    );
}
