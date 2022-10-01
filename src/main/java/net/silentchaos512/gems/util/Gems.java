package net.silentchaos512.gems.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Material;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.data.GemsWorldGen;
import net.silentchaos512.gems.item.GemBlockItem;
import net.silentchaos512.gems.item.GemItem;
import net.silentchaos512.gems.setup.Registration;
import net.silentchaos512.gems.world.OreConfigDefaults;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.utils.Color;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("NonFinalFieldInEnum")
public enum Gems {
    RUBY(0xE61D1D, //hue=0
            Rarity.COMMON,
            OreConfigDefaults.defaults(4, 8, 2, -64, 32, 0.2f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty()),
    CARNELIAN(0xE04D1D, //15
            Rarity.UNCOMMON,
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 8, 1, 25, 110),
            OreConfigDefaults.empty()),
    TOPAZ(0xE6711D, //25
            Rarity.COMMON,
            OreConfigDefaults.defaults(4, 8, 2, -56, 40, 0.2f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty()),
    CITRINE(0xC78B03, //40
            Rarity.UNCOMMON,
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 8, 1, 25, 110),
            OreConfigDefaults.empty()),
    HELIODOR(0xE6C51D, //50
            Rarity.COMMON,
            OreConfigDefaults.defaults(1, 6, 4, -80, -32, 0.8f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 1, 16, 72)),
    MOLDAVITE(0xA6D923, //75
            Rarity.UNCOMMON,
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 8, 1, 25, 110),
            OreConfigDefaults.empty()),
    PERIDOT(0x29DB18, //115
            Rarity.COMMON,
            OreConfigDefaults.defaults(4, 8, 2, -56, 40, 0.2f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty()),
    TURQUOISE(0x3DF4BD, //160
            Rarity.RARE,
            OreConfigDefaults.defaults(1, 6, 4, -80, -32, 0.8f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 1, 16, 72)),
    KYANITE(0x41C4F3, //195 (-165)
            Rarity.RARE,
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 1, 16, 72)),
    SAPPHIRE(0x1D60E5, //220 (-140)
            Rarity.COMMON,
            OreConfigDefaults.defaults(4, 8, 2, -80, 32, 0.2f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty()),
    IOLITE(0x7543F5, //260 (-100)
            Rarity.UNCOMMON,
            OreConfigDefaults.defaults(1, 6, 4, -80, -32, 0.8f),
            OreConfigDefaults.defaults(2, 8, 1, 20, 80),
            OreConfigDefaults.empty()),
    ALEXANDRITE(0xAB37E5, //280 (-80)
            Rarity.UNCOMMON,
            OreConfigDefaults.defaults(1, 6, 4, -80, -32, 0.8f),
            OreConfigDefaults.defaults(2, 8, 1, 20, 80),
            OreConfigDefaults.empty()),
    AMMOLITE(0xDB2BFF, //290 (-70)
            Rarity.RARE,
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 1, 16, 72)),
    ROSE_QUARTZ(0xFF4EAB, //330 (-30), B+30,C+40
            Rarity.RARE,
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 1, 16, 72)),
    BLACK_DIAMOND(0x5F524C, //20, Sat=20,Lit=-36
            Rarity.EPIC,
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(3, 8, 3, -10, 60),
            OreConfigDefaults.empty()),
    WHITE_DIAMOND(0xD5C1D2, //310 (-50), Sat=10, B+50,C+30
            Rarity.EPIC,
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(3, 8, 3, -10, 60));

    private final Color color;
    private final Rarity rarity;

    // World/ore generation
    private final Map<ResourceKey<Level>, OreConfigDefaults> oreConfigDefaults = new HashMap<>();

    // Blocks
    BlockRegistryObject<GemOreBlock> ore;
    BlockRegistryObject<GemOreBlock> deepslateOre;
    BlockRegistryObject<GemOreBlock> netherOre;
    BlockRegistryObject<GemOreBlock> endOre;
    BlockRegistryObject<GemBlock> block;
    BlockRegistryObject<GemBlock> bricks;
    BlockRegistryObject<GemGlassBlock> glass;
    BlockRegistryObject<GlowroseBlock> glowrose;
    BlockRegistryObject<FlowerPotBlock> pottedGlowrose;
    Map<GemLampBlock.State, BlockRegistryObject<GemLampBlock>> lamps = new EnumMap<>(GemLampBlock.State.class);

    // Items
    ItemRegistryObject<GemItem> item;
    ItemRegistryObject<GemItem> shard;

    // Tags
    final TagKey<Block> blockTag;
    final TagKey<Block> glowroseTag;
    final TagKey<Block> oreTag;
    final TagKey<Block> modOresTag;
    final TagKey<Item> blockItemTag;
    final TagKey<Item> glowroseItemTag;
    final TagKey<Item> oreItemTag;
    final TagKey<Item> modOresItemTag;
    final TagKey<Item> itemTag;
//    final ITag.INamedTag<Item> shardTag;

    Gems(int colorIn, Rarity rarity, OreConfigDefaults overworldOres, OreConfigDefaults netherOres, OreConfigDefaults endOres) {
        this.color = new Color(colorIn);
        this.rarity = rarity;

        this.oreConfigDefaults.put(Level.OVERWORLD, overworldOres);
        this.oreConfigDefaults.put(Level.NETHER, netherOres);
        this.oreConfigDefaults.put(Level.END, endOres);

        String name = this.getName();
        this.blockTag = makeBlockTag(forgeId("storage_blocks/" + name));
        this.glowroseTag = makeBlockTag(GemsBase.getId("glowroses/" + this.getName()));
        this.oreTag = makeBlockTag(forgeId("ores/" + name));
        this.modOresTag = makeBlockTag(GemsBase.getId("ores/" + name));

        this.blockItemTag = makeItemTag(forgeId("storage_blocks/" + name));
        this.glowroseItemTag = makeItemTag(GemsBase.getId("glowroses/" + this.getName()));
        this.oreItemTag = makeItemTag(forgeId("ores/" + name));
        this.modOresItemTag = makeItemTag(GemsBase.getId("ores/" + name));
        this.itemTag = makeItemTag(forgeId("gems/" + name));
    }

    private static TagKey<Block> makeBlockTag(ResourceLocation name) {
        return BlockTags.create(name);
    }

    private static TagKey<Item> makeItemTag(ResourceLocation name) {
        return ItemTags.create(name);
    }

    private static ResourceLocation forgeId(String path) {
        return new ResourceLocation("forge", path);
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getColor() {
        return color.getColor();
    }

    public float[] getColorArray() {
        return new float[]{color.getRed(), color.getGreen(), color.getBlue()};
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Component getDisplayName() {
        return Component.translatable("gem.silentgems." + this.getName());
    }

    //region World generation

    public OreConfigDefaults getOreConfigDefaults(ResourceKey<Level> level) {
        return this.oreConfigDefaults.getOrDefault(level, this.oreConfigDefaults.get(Level.OVERWORLD));
    }

    public ConfiguredFeature<OreConfiguration, ?> createOreConfiguredFeature(ResourceKey<Level> level) {
        OreConfigDefaults config = this.getOreConfigDefaults(level);
        String configName;

        OreConfiguration oreConfiguration;
        if (level == Level.NETHER) {
            oreConfiguration = new OreConfiguration(OreFeatures.NETHERRACK, netherOre.get().defaultBlockState(), config.size(), config.discardChanceOnAirExposure());
            configName = getName() + "_nether_ore";
        } else if (level == Level.END) {
            oreConfiguration = new OreConfiguration(GemsWorldGen.BASE_STONE_END, endOre.get().defaultBlockState(), config.size(), config.discardChanceOnAirExposure());
            configName = getName() + "_end_ore";
        } else {
            ImmutableList<OreConfiguration.TargetBlockState> targetList = ImmutableList.of(
                    OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ore.get().defaultBlockState()),
                    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateOre.get().defaultBlockState()));
            oreConfiguration = new OreConfiguration(targetList, config.size(), config.discardChanceOnAirExposure());
            configName = getName() + "_ore";
        }

        return new ConfiguredFeature<>(Feature.ORE, oreConfiguration);
    }

    public PlacedFeature createOrePlacedFeature(ResourceKey<Level> level, Holder<ConfiguredFeature<?, ?>> configuredFeature) {
        OreConfigDefaults config = getOreConfigDefaults(level);

        String configName;
        if (level == Level.NETHER) {
            configName = getName() + "_nether_ore";
        } else if (level == Level.END) {
            configName = getName() + "_end_ore";
        } else {
            configName = getName() + "_ore";
        }

        return new PlacedFeature(configuredFeature, List.of(
                CountPlacement.of(config.count()),
                RarityFilter.onAverageOnceEvery(config.rarity()),
                InSquarePlacement.spread(),
                HeightRangePlacement.triangle(VerticalAnchor.absolute(config.minHeight()), VerticalAnchor.absolute(config.maxHeight())),
                BiomeFilter.biome()
        ));
    }

    public ConfiguredFeature<RandomPatchConfiguration, ?> createGlowroseConfiguredFeature(ResourceKey<Level> level) {
        OreConfigDefaults config = this.getOreConfigDefaults(level);
        int baseSpread = config.isEnabled() ? 2 : 0;

        String configName;
        if (level == Level.NETHER) {
            configName = getName() + "_nether_glowrose";
        } else if (level == Level.END) {
            configName = getName() + "_end_glowrose";
        } else {
            configName = getName() + "_glowrose";
        }

        RandomPatchConfiguration featureConfig = FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(getGlowrose())),
                List.of(),
                32);
        return new ConfiguredFeature<>(Feature.FLOWER, featureConfig);
    }

    public PlacedFeature createGlowrosePlacedFeature(ResourceKey<Level> level, Holder<ConfiguredFeature<?, ?>> configuredFeature) {
        OreConfigDefaults config = getOreConfigDefaults(level);

        String configName;
        if (level == Level.NETHER) {
            configName = getName() + "_nether_glowrose";
        } else if (level == Level.END) {
            configName = getName() + "_end_glowrose";
        } else {
            configName = getName() + "_glowrose";
        }

        return new PlacedFeature(configuredFeature, List.of(
                RarityFilter.onAverageOnceEvery(128 * config.rarity()),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        ));
    }

    //endregion

    //region Block, Item, and Tag getters

    public GemOreBlock getOre() {
        return ore.get();
    }

    public GemOreBlock getDeepslateOre() {
        return deepslateOre.get();
    }

    public GemOreBlock getNetherOre() {
        return netherOre.get();
    }

    public GemOreBlock getEndOre() {
        return endOre.get();
    }

    public GemBlock getBlock() {
        return block.get();
    }

    public GemBlock getBricks() {
        return bricks.get();
    }

    public GemGlassBlock getGlass() {
        return glass.get();
    }

    public GemLampBlock getLamp(GemLampBlock.State state) {
        return lamps.get(state).get();
    }

    public GlowroseBlock getGlowrose() {
        return glowrose.get();
    }

    public FlowerPotBlock getPottedGlowrose() {
        return pottedGlowrose.get();
    }

    public GemItem getItem() {
        return item.get();
    }

    @Deprecated
    public GemItem getShard() {
        return shard.get();
    }

    public TagKey<Block> getOreTag() {
        return oreTag;
    }

    public TagKey<Block> getModOresTag() {
        return modOresTag;
    }

    public TagKey<Block> getBlockTag() {
        return blockTag;
    }

    public TagKey<Block> getGlowroseTag() {
        return glowroseTag;
    }

    public TagKey<Item> getOreItemTag() {
        return oreItemTag;
    }

    public TagKey<Item> getModOresItemTag() {
        return modOresItemTag;
    }

    public TagKey<Item> getBlockItemTag() {
        return blockItemTag;
    }

    public TagKey<Item> getGlowroseItemTag() {
        return glowroseItemTag;
    }

    public TagKey<Item> getItemTag() {
        return itemTag;
    }

    //endregion

    public static void registerBlocks() {
        for (Gems gem : values())
            gem.ore = registerBlock(gem.getName() + "_ore", () ->
                    new GemOreBlock(gem, 2, "gem_ore", BlockBehaviour.Properties.of(Material.STONE)
                            .strength(3f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)));

        for (Gems gem : values())
            gem.deepslateOre = registerBlock("deepslate_" + gem.getName() + "_ore", () ->
                    new GemOreBlock(gem, 2, "deepslate_gem_ore", BlockBehaviour.Properties.copy(gem.ore.get())
                            .strength(4.5f, 3f)
                            .sound(SoundType.DEEPSLATE)));

        for (Gems gem : values())
            gem.netherOre = registerBlock(gem.getName() + "_nether_ore", () ->
                    new GemOreBlock(gem, 3, "gem_nether_ore", BlockBehaviour.Properties.copy(gem.ore.get())
                            .strength(4f)
                            .sound(SoundType.NETHER_ORE)));

        for (Gems gem : values())
            gem.endOre = registerBlock(gem.getName() + "_end_ore", () ->
                    new GemOreBlock(gem, 4, "gem_end_ore", BlockBehaviour.Properties.copy(gem.ore.get())
                            .strength(6f)));

        for (Gems gem : values())
            gem.block = registerBlock(gem.getName() + "_block", () ->
                    new GemBlock(gem, "gem_block", BlockBehaviour.Properties.of(Material.METAL)
                            .strength(4, 30)
                            .sound(SoundType.METAL)));

        for (Gems gem : values())
            gem.bricks = registerBlock(gem.getName() + "_bricks", () ->
                    new GemBlock(gem, "gem_bricks", BlockBehaviour.Properties.of(Material.STONE)
                            .strength(2f, 8f)));

        BlockBehaviour.StatePredicate isNotSolid = (state, world, pos) -> false;
        for (Gems gem : values())
            gem.glass = registerBlock(gem.getName() + "_glass", () ->
                    new GemGlassBlock(gem, BlockBehaviour.Properties.of(Material.GLASS)
                            .strength(1f, 5f)
                            .sound(SoundType.GLASS)
                            .noOcclusion()
                            .isValidSpawn((state, world, pos, entityType) -> false)
                            .isRedstoneConductor(isNotSolid)
                            .isSuffocating(isNotSolid)
                            .isViewBlocking(isNotSolid)));

        for (Gems gem : values())
            gem.lamps.put(GemLampBlock.State.OFF, registerLamp(gem, GemLampBlock.State.OFF));
        for (Gems gem : values())
            gem.lamps.put(GemLampBlock.State.ON, registerLamp(gem, GemLampBlock.State.ON));
        for (Gems gem : values())
            gem.lamps.put(GemLampBlock.State.INVERTED_ON, registerLamp(gem, GemLampBlock.State.INVERTED_ON));
        for (Gems gem : values())
            gem.lamps.put(GemLampBlock.State.INVERTED_OFF, registerLamp(gem, GemLampBlock.State.INVERTED_OFF));

        for (Gems gem : values())
            gem.glowrose = registerBlock(gem.getName() + "_glowrose", () ->
                    new GlowroseBlock(gem, BlockBehaviour.Properties.of(Material.PLANT)
                            .sound(SoundType.GRASS)
                            .lightLevel(state -> GemsConfig.Common.isLoaded() ? GemsConfig.Common.glowroseNormalLight.get() : 10)
                            .strength(0)
                            .noCollission()));

        for (Gems gem : values()) {
            gem.pottedGlowrose = registerBlockNoItem("potted_" + gem.getName() + "_glowrose", () ->
                    new PottedGlowroseBlock(gem, gem.glowrose, BlockBehaviour.Properties
                            .of(Material.DECORATION)
                            .lightLevel(state -> GemsConfig.Common.isLoaded() ? GemsConfig.Common.glowrosePottedLight.get() : 15)
                            .strength(0)));
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(GemsBase.getId(gem.getName() + "_glowrose"), gem.pottedGlowrose);
        }
    }

    public static void registerItems() {
        for (Gems gem : values())
            gem.item = registerItem(gem.getName(), () ->
                    new GemItem(gem, "gem", new Item.Properties().tab(GemsBase.ITEM_GROUP)));

//        for (Gems gem : values())
//            gem.shard = registerItem(gem.getName() + "_shard", () ->
//                    new GemItem(gem, "gem_shard", new Item.Properties().group(GemsBase.ITEM_GROUP)));
    }

    private static <T extends Block> BlockRegistryObject<T> registerBlockNoItem(String name, Supplier<T> block) {
        return new BlockRegistryObject<>(Registration.BLOCKS.register(name, block));
    }

    private static <T extends Block> BlockRegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, Gems::defaultBlockItem);
    }

    private static <T extends Block> BlockRegistryObject<T> registerBlock(String name, Supplier<T> block, Function<BlockRegistryObject<T>, Supplier<BlockItem>> item) {
        BlockRegistryObject<T> ret = registerBlockNoItem(name, block);
        if (item != null) {
            Registration.ITEMS.register(name, item.apply(ret));
        }
        return ret;
    }

    private static BlockRegistryObject<GemLampBlock> registerLamp(Gems gem, GemLampBlock.State state) {
        String name = gem.getName() + "_lamp" + (state.inverted() ? "_inverted" : "") + (state.lit() ? "_on" : "");
        return registerBlock(name,
                () -> new GemLampBlock(gem, state, Block.Properties.of(Material.BUILDABLE_GLASS)
                        .strength(0.3f, 15)
                        .lightLevel(s -> state.lit() ? 15 : 0)),
                state.hasItem() ? Gems::defaultBlockItem : null);
    }

    private static <T extends Item> ItemRegistryObject<T> registerItem(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }

    private static Supplier<BlockItem> defaultBlockItem(BlockRegistryObject<?> block) {
        return () -> new GemBlockItem(block.get(), new Item.Properties().tab(GemsBase.ITEM_GROUP));
    }
}
