package net.silentchaos512.gems.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.chaos.ChaosEvents;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GemsConfig {
    public static final class Common {
        static final ForgeConfigSpec spec;

        public static final ForgeConfigSpec.IntValue chaosCoalBurnTime;
        public static final ForgeConfigSpec.IntValue chaosMaxValue;
        public static final ForgeConfigSpec.BooleanValue chaosNoEventsUntilHasBed;
        public static final ForgeConfigSpec.BooleanValue debugMasterSwitch;
        public static final Supplier<Boolean> debugShowOverlay;
        public static final Supplier<Boolean> debugExtraTooltipInfo;
        public static final ForgeConfigSpec.IntValue enderSlimeSpawnWeight;
        public static final ForgeConfigSpec.IntValue enderSlimeGroupSizeMin;
        public static final ForgeConfigSpec.IntValue enderSlimeGroupSizeMax;
        public static final ForgeConfigSpec.BooleanValue gearSoulsGetXpFromFakePlayers;
        public static final ForgeConfigSpec.IntValue glowroseMaxPlaceCount;
        public static final ForgeConfigSpec.IntValue glowroseNormalLight;
        public static final ForgeConfigSpec.IntValue glowrosePottedLight;
        public static final ForgeConfigSpec.IntValue glowroseSpawnTryCount;
        public static final ForgeConfigSpec.BooleanValue returnHomeAllowAnchors;
        public static final ForgeConfigSpec.BooleanValue returnHomeMatchGems;
        public static final ForgeConfigSpec.IntValue returnHomeMaxUses;
        public static final ForgeConfigSpec.IntValue returnHomeUseTime;
        public static final ForgeConfigSpec.DoubleValue soulGemDropRateAverage;
        public static final ForgeConfigSpec.DoubleValue soulGemDropRateBoss;
        public static final ForgeConfigSpec.DoubleValue soulGemDropRateDeviation;
        public static final ForgeConfigSpec.BooleanValue teleporterAllowAnchors;
        public static final ForgeConfigSpec.IntValue teleporterChaosCrossDimension;
        public static final ForgeConfigSpec.IntValue teleporterChaosPerBlock;
        public static final ForgeConfigSpec.IntValue teleporterFreeRange;
        public static final ForgeConfigSpec.BooleanValue teleporterMatchGems;
        public static final ForgeConfigSpec.IntValue teleporterSearchRadius;
        public static final ForgeConfigSpec.BooleanValue wispsCauseFire;
        public static final ForgeConfigSpec.BooleanValue worldGenWildFluffyPuffs;
        public static final ForgeConfigSpec.IntValue worldGenOverworldMinGemsPerBiome;
        public static final ForgeConfigSpec.IntValue worldGenOverworldMaxGemsPerBiome;
        public static final ForgeConfigSpec.IntValue worldGenNetherGemsRegionSize;
        public static final ForgeConfigSpec.IntValue worldGenEndGemsRegionSize;
        public static final ForgeConfigSpec.IntValue worldGenOtherDimensionGemsRegionSize;
        public static final ForgeConfigSpec.IntValue worldGenSilverVeinCount;
        public static final ForgeConfigSpec.DoubleValue worldGenGeodeBaseChance;
        public static final ForgeConfigSpec.DoubleValue worldGenGeodeChanceVariation;
        public static final ForgeConfigSpec.IntValue worldGenChaosOreMinCount;
        public static final ForgeConfigSpec.IntValue worldGenChaosOreMaxCount;
        public static final ForgeConfigSpec.IntValue worldGenEnderOreCount;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            {
                builder.push("general");
                {
                    chaosCoalBurnTime = builder
                            .comment("The burn time (in ticks) of chaos coal (normal coal is 1600)")
                            .defineInRange("chaosCoalBurnTime", 6400, 0, Integer.MAX_VALUE);
                }
                builder.pop();
            }
            {
                builder.push("chaos");
                chaosMaxValue = builder
                        .comment("The most chaos the any source (player or world) can accumulate")
                        .defineInRange("maxValue", 10_000_000, 0, Integer.MAX_VALUE);
                chaosNoEventsUntilHasBed = builder
                        .comment("If true, players will not experience chaos events until they have used a bed (set a respawn point)")
                        .define("noEventsUntilPlayerHasBed", true);
                builder.pop();
            }
            {
                builder.push("debug");
                debugMasterSwitch = builder
                        .comment("Must be true for any other debug settings to take effect")
                        .define("masterSwitch", SilentGems.isDevBuild());
                debugShowOverlay = debugConfig(builder
                        .comment("Display text on-screen with various information, such as player/world chaos")
                        .define("showOverlay", true));
                debugExtraTooltipInfo = debugConfig(builder
                        .comment("Add additional tooltip information to some items")
                        .define("extraTooltipInfo", true));
                builder.pop();
            }
            {
                builder.comment("Settings for entities and mobs");
                builder.push("entity");
                builder.push("enderSlime");
                builder.comment("Ender slime spawn properties (REQUIRES RESTART)");
                builder.push("spawn");
                enderSlimeSpawnWeight = builder
                        .comment("Spawn weight of ender slimes in The End. Set to zero to disable spawns.")
                        .defineInRange("spawn.weight", 3, 0, Integer.MAX_VALUE);
                enderSlimeGroupSizeMin = builder
                        .comment("Smallest possible group size")
                        .defineInRange("spawn.minGroupSize", 1, 1, Integer.MAX_VALUE);
                enderSlimeGroupSizeMax = builder
                        .comment("Largest possible group size")
                        .defineInRange("spawn.maxGroupSize", 2, 1, Integer.MAX_VALUE);
                builder.pop(2);
                builder.push("wisp");
                wispsCauseFire = builder
                        .comment("Fire and lightning wisps can light blocks on fire")
                        .define("canCauseFire", true);
                builder.pop(2);
            }
            {
                builder.push("gearSoul");
                gearSoulsGetXpFromFakePlayers = builder
                        .comment("If true, gear souls can gain XP when being used by fake players (certain machines)")
                        .define("fakePlayersGetXp", false);
                builder.pop();
            }
            {
                builder.push("glowrose");
                glowroseMaxPlaceCount = builder
                        .comment("The most glowroses that can be in a single patch")
                        .defineInRange("world.maxPerPatch", 16, 0, Integer.MAX_VALUE);
                glowroseSpawnTryCount = builder
                        .comment("The number of placement attempts when generating new chunks (higher numbers = bigger patches)",
                                "Note this is the number of 'attempts', not the actual number you will likely see in any given patch")
                        .defineInRange("world.placeTryCount", 40, 0, Integer.MAX_VALUE);
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
            {
                builder.push("returnHomeCharm");
                returnHomeAllowAnchors = builder
                        .comment("Allow return home charms to be bound to teleporter anchors")
                        .define("allowAnchors", true);
                returnHomeMatchGems = builder
                        .comment("Only allow return home charms to be bound to teleporters made with the same gem")
                        .define("sameGemOnly", false);
                returnHomeMaxUses = builder
                        .comment("Durability of return home charms. 0 means unlimited. Charms with durability will still generate chaos when used.")
                        .defineInRange("maxUses", 0, 0, Integer.MAX_VALUE);
                returnHomeUseTime = builder
                        .comment("The time (in ticks) the player must use a return home charm to activate it")
                        .defineInRange("useTime", 16, 0, Integer.MAX_VALUE);
                builder.pop();
            }
            {
                builder.push("soulGem");
                builder.comment("Drop rate of soul gems is randomly selected based on the world seed.",
                        "There is an average and a deviation, which makes a normal distribution.",
                        "The numbers will tend to be close to average, but could occasionally be plus/minus a couple deviations.");
                builder.push("dropRate");
                soulGemDropRateAverage = builder
                        .comment("Average drop rate of soul gems (1 = 100%)")
                        .defineInRange("average", 0.025, 0, 1);
                soulGemDropRateBoss = builder
                        .comment("The drop rate for boss creatures (overrides normal calculation)")
                        .defineInRange("boss", 1.0, 0, 1);
                soulGemDropRateDeviation = builder
                        .comment("Standard deviation of drop rate (should be no more than a quarter of the average, preferably less) [0 ~ 1]")
                        .defineInRange("deviation", 0.002, 0, 1);
                builder.pop(2);
            }
            {
                builder.push("teleporter");
                builder.push("chaos");
                teleporterChaosCrossDimension = builder
                        .comment("The chaos produced when traveling between dimensions using a teleport")
                        .defineInRange("crossDimension", 50_000, 0, Integer.MAX_VALUE);
                teleporterChaosPerBlock = builder
                        .comment("The chaos produced per block traveled (ignores Y-axis)",
                                " Does not apply when teleporting to another dimension")
                        .defineInRange("perBlock", 50, 0, Integer.MAX_VALUE);
                teleporterFreeRange = builder
                        .comment("When teleporting this distance or less, no chaos is produced (ignores Y-axis)")
                        .defineInRange("freeRange", 64, 0, Integer.MAX_VALUE);
                builder.pop();
                builder.push("redstone");
                teleporterSearchRadius = builder
                        .comment("All entities within this distance of a redstone teleporter will teleport when activated with redstone.",
                                "Default is 2 blocks, restricted to [1,16]")
                        .defineInRange("searchRadius", 2, 1, 16);
                builder.pop();
                teleporterAllowAnchors = builder
                        .comment("Allow teleporters to link to teleporter anchors")
                        .define("allowAnchors", true);
                teleporterMatchGems = builder
                        .comment("Only allow teleporters to be linked to teleporters made with the same gem")
                        .define("sameGemOnly", false);
                builder.pop();
            }
            {
                builder.push("world");
                builder.comment("World generation settings (ores, etc.) Most of these REQUIRE A RESTART!");
                builder.push("generation");
                worldGenWildFluffyPuffs = builder
                        .comment("Generate wild fluffy puff plants. If disabled, you will need to add some other way to obtain fluffy puff seeds.")
                        .define("plants.wildFluffyPuffs", true);
                worldGenOverworldMinGemsPerBiome = builder
                        .comment("The fewest number of gem types to select per biome. Should be no bigger than the max value.",
                                "Setting both min and max to zero will disable normal gem generation in the overworld, but not in other dimensions.")
                        .defineInRange("overworld.gems.minTypePerBiome", 2, 0, 16);
                worldGenOverworldMaxGemsPerBiome = builder
                        .comment("The most gem types to select per biome. Should be at least the same as the min value.",
                                "Setting both min and max to zero will disable normal gem generation in the overworld, but not in other dimensions.")
                        .defineInRange("overworld.gems.maxTypePerBiome", 5, 0, 16);
                worldGenNetherGemsRegionSize = builder
                        .comment("The region size for gems in the Nether.",
                                "Each 'size x size' chunk area is a 'region', which contains a couple types of gems.",
                                "Larger regions will make finding many types of gems more difficult.",
                                "Setting to zero will disable gem generation in this dimension.")
                        .defineInRange("nether.gems.regionSize", 8, 0, Integer.MAX_VALUE);
                worldGenEndGemsRegionSize = builder
                        .comment("The region size for gems in The End.",
                                "Each 'size x size' chunk area is a 'region', which contains a couple types of gems.",
                                "Larger regions will make finding many types of gems more difficult.",
                                "Setting to zero will disable gem generation in this dimension.")
                        .defineInRange("end.gems.regionSize", 6, 0, Integer.MAX_VALUE);
                worldGenOtherDimensionGemsRegionSize = builder
                        .comment("The region size for gems in non-vanilla dimensions.",
                                "Each 'size x size' chunk area is a 'region', which contains a couple types of gems.",
                                "Larger regions will make finding many types of gems more difficult.",
                                "Setting to zero will disable gem generation in these dimensions (does not include overworld).")
                        .defineInRange("other.gems.regionSize", 4, 0, Integer.MAX_VALUE);
                worldGenSilverVeinCount = builder
                        .comment("Number of veins of silver ore per chunk. Set 0 to disable.",
                                "Default: 0 if Silent's Mechanisms is installed when config is created, 2 otherwise")
                        .defineInRange("ores.silver.veinCount", ModList.get().isLoaded("silents_mechanisms") ? 0 : 2, 0, Integer.MAX_VALUE);
                worldGenGeodeBaseChance = builder
                        .comment("The base chance of a chunk having a gem geode.",
                                " Setting to zero will disable geodes. A value of one would make every chunk have a geode.")
                        .defineInRange("overworld.geode.baseChance", 0.05, 0.0, 1.0);
                worldGenGeodeChanceVariation = builder
                        .comment("Max variation in geode chance. The final chance is a normal distribution, with this being the standard deviation.",
                                "This will tend to be close to the base chance, but could be more/less by several times this value.",
                                "The chance is rolled separately for each biome.")
                        .defineInRange("overworld.geode.chanceVariation", 0.0025, 0.0, 1.0);
                worldGenChaosOreMinCount = builder
                        .defineInRange("ores.chaos.veinCount.min", 1, 0, 1000);
                worldGenChaosOreMaxCount = builder
                        .defineInRange("ores.chaos.veinCount.max", 2, 0, 1000);
                worldGenEnderOreCount = builder
                        .comment("Number of ender ore veins per chunk in The End. Set zero to disable.")
                        .defineInRange("ores.ender.veinCount", 1, 0, 1000);
                builder.pop(2);
            }

            ChaosEvents.loadConfigs(builder);

            spec = builder.build();
        }

        private Common() {}

        private static Supplier<Boolean> debugConfig(ForgeConfigSpec.BooleanValue config) {
            return () -> debugMasterSwitch != null && config != null && debugMasterSwitch.get() && config.get();
        }
    }

    private GemsConfig() { }

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Common.spec);
    }

    public static void sync() {
    }

    @SubscribeEvent
    public static void sync(ModConfig.Loading event) {
        sync();
    }

    @SubscribeEvent
    public static void sync(ModConfig.Reloading event) {
        sync();
    }
}
