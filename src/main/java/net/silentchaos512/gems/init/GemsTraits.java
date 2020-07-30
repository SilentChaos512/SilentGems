package net.silentchaos512.gems.init;

import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.traits.TraitSerializers;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.trait.PersistenceTrait;
import net.silentchaos512.gems.trait.RunicTrait;

public final class GemsTraits {
    public static final ResourceLocation AERIAL = SilentGems.getId("aerial");
    public static final ResourceLocation CHAOTIC = SilentGems.getId("chaotic");
    public static final ResourceLocation CRITICAL = SilentGems.getId("critical");
    public static final ResourceLocation ENTROPY = SilentGems.getId("entropy");
    public static final ResourceLocation ICE_AURA = SilentGems.getId("ice_aura");
    public static final ResourceLocation ICY = SilentGems.getId("icy");
    public static final ResourceLocation IMPERIAL = SilentGems.getId("imperial");
    public static final ResourceLocation IRONCLAD = SilentGems.getId("ironclad");
    public static final ResourceLocation LIGHTNING_AURA = SilentGems.getId("lightning_aura");
    public static final ResourceLocation LUNA = SilentGems.getId("luna");
    public static final ResourceLocation RACKER = SilentGems.getId("racker");
    public static final ResourceLocation RUNIC = SilentGems.getId("runic");
    public static final ResourceLocation SOL = SilentGems.getId("sol");
    public static final ResourceLocation SPARKING = SilentGems.getId("sparking");

    private GemsTraits() {}

    public static void registerSerializers() {
        TraitSerializers.register(PersistenceTrait.SERIALIZER);
        TraitSerializers.register(RunicTrait.SERIALIZER);
    }
}
