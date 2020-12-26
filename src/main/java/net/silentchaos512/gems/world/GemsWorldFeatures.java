package net.silentchaos512.gems.world;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.util.Gems;

@Mod.EventBusSubscriber(modid = GemsBase.MOD_ID)
public final class GemsWorldFeatures {
    public static final RuleTest BASE_STONE_END = new TagMatchRuleTest(Tags.Blocks.END_STONES);

    private static boolean configuredFeaturesRegistered = false;

    private GemsWorldFeatures() {}

    @SubscribeEvent
    public static void biomeLoading(BiomeLoadingEvent biome) {
        registerConfiguredFeatures();

        if (biome.getCategory() == Biome.Category.NETHER) {
            addGemOreFeatures(biome, World.THE_NETHER);
        } else if (biome.getCategory() == Biome.Category.THEEND) {
            addGemOreFeatures(biome, World.THE_END);
        } else {
            addGemOreFeatures(biome, World.OVERWORLD);
        }
    }

    public static void registerConfiguredFeatures() {
        if (configuredFeaturesRegistered) return;
        configuredFeaturesRegistered = true;

        for (Gems gem : Gems.values()) {
            registerConfiguredFeature(gem.getName(), gem.getOreConfiguredFeature(World.OVERWORLD));
            registerConfiguredFeature(gem.getName() + "_nether", gem.getOreConfiguredFeature(World.THE_NETHER));
            registerConfiguredFeature(gem.getName() + "_end", gem.getOreConfiguredFeature(World.THE_END));
        }
    }

    private static void registerConfiguredFeature(String name, ConfiguredFeature<?, ?> configuredFeature) {
        GemsBase.LOGGER.debug("register configured feature '{}'", name);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, GemsBase.getId(name), configuredFeature);
    }

    private static void addGemOreFeatures(BiomeLoadingEvent biome, RegistryKey<World> world) {
        for (Gems gem : Gems.values()) {
            ConfiguredFeature<?, ?> feature = gem.getOreConfiguredFeature(world);
            biome.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
        }
    }
}
