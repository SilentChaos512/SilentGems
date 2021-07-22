package net.silentchaos512.gems.util;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
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
    private final Map<RegistryKey<World>, OreConfig.Defaults> oreConfigDefaults = new HashMap<>();
    private final Map<RegistryKey<World>, OreConfig> oreConfigs = new HashMap<>();
    private final Map<RegistryKey<World>, Lazy<ConfiguredFeature<?, ?>>> oreConfiguredFeatures = new HashMap<>();
    private final Map<RegistryKey<World>, Lazy<ConfiguredFeature<?, ?>>> glowroseConfiguredFeatures = new HashMap<>();

    // Blocks
    BlockRegistryObject<GemOreBlock> ore;
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
    final ITag.INamedTag<Block> blockTag;
    final ITag.INamedTag<Block> glowroseTag;
    final ITag.INamedTag<Block> oreTag;
    final ITag.INamedTag<Block> modOresTag;
    final ITag.INamedTag<Item> blockItemTag;
    final ITag.INamedTag<Item> glowroseItemTag;
    final ITag.INamedTag<Item> oreItemTag;
    final ITag.INamedTag<Item> modOresItemTag;
    final ITag.INamedTag<Item> itemTag;
//    final ITag.INamedTag<Item> shardTag;

    Gems(int colorIn, Rarity rarity, OreConfig.Defaults overworldOres, OreConfig.Defaults netherOres, OreConfig.Defaults endOres) {
        this.color = new Color(colorIn);
        this.rarity = rarity;

        this.oreConfigDefaults.put(World.OVERWORLD, overworldOres);
        this.oreConfigDefaults.put(World.NETHER, netherOres);
        this.oreConfigDefaults.put(World.END, endOres);

        this.oreConfiguredFeatures.put(World.OVERWORLD, Lazy.of(() ->
                createOreConfiguredFeature(World.OVERWORLD, OreFeatureConfig.FillerBlockType.NATURAL_STONE)));
        this.oreConfiguredFeatures.put(World.NETHER, Lazy.of(() ->
                createOreConfiguredFeature(World.NETHER, OreFeatureConfig.FillerBlockType.NETHERRACK)));
        this.oreConfiguredFeatures.put(World.END, Lazy.of(() ->
                createOreConfiguredFeature(World.END, GemsWorldFeatures.BASE_STONE_END)));

        this.glowroseConfiguredFeatures.put(World.OVERWORLD, Lazy.of(() ->
                createGlowroseConfiguredFeature(World.OVERWORLD)));
        this.glowroseConfiguredFeatures.put(World.NETHER, Lazy.of(() ->
                createGlowroseConfiguredFeature(World.NETHER)));
        this.glowroseConfiguredFeatures.put(World.END, Lazy.of(() ->
                createGlowroseConfiguredFeature(World.END)));

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

    private static ITag.INamedTag<Block> makeBlockTag(ResourceLocation name) {
        return BlockTags.bind(name.toString());
    }

    private static ITag.INamedTag<Item> makeItemTag(ResourceLocation name) {
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

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gem.silentgems." + this.getName());
    }

    //region World generation

    public ConfiguredFeature<?, ?> getOreConfiguredFeature(RegistryKey<World> world) {
        return this.oreConfiguredFeatures.getOrDefault(world, this.oreConfiguredFeatures.get(World.OVERWORLD)).get();
    }

    public ConfiguredFeature<?, ?> getGlowroseConfiguredFeature(RegistryKey<World> world) {
        return this.glowroseConfiguredFeatures.getOrDefault(world, this.glowroseConfiguredFeatures.get(World.OVERWORLD)).get();
    }

    public OreConfig getOreConfig(RegistryKey<World> world) {
        return this.oreConfigs.getOrDefault(world, this.oreConfigs.get(World.OVERWORLD));
    }

    public OreConfig.Defaults getOreConfigDefaults(RegistryKey<World> world) {
        return this.oreConfigDefaults.getOrDefault(world, this.oreConfigDefaults.get(World.OVERWORLD));
    }

    public static void buildOreConfigs(ForgeConfigSpec.Builder builder) {
        for (Gems gem : Gems.values()) {
            gem.oreConfigs.put(World.OVERWORLD, new OreConfig(builder,
                    gem.getName() + ".overworld",
                    gem.getOreConfigDefaults(World.OVERWORLD)));
            gem.oreConfigs.put(World.NETHER, new OreConfig(builder,
                    gem.getName() + ".the_nether",
                    gem.getOreConfigDefaults(World.NETHER)));
            gem.oreConfigs.put(World.END, new OreConfig(builder,
                    gem.getName() + ".the_end",
                    gem.getOreConfigDefaults(World.END)));
        }
    }

    private ConfiguredFeature<?, ?> createOreConfiguredFeature(RegistryKey<World> world, RuleTest fillerType) {
        OreConfig config = this.getOreConfig(world);
        int bottom = config.getMinHeight();
        return Feature.ORE
                .configured(new OreFeatureConfig(fillerType, this.getOre(world).defaultBlockState(), config.getSize()))
                .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(bottom, bottom, config.getMaxHeight())))
                .decorated(Placement.CHANCE.configured(new ChanceConfig(config.getRarity())))
                .squared()
                .count(config.getCount());
    }

    private ConfiguredFeature<?, ?> createGlowroseConfiguredFeature(RegistryKey<World> world) {
        OreConfig config = this.getOreConfig(world);
        int baseSpread = config.isEnabled() ? 2 : 0;
        int rarity = 2 * config.getRarity();

        int tryCount;
        if (world == World.END) {
            tryCount = 6;
        } else if (world == World.NETHER) {
            tryCount = 12;
        } else {
            tryCount = 8;
        }

        return Feature.FLOWER
                .configured(new BlockClusterFeatureConfig.Builder(
                        new SimpleBlockStateProvider(this.getGlowrose().defaultBlockState()), SimpleBlockPlacer.INSTANCE)
                        .tries(tryCount)
                        .build())
                .decorated(Placement.COUNT_MULTILAYER.configured(new FeatureSpreadConfig(baseSpread)))
                .decorated(LibPlacements.DIMENSION_FILTER.configured(DimensionFilterConfig.whitelist(World.OVERWORLD)))
                .decorated(Placement.CHANCE.configured(new ChanceConfig(rarity)))
                .range(128)
                .chance(32);
    }

    //endregion

    //region Block, Item, and Tag getters

    public GemOreBlock getOre(RegistryKey<World> world) {
        if (world.equals(World.NETHER))
            return netherOre.get();
        if (world.equals(World.END))
            return endOre.get();
        return ore.get();
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

    public ITag.INamedTag<Block> getOreTag() {
        return oreTag;
    }

    public ITag.INamedTag<Block> getModOresTag() {
        return modOresTag;
    }

    public ITag.INamedTag<Block> getBlockTag() {
        return blockTag;
    }

    public ITag.INamedTag<Block> getGlowroseTag() {
        return glowroseTag;
    }

    public ITag.INamedTag<Item> getOreItemTag() {
        return oreItemTag;
    }

    public ITag.INamedTag<Item> getModOresItemTag() {
        return modOresItemTag;
    }

    public ITag.INamedTag<Item> getBlockItemTag() {
        return blockItemTag;
    }

    public ITag.INamedTag<Item> getGlowroseItemTag() {
        return glowroseItemTag;
    }

    public ITag.INamedTag<Item> getItemTag() {
        return itemTag;
    }

    //endregion

    public static void registerBlocks() {
        for (Gems gem : values())
            gem.ore = registerBlock(gem.getName() + "_ore", () ->
                    new GemOreBlock(gem, 2, "gem_ore", AbstractBlock.Properties.of(Material.STONE)
                            .strength(3)
                            .harvestTool(ToolType.PICKAXE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)));

        for (Gems gem : values())
            gem.netherOre = registerBlock(gem.getName() + "_nether_ore", () ->
                    new GemOreBlock(gem, 3, "gem_nether_ore", AbstractBlock.Properties.of(Material.STONE)
                            .strength(4)
                            .harvestTool(ToolType.PICKAXE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHER_ORE)));

        for (Gems gem : values())
            gem.endOre = registerBlock(gem.getName() + "_end_ore", () ->
                    new GemOreBlock(gem, 4, "gem_end_ore", AbstractBlock.Properties.of(Material.STONE)
                            .strength(6)
                            .harvestTool(ToolType.PICKAXE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)));

        for (Gems gem : values())
            gem.block = registerBlock(gem.getName() + "_block", () ->
                    new GemBlock(gem, "gem_block", AbstractBlock.Properties.of(Material.METAL)
                            .strength(4, 30)
                            .sound(SoundType.METAL)));

        for (Gems gem : values())
            gem.bricks = registerBlock(gem.getName() + "_bricks", () ->
                    new GemBlock(gem, "gem_bricks", AbstractBlock.Properties.of(Material.STONE)
                            .strength(2f, 8f)));

        AbstractBlock.IPositionPredicate isNotSolid = (state, world, pos) -> false;
        for (Gems gem : values())
            gem.glass = registerBlock(gem.getName() + "_glass", () ->
                    new GemGlassBlock(gem, AbstractBlock.Properties.of(Material.GLASS)
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
                    new GlowroseBlock(gem, AbstractBlock.Properties.of(Material.PLANT)
                            .sound(SoundType.GRASS)
                            .lightLevel(state -> GemsConfig.Common.glowroseNormalLight.get())
                            .strength(0)
                            .noCollission()));

        for (Gems gem : values()) {
            gem.pottedGlowrose = registerBlockNoItem("potted_" + gem.getName() + "_glowrose", () ->
                    new PottedGlowroseBlock(gem, gem.glowrose, AbstractBlock.Properties
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
