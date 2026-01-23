package net.silentchaos512.gems;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.silentchaos512.lib.util.TimeUtils;

public final class GemsConfig {
    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        var commonPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();
    }

    public static final class Common {
        public final ModConfigSpec.IntValue glowroseNormalLight;
        public final ModConfigSpec.IntValue glowrosePottedLight;
        public final ModConfigSpec.BooleanValue featureRabbitsProduceCoffee;
        public final ModConfigSpec.IntValue featureRabbitsCoffeeDelay;

        private Common(ModConfigSpec.Builder builder) {
            glowroseNormalLight = builder
                    .comment("The light level of free-standing glowroses.",
                            "Existing glowroses may not update until broken and replaced.")
                    .defineInRange("glowrose.normalLight", 10, 0, 15);
            glowrosePottedLight = builder
                    .comment("The light level of glowroses planted in vanilla flower pots.",
                            "Existing blocks may not update until broken and replaced.")
                    .defineInRange("glowrose.pottedLight", 15, 0, 15);
            featureRabbitsProduceCoffee = builder
                    .comment("Rabbits will periodically drop cups of coffee")
                    .define("features.rabbitsProduceCoffee", true);
            featureRabbitsCoffeeDelay = builder
                    .comment("The time (in ticks) it takes rabbits to produce a cup of coffee")
                    .defineInRange("features.rabbitCoffeeDelay", TimeUtils.ticksFromMinutes(10), TimeUtils.ticksFromMinutes(1), TimeUtils.ticksFromHours(24));
        }
    }

    private GemsConfig() {}
}
