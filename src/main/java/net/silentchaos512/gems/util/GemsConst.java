package net.silentchaos512.gems.util;

import net.silentchaos512.gear.api.traits.ITrait;
import net.silentchaos512.gear.util.DataResource;
import net.silentchaos512.gems.GemsBase;

public class GemsConst {
    public static final class Traits {
        public static final DataResource<ITrait> BARRIER_JACKET = trait("barrier_jacket");
        public static final DataResource<ITrait> BOOSTER = trait("booster");
        public static final DataResource<ITrait> CLOAKING = trait("cloaking");
        public static final DataResource<ITrait> HEARTY = trait("hearty");
        public static final DataResource<ITrait> LEAPING = trait("leaping");

        private Traits() {}

        private static DataResource<ITrait> trait(String name) {
            return DataResource.trait(GemsBase.getId(name));
        }
    }
}
