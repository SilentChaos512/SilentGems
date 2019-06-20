package net.silentchaos512.gems.init;

import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.traits.TraitSerializers;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.trait.PersistenceTrait;

public final class GemsTraits {
    public static final ResourceLocation CRITICAL = SilentGems.getId("critical");
    public static final ResourceLocation RUNIC = SilentGems.getId("runic");

    private GemsTraits() {}

    public static void registerSerializers() {
        TraitSerializers.register(PersistenceTrait.SERIALIZER);
    }
}
