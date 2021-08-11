package net.silentchaos512.gems.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
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
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ToolType;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.config.OreConfig;
import net.silentchaos512.gems.item.GemBlockItem;
import net.silentchaos512.gems.item.GemItem;
import net.silentchaos512.gems.setup.Registration;
import net.silentchaos512.gems.world.GemsWorldFeatures;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.lib.world.placement.DimensionFilterConfig;
import net.silentchaos512.lib.world.placement.LibPlacements;
import net.silentchaos512.utils.Color;
import net.silentchaos512.utils.Lazy;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("NonFinalFieldInEnum")
public enum Gems {
    RUBY(0xE61D1D, //hue=0
            Rarity.COMMON,
            OreConfig.defaults(3, 8, 2, 5, 40),
            OreConfig.empty(),
            OreConfig.empty()),
    CARNELIAN(0xE04D1D, //15
            Rarity.UNCOMMON,
            OreConfig.empty(),
            OreConfig.defaults(2, 8, 1, 25, 110),
            OreConfig.empty()),
    TOPAZ(0xE6711D, //25
            Rarity.COMMON,
            OreConfig.defaults(3, 8, 2, 15, 50),
            OreConfig.empty(),
            OreConfig.empty()),
    CITRINE(0xC78B03, //40
            Rarity.UNCOMMON,
            OreConfig.empty(),
            OreConfig.defaults(2, 8, 1, 25, 110),
            OreConfig.empty()),
    HELIODOR(0xE6C51D, //50
            Rarity.COMMON,
            OreConfig.defaults(1, 6, 6, 0, 15),
            OreConfig.empty(),
            OreConfig.defaults(2, 9, 1, 16, 72)),
    MOLDAVITE(0xA6D923, //75
            Rarity.UNCOMMON,
            OreConfig.empty(),
            OreConfig.defaults(2, 8, 1, 25, 110),
            OreConfig.empty()),
    PERIDOT(0x29DB18, //115
            Rarity.COMMON,
            OreConfig.defaults(3, 8, 2, 15, 50),
            OreConfig.empty(),
            OreConfig.empty()),
    TURQUOISE(0x3DF4BD, //160
            Rarity.RARE,
            OreConfig.defaults(1, 6, 6, 0, 15),
            OreConfig.empty(),
            OreConfig.defaults(2, 9, 1, 16, 72)),
    KYANITE(0x41C4F3, //195 (-165)
            Rarity.RARE,
            OreConfig.empty(),
            OreConfig.empty(),
            OreConfig.defaults(2, 9, 1, 16, 72)),
    SAPPHIRE(0x1D60E5, //220 (-140)
            Rarity.COMMON,
            OreConfig.defaults(3, 8, 2, 5, 40),
            OreConfig.empty(),
            OreConfig.empty()),
    IOLITE(0x7543F5, //260 (-100)
            Rarity.UNCOMMON,
            OreConfig.defaults(1, 6, 6, 0, 15),
            OreConfig.defaults(2, 8, 1, 20, 80),
            OreConfig.empty()),
    ALEXANDRITE(0xAB37E5, //280 (-80)
            Rarity.UNCOMMON,
            OreConfig.defaults(1, 6, 6, 0, 15),
            OreConfig.defaults(2, 8, 1, 20, 80),
            OreConfig.empty()),
    AMMOLITE(0xDB2BFF, //290 (-70)
            Rarity.RARE,
            OreConfig.empty(),
            OreConfig.empty(),
            OreConfig.defaults(2, 9, 1, 16, 72)),
    ROSE_QUARTZ(0xFF4EAB, //330 (-30), B+30,C+40
            Rarity.RARE,
            OreConfig.empty(),
            OreConfig.empty(),
            OreConfig.defaults(2, 9, 1, 16, 72)),
    BLACK_DIAMOND(0x5F524C, //20, Sat=20,Lit=-36
            Rarity.EPIC,
            OreConfig.empty(),
            OreConfig.defaults(2, 8, 3, 10, 50),
            OreConfig.empty()),
    WHITE_DIAMOND(0xD5C1D2, //310 (-50), Sat=10, B+50,C+30
            Rarity.EPIC,
            OreConfig.empty(),
            OreConfig.empty(),
            OreConfig.defaults(2, 8, 3, 10, 50));

    private final Color color;
    private final Rarity rarity;

    // World/ore generation
    private final Map<ResourceKey<Level>, OreConfig.Defaults> oreConfigDefaults = new HashMap<>();
    private final Map<ResourceKey<Level>, OreConfig> oreConfigs = new HashMap<>();
    private final Map<ResourceKey<Level>, Lazy<ConfiguredFeature<?, ?>>> oreConfiguredFeatures = new HashMap<>();
    private final Map<ResourceKey<Level>, Lazy<ConfiguredFeature<?, ?>>> glowroseConfiguredFeatures = new HashMap<>();

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
    final Tag.Named<Block> blockTag;
    final Tag.Named<Block> glowroseTag;
    final Tag.Named<Block> oreTag;
    final Tag.Named<Block> modOresTag;
    final Tag.Named<Item> blockItemTag;
    final Tag.Named<Item> glowroseItemTag;
    final Tag.Named<Item> oreItemTag;
    final Tag.Named<Item> modOresItemTag;
    final Tag.Named<Item> itemTag;
//    final ITag.INamedTag<Item> shardTag;

    Gems(int colorIn, Rarity rarity, OreConfig.Defaults overworldOres, OreConfig.Defaults netherOres, OreConfig.Defaults endOres) {
        this.color = new Color(colorIn);
        this.rarity = rarity;

        this.oreConfigDefaults.put(Level.OVERWORLD, overworldOres);
        this.oreConfigDefaults.put(Level.NETHER, netherOres);
        this.oreConfigDefaults.put(Level.END, endOres);

        this.oreConfiguredFeatures.put(Level.OVERWORLD, Lazy.of(() ->
                createOreConfiguredFeature(Level.OVERWORLD)));
        this.oreConfiguredFeatures.put(Level.NETHER, Lazy.of(() ->
                createOreConfiguredFeature(Level.NETHER)));
        this.oreConfiguredFeatures.put(Level.END, Lazy.of(() ->
                createOreConfiguredFeature(Level.END)));

        this.glowroseConfiguredFeatures.put(Level.OVERWORLD, Lazy.of(() ->
                createGlowroseConfiguredFeature(Level.OVERWORLD)));
        this.glowroseConfiguredFeatures.put(Level.NETHER, Lazy.of(() ->
                createGlowroseConfiguredFeature(Level.NETHER)));
        this.glowroseConfiguredFeatures.put(Level.END, Lazy.of(() ->
                createGlowroseConfiguredFeature(Level.END)));

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
//        this.shardTag = ItemTags.makeWrapperTag(new ResourceLocation("forge", "nuggets/" + this.getName()).toString());
    }

    private static Tag.Named<Block> makeBlockTag(ResourceLocation name) {
        return BlockTags.bind(name.toString());
    }

    private static Tag.Named<Item> makeItemTag(ResourceLocation name) {
        return ItemTags.bind(name.toString());
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
        return new TranslatableComponent("gem.silentgems." + this.getName());
    }

    //region World generation

    public ConfiguredFeature<?, ?> getOreConfiguredFeature(ResourceKey<Level> world) {
        return this.oreConfiguredFeatures.getOrDefault(world, this.oreConfiguredFeatures.get(Level.OVERWORLD)).get();
    }

    public ConfiguredFeature<?, ?> getGlowroseConfiguredFeature(ResourceKey<Level> world) {
        return this.glowroseConfiguredFeatures.getOrDefault(world, this.glowroseConfiguredFeatures.get(Level.OVERWORLD)).get();
    }

    public OreConfig getOreConfig(ResourceKey<Level> world) {
        return this.oreConfigs.getOrDefault(world, this.oreConfigs.get(Level.OVERWORLD));
    }

    public OreConfig.Defaults getOreConfigDefaults(ResourceKey<Level> world) {
        return this.oreConfigDefaults.getOrDefault(world, this.oreConfigDefaults.get(Level.OVERWORLD));
    }

    public static void buildOreConfigs(ForgeConfigSpec.Builder builder) {
        for (Gems gem : Gems.values()) {
            gem.oreConfigs.put(Level.OVERWORLD, new OreConfig(builder,
                    gem.getName() + ".overworld",
                    gem.getOreConfigDefaults(Level.OVERWORLD)));
            gem.oreConfigs.put(Level.NETHER, new OreConfig(builder,
                    gem.getName() + ".the_nether",
                    gem.getOreConfigDefaults(Level.NETHER)));
            gem.oreConfigs.put(Level.END, new OreConfig(builder,
                    gem.getName() + ".the_end",
                    gem.getOreConfigDefaults(Level.END)));
        }
    }

    private ConfiguredFeature<?, ?> createOreConfiguredFeature(ResourceKey<Level> world) {
        OreConfig config = this.getOreConfig(world);

        OreConfiguration oreConfiguration;
        if (world == Level.NETHER) {
            oreConfiguration = new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, netherOre.get().defaultBlockState(), config.getSize());
        } else if (world == Level.END) {
            oreConfiguration = new OreConfiguration(GemsWorldFeatures.BASE_STONE_END, endOre.get().defaultBlockState(), config.getSize());
        } else {
            ImmutableList<OreConfiguration.TargetBlockState> targetList = ImmutableList.of(
                    OreConfiguration.target(OreConfiguration.Predicates.STONE_ORE_REPLACEABLES, ore.get().defaultBlockState()),
                    OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, deepslateOre.get().defaultBlockState()));
            oreConfiguration = new OreConfiguration(targetList, config.getSize());
        }

        return Feature.ORE
                .configured(oreConfiguration)
                .rangeUniform(VerticalAnchor.aboveBottom(config.getMinHeight()), VerticalAnchor.absolute(config.getMaxHeight()))
                .decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(config.getRarity())))
                .squared()
                .count(config.getCount());
    }

    private ConfiguredFeature<?, ?> createGlowroseConfiguredFeature(ResourceKey<Level> world) {
        OreConfig config = this.getOreConfig(world);
        int baseSpread = config.isEnabled() ? 2 : 0;
        int rarity = 32 * config.getRarity();

        int tryCount;
        if (world == Level.END) {
            tryCount = 6;
        } else if (world == Level.NETHER) {
            tryCount = 12;
        } else {
            tryCount = 8;
        }

        return Feature.FLOWER
                .configured(new RandomPatchConfiguration.GrassConfigurationBuilder(
                        new SimpleStateProvider(this.getGlowrose().defaultBlockState()), SimpleBlockPlacer.INSTANCE)
                        .tries(tryCount)
                        .build())
                .decorated(FeatureDecorator.COUNT_MULTILAYER.configured(new CountConfiguration(baseSpread)))
                .decorated(LibPlacements.DIMENSION_FILTER.configured(DimensionFilterConfig.whitelist(Level.OVERWORLD)))
                .decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(rarity)))
                .rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128));
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

    public Tag.Named<Block> getOreTag() {
        return oreTag;
    }

    public Tag.Named<Block> getModOresTag() {
        return modOresTag;
    }

    public Tag.Named<Block> getBlockTag() {
        return blockTag;
    }

    public Tag.Named<Block> getGlowroseTag() {
        return glowroseTag;
    }

    public Tag.Named<Item> getOreItemTag() {
        return oreItemTag;
    }

    public Tag.Named<Item> getModOresItemTag() {
        return modOresItemTag;
    }

    public Tag.Named<Item> getBlockItemTag() {
        return blockItemTag;
    }

    public Tag.Named<Item> getGlowroseItemTag() {
        return glowroseItemTag;
    }

    public Tag.Named<Item> getItemTag() {
        return itemTag;
    }

    //endregion

    public static void registerBlocks() {
        for (Gems gem : values())
            gem.ore = registerBlock(gem.getName() + "_ore", () ->
                    new GemOreBlock(gem, 2, "gem_ore", BlockBehaviour.Properties.of(Material.STONE)
                            .strength(3f)
                            .harvestTool(ToolType.PICKAXE)
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
                            .lightLevel(state -> GemsConfig.Common.glowroseNormalLight.get())
                            .strength(0)
                            .noCollission()));

        for (Gems gem : values()) {
            gem.pottedGlowrose = registerBlockNoItem("potted_" + gem.getName() + "_glowrose", () ->
                    new PottedGlowroseBlock(gem, gem.glowrose, BlockBehaviour.Properties
                            .of(Material.DECORATION)
                            .lightLevel(state -> GemsConfig.Common.glowrosePottedLight.get())
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
