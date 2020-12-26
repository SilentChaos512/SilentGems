package net.silentchaos512.gems.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.util.Gems;

@Mod.EventBusSubscriber(modid = GemsBase.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GemsConfig {
    public static final class Common {
        static final ForgeConfigSpec spec;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

            {
                builder.comment("World generation settings. You must restart your game after changing these.",
                        "Changes will not be reflected in chunks that have already generated, only new ones.");
                builder.push("world");

                builder.comment("Settings for ores. Setting either vein count or vein size to zero (0) will stop that ore from generating.",
                        "Mod dimensions will use the overworld config.",
                        "Note that most of these config are disabled by default. They are provided so that which gems are in each dimension can be customized.");
                builder.push("ores");

                Gems.buildOreConfigs(builder);

                builder.pop(2);
            }

            spec = builder.build();
        }

        private Common() {}
    }

    private GemsConfig() {}

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
