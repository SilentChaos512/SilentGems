package net.silentchaos512.gems.setup;

import net.silentchaos512.gear.api.GearApi;
import net.silentchaos512.gear.api.traits.ITrait;
import net.silentchaos512.gear.api.util.DataResource;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.gear.trait.CriticalStrikeTrait;

public final class GemsTraits {
    public static final DataResource<ITrait> BARRIER_JACKET = trait("barrier_jacket");
    public static final DataResource<ITrait> BOOSTER = trait("booster");
    public static final DataResource<ITrait> CLOAKING = trait("cloaking");
    public static final DataResource<ITrait> CRITICAL_STRIKE = trait("critical_strike");
    public static final DataResource<ITrait> FRACTAL = trait("fractal");
    public static final DataResource<ITrait> HEARTY = trait("hearty");
    public static final DataResource<ITrait> LEAPING = trait("leaping");

    private GemsTraits() {}

    public static void registerSerializers() {
        GearApi.registerTraitSerializer(CriticalStrikeTrait.SERIALIZER);
    }

    private static DataResource<ITrait> trait(String name) {
        return DataResource.trait(GemsBase.getId(name));
    }
}
