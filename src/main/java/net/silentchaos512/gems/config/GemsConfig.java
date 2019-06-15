package net.silentchaos512.gems.config;

import net.minecraftforge.fml.loading.FMLPaths;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.utils.config.BooleanValue;
import net.silentchaos512.utils.config.ConfigSpecWrapper;
import net.silentchaos512.utils.config.DoubleValue;
import net.silentchaos512.utils.config.IntValue;

public final class GemsConfig {
    private static final ConfigSpecWrapper WRAPPER = ConfigSpecWrapper.create(
            FMLPaths.CONFIGDIR.get().resolve("silentgems-common.toml"));

    public static final Common COMMON = new Common(WRAPPER);

    public static class Common {
        public final IntValue chaosCoalBurnTime;
        public final BooleanValue debugMasterSwitch;
        public final BooleanValue debugShowOverlay;
        public final IntValue enderSlimeSpawnWeight;
        public final IntValue enderSlimeGroupSizeMin;
        public final IntValue enderSlimeGroupSizeMax;
        public final BooleanValue gearSoulsGetXpFromFakePlayers;
        public final IntValue glowroseMaxPlaceCount;
        public final IntValue glowroseNormalLight;
        public final IntValue glowrosePottedLight;
        public final IntValue glowroseSpawnTryCount;
        public final IntValue returnHomeUseTime;
        public final DoubleValue soulGemDropRateAverage;
        public final DoubleValue soulGemDropRateBoss;
        public final DoubleValue soulGemDropRateDeviation;
        public final IntValue teleporterChaosCrossDimension;
        public final IntValue teleporterChaosPerBlock;
        public final IntValue teleporterFreeRange;
        public final IntValue teleporterSearchRadius;

        Common(ConfigSpecWrapper wrapper) {
            chaosCoalBurnTime = wrapper
                    .builder("general.chaosCoalBurnTime")
                    .comment("The burn time (in ticks) of chaos coal (normal coal is 1600)")
                    .defineInRange(6400, 0, Integer.MAX_VALUE);
            debugMasterSwitch = wrapper
                    .builder("debug.masterSwitch")
                    .comment("Must be true for any other debug settings to take effect")
                    .define(SilentGems.isDevBuild());
            debugShowOverlay = wrapper
                    .builder("debug.showOverlay")
                    .comment("Display text on-screen with various information, such as player/world chaos")
                    .define(true);
            wrapper.comment("entity.enderSlime.spawn", "Ender slime spawn properties (REQUIRES RESTART)");
            enderSlimeSpawnWeight = wrapper
                    .builder("entity.enderSlime.spawn.weight")
                    .comment("Spawn weight of ender slimes in The End. Set to zero to disable spawns.")
                    .defineInRange(3, 0, Integer.MAX_VALUE);
            enderSlimeGroupSizeMin = wrapper
                    .builder("entity.enderSlime.spawn.minGroupSize")
                    .comment("Smallest possible group size")
                    .defineInRange(1, 1, Integer.MAX_VALUE);
            enderSlimeGroupSizeMax = wrapper
                    .builder("entity.enderSlime.spawn.maxGroupSize")
                    .comment("Largest possible group size")
                    .defineInRange(2, 1, Integer.MAX_VALUE);
            gearSoulsGetXpFromFakePlayers = wrapper
                    .builder("gearSoul.fakePlayersGetXp")
                    .comment("If true, gear souls can gain XP when being used by fake players (certain machines)")
                    .define(false);
            glowroseMaxPlaceCount = wrapper
                    .builder("glowrose.world.maxPerPatch")
                    .comment("The most glowroses that can be in a single patch")
                    .defineInRange(16, 0, Integer.MAX_VALUE);
            glowroseNormalLight = wrapper
                    .builder("glowrose.normalLight")
                    .comment("The light level of free-standing glowroses [0 ~ 15]",
                            "Requires a Minecraft restart")
                    .defineInRange(10, 0, 15);
            glowrosePottedLight = wrapper
                    .builder("glowrose.pottedLight")
                    .comment("The light level of glowroses planted in vanilla flower pots [0 ~ 15]",
                            "Requires a Minecraft restart")
                    .defineInRange(15, 0, 15);
            glowroseSpawnTryCount = wrapper
                    .builder("glowrose.world.placeTryCount")
                    .comment("The number of placement attempts when generating new chunks (higher numbers = bigger patches)",
                            "Note this is the number of 'attempts', not the actual number you will likely see in any given patch")
                    .defineInRange(40, 0, Integer.MAX_VALUE);
            returnHomeUseTime = wrapper
                    .builder("returnHomeCharm.useTime")
                    .comment("The time (in ticks) the player must use a return home charm to activate it")
                    .defineInRange(16, 0, Integer.MAX_VALUE);
            wrapper.comment("soulGem.dropRate",
                    "Drop rate of soul gems is randomly selected based on the world seed.",
                    "There is an average and a deviation, which makes a normal distribution.",
                    "The numbers will tend to be close to average, but could occasionally be plus/minus a couple deviations.");
            soulGemDropRateAverage = wrapper
                    .builder("soulGem.dropRate.average")
                    .comment("Average drop rate of soul gems (1 = 100%) [0 ~ 1]")
                    .defineInRange(0.025, 0, 1);
            soulGemDropRateBoss = wrapper
                    .builder("soulGem.dropRate.boss")
                    .comment("The drop rate for boss creatures (overrides normal calculation) [0 ~ 1]")
                    .defineInRange(1.0, 0, 1);
            soulGemDropRateDeviation = wrapper
                    .builder("soulGem.dropRate.deviation")
                    .comment("Standard deviation of drop rate (should be no more than a quarter of the average, preferably less) [0 ~ 1]")
                    .defineInRange(0.002, 0, 1);
            teleporterChaosCrossDimension = wrapper
                    .builder("teleporter.chaos.crossDimension")
                    .comment("The chaos produced when traveling between dimensions using a teleport")
                    .defineInRange(50_000, 0, Integer.MAX_VALUE);
            teleporterChaosPerBlock = wrapper
                    .builder("teleporter.chaos.perBlock")
                    .comment("The chaos produced per block traveled (ignores Y-axis)",
                            " Does not apply when teleporting to another dimension")
                    .defineInRange(50, 0, Integer.MAX_VALUE);
            teleporterFreeRange = wrapper
                    .builder("teleporter.chaos.freeRange")
                    .comment("When teleporting this distance or less, no chaos is produced (ignores Y-axis)")
                    .defineInRange(64, 0, Integer.MAX_VALUE);
            teleporterSearchRadius = wrapper
                    .builder("teleporter.redstone.searchRadius")
                    .comment("All entities within this distance of a redstone teleporter will teleport when activated with redstone.",
                            "Default is 2 blocks, restricted to [1,16]")
                    .defineInRange(2, 1, 16);
        }
    }

    private GemsConfig() { }

    public static void init() {
        WRAPPER.validate();
        WRAPPER.validate();
    }
}
