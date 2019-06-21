package net.silentchaos512.gems.init;

import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.traits.TraitSerializers;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.trait.PersistenceTrait;
import net.silentchaos512.gems.trait.RunicTrait;

public final class GemsTraits {
    public static final ResourceLocation CHAOTIC = SilentGems.getId("chaotic");
    public static final ResourceLocation CRITICAL = SilentGems.getId("critical");
    public static final ResourceLocation ENTROPY = SilentGems.getId("entropy");
    public static final ResourceLocation LUNA = SilentGems.getId("luna");
    public static final ResourceLocation RUNIC = SilentGems.getId("runic");
    public static final ResourceLocation SOL = SilentGems.getId("sol");

    private GemsTraits() {}

    public static void registerSerializers() {
        TraitSerializers.register(PersistenceTrait.SERIALIZER);
        TraitSerializers.register(RunicTrait.SERIALIZER);
    }
}
