package net.silentchaos512.gems;

import net.neoforged.neoforge.common.ModConfigSpec;

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

        private Common(ModConfigSpec.Builder builder) {
            glowroseNormalLight = builder
                    .comment("The light level of free-standing glowroses.",
                            "Existing glowroses may not update until broken and replaced.")
                    .defineInRange("glowrose.normalLight", 10, 0, 15);
            glowrosePottedLight = builder
                    .comment("The light level of glowroses planted in vanilla flower pots.",
                            "Existing blocks may not update until broken and replaced.")
                    .defineInRange("glowrose.pottedLight", 15, 0, 15);
        }
    }

    private GemsConfig() {}
}
