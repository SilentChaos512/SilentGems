package net.silentchaos512.gems;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = SilentGems.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class GemsConfig {
    public static final class Common {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        // block
        public static final ModConfigSpec.IntValue GLOWROSE_NORMAL_LIGHT = BUILDER
                .comment("The light level of free-standing glowroses.",
                        "Existing glowroses may not update until broken and replaced.")
                .defineInRange("glowrose.normalLight", 10, 0, 15);
        public static final ModConfigSpec.IntValue GLOWROSE_POTTED_LIGHT = BUILDER
                .comment("The light level of glowroses planted in vanilla flower pots",
                        "Existing blocks may not update until broken and replaced.")
                .defineInRange("glowrose.pottedLight", 15, 0, 15);

        static final ModConfigSpec SPEC = BUILDER.build();

        public static int glowroseNormalLight;
        public static int glowrosePottedLight;

        private Common() {}
    }

    private GemsConfig() {}

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        Common.glowroseNormalLight = Common.GLOWROSE_NORMAL_LIGHT.get();
        Common.glowrosePottedLight = Common.GLOWROSE_POTTED_LIGHT.get();
    }
}
