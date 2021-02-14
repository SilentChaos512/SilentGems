package net.silentchaos512.gemschaos.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.silentchaos512.gems.config.OreConfig;
import net.silentchaos512.gemschaos.ChaosMod;

@Mod.EventBusSubscriber(modid = ChaosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ChaosConfig {
    public static final class Common {
        static final ForgeConfigSpec spec;

        public static final OreConfig chaosOres;
        public static final ForgeConfigSpec.BooleanValue chaosNoEventsUntilHasBed;
        public static final ForgeConfigSpec.IntValue maxChaos;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

            {
                builder.comment("Settings related to the chaos system");
                builder.push("chaos");

                chaosNoEventsUntilHasBed = builder
                        .comment("Players will not experience chaos events until they have a respawn point set")
                        .define("noEventsUntilHasBed", true);

                maxChaos = builder
                        .comment("The maximum chaos value that a player can reach")
                        .defineInRange("maxChaos", 10_000_000, 1, Integer.MAX_VALUE);

                builder.pop();
            }
            {
                builder.comment("World generation settings. You must restart your game after changing these.",
                        "Changes will not be reflected in chunks that have already generated, only new ones.");
                builder.push("world");

                {
                    builder.comment("Settings for ores. Setting either vein count or vein size to zero (0) will stop that ore from generating.",
                            "Mod dimensions will use the overworld config.",
                            "Note that most of these config are disabled by default. They are provided so that which gems are in each dimension can be customized.");
                    builder.push("ores");

                    chaosOres = new OreConfig(builder, "chaos",
                            OreConfig.defaults(3, 9, 2, 6, 24));

                    builder.pop();
                }

                builder.pop();
            }

            spec = builder.build();
        }

        private Common() {}
    }

    private ChaosConfig() {}

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Common.spec);
    }

    public static void sync() {}

    @SubscribeEvent
    public static void sync(ModConfig.Loading event) {
        sync();
    }

    @SubscribeEvent
    public static void sync(ModConfig.Reloading event) {
        sync();
    }
}
