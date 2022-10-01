package net.silentchaos512.gems.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.silentchaos512.gems.GemsBase;

@Mod.EventBusSubscriber(modid = GemsBase.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GemsConfig {
    public static final class Common {
        // block
        public static final ForgeConfigSpec.IntValue glowroseNormalLight;
        public static final ForgeConfigSpec.IntValue glowrosePottedLight;

        static final ForgeConfigSpec spec;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

            {
                builder.push("block");

                {
                    builder.push("glowrose");

                    glowroseNormalLight = builder
                            .comment("The light level of free-standing glowroses.",
                                    "Existing glowroses may not update until broken and replaced.",
                                    "Requires a Minecraft restart")
                            .defineInRange("normalLight", 10, 0, 15);
                    glowrosePottedLight = builder
                            .comment("The light level of glowroses planted in vanilla flower pots",
                                    "Existing blocks may not update until broken and replaced.",
                                    "Requires a Minecraft restart")
                            .defineInRange("pottedLight", 15, 0, 15);

                    builder.pop();
                }

                builder.pop();
            }

            spec = builder.build();
        }

        private Common() {}

        public static boolean isLoaded() {
            return spec.isLoaded();
        }
    }

    private GemsConfig() {}

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Common.spec);
    }
}
