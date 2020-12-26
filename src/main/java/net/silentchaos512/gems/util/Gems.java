package net.silentchaos512.gems.util;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ToolType;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.block.GemOreBlock;
import net.silentchaos512.gems.config.OreConfig;
import net.silentchaos512.gems.item.GemBlockItem;
import net.silentchaos512.gems.item.GemItem;
import net.silentchaos512.gems.setup.Registration;
import net.silentchaos512.gems.world.GemsWorldFeatures;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.utils.Color;
import net.silentchaos512.utils.Lazy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("NonFinalFieldInEnum")
public enum Gems {
    RUBY(0xE61D1D,
            OreConfig.defaults(3, 8, 1, 5, 40),
            OreConfig.empty(),
            OreConfig.empty()),
    CARNELIAN(0xE4331D,
            OreConfig.empty(),
            OreConfig.defaults(5, 12, 1, 25, 110),
            OreConfig.empty()),
    TOPAZ(0xE6711D,
            OreConfig.defaults(3, 8, 1, 15, 50),
            OreConfig.empty(),
            OreConfig.empty()),
    CITRINE(0xC78B03,
            OreConfig.empty(),
            OreConfig.defaults(5, 12, 1, 25, 110),
            OreConfig.empty()),
    HELIODOR(0xE6C51D,
            OreConfig.defaults(2, 6, 4, 0, 15),
            OreConfig.empty(),
            OreConfig.defaults(5, 10, 1, 16, 72)),
    MOLDAVITE(0xA6D923,
            OreConfig.empty(),
            OreConfig.defaults(5, 12, 1, 25, 110),
            OreConfig.empty()),
    PERIDOT(0x29DB18,
            OreConfig.defaults(3, 8, 1, 15, 50),
            OreConfig.empty(),
            OreConfig.empty()),
    TURQUOISE(0x3DF4BD,
            OreConfig.defaults(2, 6, 4, 0, 15),
            OreConfig.empty(),
            OreConfig.defaults(5, 10, 1, 16, 72)),
    KYANITE(0x41C4F3,
            OreConfig.empty(),
            OreConfig.empty(),
            OreConfig.defaults(5, 10, 1, 16, 72)),
    SAPPHIRE(0x1D1DE6,
            OreConfig.defaults(3, 8, 1, 5, 40),
            OreConfig.empty(),
            OreConfig.empty()),
    IOLITE(0x7543F5,
            OreConfig.defaults(2, 6, 4, 0, 15),
            OreConfig.defaults(4, 10, 1, 20, 80),
            OreConfig.empty()),
    ALEXANDRITE(0xBE36E8,
            OreConfig.defaults(2, 6, 4, 0, 15),
            OreConfig.defaults(4, 10, 1, 20, 80),
            OreConfig.empty()),
    AMMOLITE(0xDB2BFF,
            OreConfig.empty(),
            OreConfig.empty(),
            OreConfig.defaults(5, 10, 1, 16, 72)),
    ROSE_QUARTZ(0xFF4EAB,
            OreConfig.empty(),
            OreConfig.empty(),
            OreConfig.defaults(5, 10, 1, 16, 72)),
    BLACK_DIAMOND(0x5F524C,
            OreConfig.empty(),
            OreConfig.defaults(2, 8, 2, 10, 50),
            OreConfig.empty()),
    WHITE_DIAMOND(0xD1C1CE,
            OreConfig.empty(),
            OreConfig.empty(),
            OreConfig.defaults(2, 8, 2, 10, 50));

    private final Color color;

    // Ore generation
    private final Map<RegistryKey<World>, OreConfig.Defaults> oreConfigDefaults = new HashMap<>();
    private final Map<RegistryKey<World>, OreConfig> oreConfigs = new HashMap<>();
    private final Map<RegistryKey<World>, Lazy<ConfiguredFeature<?, ?>>> oreConfiguredFeatures = new HashMap<>();

    // Blocks
    BlockRegistryObject<GemOreBlock> ore;
    BlockRegistryObject<GemOreBlock> netherOre;
    BlockRegistryObject<GemOreBlock> endOre;
//    BlockRegistryObject<GemBlock> block;
//    BlockRegistryObject<GlowroseBlock> glowrose;
    BlockRegistryObject<FlowerPotBlock> pottedGlowrose;

    // Items
    ItemRegistryObject<GemItem> item;
    ItemRegistryObject<GemItem> shard;

    // Tags
//    final ITag.INamedTag<Block> blockTag;
//    final ITag.INamedTag<Block> glowroseTag;
    final ITag.INamedTag<Block> oreTag;
//    final ITag.INamedTag<Item> blockItemTag;
//    final ITag.INamedTag<Item> glowroseItemTag;
    final ITag.INamedTag<Item> oreItemTag;
    final ITag.INamedTag<Item> itemTag;
//    final ITag.INamedTag<Item> shardTag;

    Gems(int colorIn, OreConfig.Defaults overworldOres, OreConfig.Defaults netherOres, OreConfig.Defaults endOres) {
        this.color = new Color(colorIn);

        this.oreConfigDefaults.put(World.OVERWORLD, overworldOres);
        this.oreConfigDefaults.put(World.THE_NETHER, netherOres);
        this.oreConfigDefaults.put(World.THE_END, endOres);

        this.oreConfiguredFeatures.put(World.OVERWORLD, Lazy.of(() ->
                createOreConfiguredFeature(World.OVERWORLD, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD)));
        this.oreConfiguredFeatures.put(World.THE_NETHER, Lazy.of(() ->
                createOreConfiguredFeature(World.THE_NETHER, OreFeatureConfig.FillerBlockType.NETHERRACK)));
        this.oreConfiguredFeatures.put(World.THE_END, Lazy.of(() ->
                createOreConfiguredFeature(World.THE_END, GemsWorldFeatures.BASE_STONE_END)));

//        this.blockTag = BlockTags.makeWrapperTag(new ResourceLocation("forge", "storage_blocks/" + this.getName()).toString());
//        this.glowroseTag = BlockTags.makeWrapperTag(GemsBase.getId("glowroses/" + this.getName()).toString());
        this.oreTag = BlockTags.makeWrapperTag(new ResourceLocation("forge", "ores/" + this.getName()).toString());
//        this.blockItemTag = ItemTags.makeWrapperTag(new ResourceLocation("forge", "storage_blocks/" + this.getName()).toString());
//        this.glowroseItemTag = ItemTags.makeWrapperTag(GemsBase.getId("glowroses/" + this.getName()).toString());
        this.oreItemTag = ItemTags.makeWrapperTag(new ResourceLocation("forge", "ores/" + this.getName()).toString());
        this.itemTag = ItemTags.makeWrapperTag(new ResourceLocation("forge", "gems/" + this.getName()).toString());
//        this.shardTag = ItemTags.makeWrapperTag(new ResourceLocation("forge", "nuggets/" + this.getName()).toString());
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

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gem.silentgems." + this.getName());
    }

    //region World generation

    public ConfiguredFeature<?, ?> getOreConfiguredFeature(RegistryKey<World> world) {
        return this.oreConfiguredFeatures.getOrDefault(world, this.oreConfiguredFeatures.get(World.OVERWORLD)).get();
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
            gem.oreConfigs.put(World.THE_NETHER, new OreConfig(builder,
                    gem.getName() + ".the_nether",
                    gem.getOreConfigDefaults(World.THE_NETHER)));
            gem.oreConfigs.put(World.THE_END, new OreConfig(builder,
                    gem.getName() + ".the_end",
                    gem.getOreConfigDefaults(World.THE_END)));
        }
    }

    private ConfiguredFeature<?, ?> createOreConfiguredFeature(RegistryKey<World> world, RuleTest fillerType) {
        OreConfig config = this.getOreConfig(world);
        int bottom = config.getMinHeight();
        return Feature.ORE
                .withConfiguration(new OreFeatureConfig(fillerType, this.getOre(world).getDefaultState(), config.getSize()))
                .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(bottom, bottom, config.getMaxHeight())))
                .withPlacement(Placement.CHANCE.configure(new ChanceConfig(config.getRarity())))
                .square()
                .func_242731_b(config.getCount());
    }

    //endregion

    //region Block, Item, and Tag getters

    public GemOreBlock getOre(RegistryKey<World> world) {
        if (world.equals(World.THE_NETHER))
            return netherOre.get();
        if (world.equals(World.THE_END))
            return endOre.get();
        return ore.get();
    }

    public GemItem getItem() {
        return this.item.get();
    }

    @Deprecated
    public GemItem getShard() {
        return this.shard.get();
    }

    public ITag.INamedTag<Block> getOreTag() {
        return oreTag;
    }

    public ITag.INamedTag<Item> getOreItemTag() {
        return oreItemTag;
    }

    public ITag.INamedTag<Item> getItemTag() {
        return itemTag;
    }

    //endregion

    public static void registerBlocks() {
        for (Gems gem : values())
            gem.ore = registerBlock(gem.getName() + "_ore", () ->
                    new GemOreBlock(gem, 2, "gem_ore", AbstractBlock.Properties.create(Material.ROCK)
                            .hardnessAndResistance(3)
                            .harvestTool(ToolType.PICKAXE)
                            .setRequiresTool()
                            .sound(SoundType.STONE)));
        for (Gems gem : values())
            gem.netherOre = registerBlock(gem.getName() + "_nether_ore", () ->
                    new GemOreBlock(gem, 3, "gem_nether_ore", AbstractBlock.Properties.create(Material.ROCK)
                            .hardnessAndResistance(4)
                            .harvestTool(ToolType.PICKAXE)
                            .setRequiresTool()
                            .sound(SoundType.NETHER_ORE)));
        for (Gems gem : values())
            gem.endOre = registerBlock(gem.getName() + "_end_ore", () ->
                    new GemOreBlock(gem, 4, "gem_end_ore", AbstractBlock.Properties.create(Material.ROCK)
                            .hardnessAndResistance(6)
                            .harvestTool(ToolType.PICKAXE)
                            .setRequiresTool()
                            .sound(SoundType.STONE)));
    }

    public static void registerItems() {
        for (Gems gem : values())
            gem.item = registerItem(gem.getName(), () ->
                    new GemItem(gem, "gem", new Item.Properties().group(GemsBase.ITEM_GROUP)));

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
        Registration.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    private static <T extends Item> ItemRegistryObject<T> registerItem(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }

    private static Supplier<BlockItem> defaultBlockItem(BlockRegistryObject<?> block) {
        return () -> new GemBlockItem(block.get(), new Item.Properties().group(GemsBase.ITEM_GROUP));
    }
}
