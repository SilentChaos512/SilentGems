package net.silentchaos512.gems.setup;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.silentchaos512.gems.SilentGems;

import java.util.function.Supplier;

public class GemsAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> REGISTRAR = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SilentGems.MOD_ID);

    public static final Supplier<AttachmentType<Integer>> COFFEE_TIMER = REGISTRAR.register(
            "coffee_timer",
            () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT.fieldOf("coffee_timer"))
                    .build()
    );
}
