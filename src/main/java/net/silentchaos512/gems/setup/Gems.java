package net.silentchaos512.gems.setup;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.block.teleporter.GemRedstoneTeleporterBlock;
import net.silentchaos512.gems.block.teleporter.GemTeleporterBlock;
import net.silentchaos512.gems.item.GemBlockItem;
import net.silentchaos512.gems.item.GemItem;
import net.silentchaos512.gems.world.OreConfigDefaults;
import net.silentchaos512.lib.util.Color;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings("NonFinalFieldInEnum")
public enum Gems {
    RUBY(0xE61D1D, //hue=0
            Rarity.COMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.defaults(4, 8, 2, -64, 32, 0.2f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty()),
    CARNELIAN(0xE04D1D, //15
            Rarity.UNCOMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 8, 1, 25, 110),
            OreConfigDefaults.empty()),
    TOPAZ(0xE6711D, //25
            Rarity.COMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.defaults(4, 8, 2, -56, 40, 0.2f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty()),
    CITRINE(0xC78B03, //40
            Rarity.UNCOMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 8, 1, 25, 110),
            OreConfigDefaults.empty()),
    HELIODOR(0xE6C51D, //50
            Rarity.COMMON,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "3",
            OreConfigDefaults.defaults(1, 6, 4, -80, -32, 0.8f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 1, 16, 72)),
    MOLDAVITE(0xA6D923, //75
            Rarity.UNCOMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 8, 1, 25, 110),
            OreConfigDefaults.empty()),
    PERIDOT(0x29DB18, //115
            Rarity.COMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.defaults(4, 8, 2, -56, 40, 0.2f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty()),
    TURQUOISE(0x3DF4BD, //160
            Rarity.RARE,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "3",
            OreConfigDefaults.defaults(1, 6, 4, -80, -32, 0.8f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 1, 16, 72)),
    KYANITE(0x41C4F3, //195 (-165)
            Rarity.RARE,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, "4",
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 1, 16, 72)),
    SAPPHIRE(0x1D60E5, //220 (-140)
            Rarity.COMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.defaults(4, 8, 2, -80, 32, 0.2f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty()),
    IOLITE(0x7543F5, //260 (-100)
            Rarity.UNCOMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.defaults(1, 6, 4, -80, -32, 0.8f),
            OreConfigDefaults.defaults(2, 8, 1, 20, 80),
            OreConfigDefaults.empty()),
    ALEXANDRITE(0xAB37E5, //280 (-80)
            Rarity.UNCOMMON,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "3",
            OreConfigDefaults.defaults(1, 6, 4, -80, -32, 0.8f),
            OreConfigDefaults.defaults(2, 8, 1, 20, 80),
            OreConfigDefaults.empty()),
    AMMOLITE(0xDB2BFF, //290 (-70)
            Rarity.RARE,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "3",
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 1, 16, 72)),
    ROSE_QUARTZ(0xFF4EAB, //330 (-30), B+30,C+40
            Rarity.RARE,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, "4",
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 1, 16, 72)),
    BLACK_DIAMOND(0x5F524C, //20, Sat=20,Lit=-36
            Rarity.EPIC,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, "4",
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(3, 8, 3, -10, 60),
            OreConfigDefaults.empty()),
    WHITE_DIAMOND(0xD5C1D2, //310 (-50), Sat=10, B+50,C+30
            Rarity.EPIC,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, "4",
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(3, 8, 3, -10, 60)),
    // Gems added by giok3r
    GARNET(0x970000,
            Rarity.UNCOMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.defaults(2, 8, 2, -64, 32, 0.2f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(1, 8, 2, -10, 60)),
    AQUAMARINE(0x6EE9F4,
            Rarity.UNCOMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.defaults(2, 8, 2, -64, 32, 0.2f),
            OreConfigDefaults.empty(),
            OreConfigDefaults.empty()),
    TANZANITE(0x3D00B9,
            Rarity.UNCOMMON,
            BlockTags.INCORRECT_FOR_IRON_TOOL, "2",
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 8, 1, 25, 110),
            OreConfigDefaults.empty()),
    OPAL(0xDDFFE4,
            Rarity.RARE,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "3",
            OreConfigDefaults.defaults(1, 6, 4, -80, 0),
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(2, 9, 2, 16, 72)),
    PEARL(0xD3CBBF,
            Rarity.RARE,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL, "3",
            OreConfigDefaults.empty(),
            OreConfigDefaults.defaults(1, 6, 4, 0, 55),
            OreConfigDefaults.defaults(2, 9, 2, 16, 72));

    private final Color color;
    private final Rarity rarity;

    // World/ore generation
    private final Map<ResourceKey<Level>, OreConfigDefaults> oreConfigDefaults = new HashMap<>();

    // Blocks
    DeferredBlock<GemOreBlock> ore;
    DeferredBlock<GemOreBlock> deepslateOre;
    DeferredBlock<GemOreBlock> netherOre;
    DeferredBlock<GemOreBlock> endOre;
    DeferredBlock<GemBlock> block;
    DeferredBlock<GemBlock> bricks;
    DeferredBlock<GemBlock> tiles;
    DeferredBlock<GemBlock> smallBricks;
    DeferredBlock<GemBlock> polishedStone;
    DeferredBlock<GemBlock> smoothStone;
    DeferredBlock<GemBlock> chiseledStone;
    DeferredBlock<GemGlassBlock> glass;
    DeferredBlock<GlowroseBlock> glowrose;
    DeferredBlock<FlowerPotBlock> pottedGlowrose;
    Map<GemLampBlock.State, DeferredBlock<GemLampBlock>> lamps = new EnumMap<>(GemLampBlock.State.class);
    DeferredBlock<GemTeleporterBlock> teleporter;
    DeferredBlock<GemTeleporterBlock> redstoneTeleporter;

    // Items
    DeferredItem<GemItem> item;

    // Tags
    final TagKey<Block> incorrectForToolTag;
    final TagKey<Block> equivalentIncorrectForToolTag;
    final String harvestTierLevelHint;
    final TagKey<Block> blockTag;
    final TagKey<Block> glowroseTag;
    final TagKey<Block> oreTag;
    final TagKey<Block> modOresTag;
    final TagKey<Item> blockItemTag;
    final TagKey<Item> glowroseItemTag;
    final TagKey<Item> oreItemTag;
    final TagKey<Item> modOresItemTag;
    final TagKey<Item> itemTag;

    Gems(int colorIn, Rarity rarity, TagKey<Block> equivalentHarvestTierTag, String harvestTierLevelHint, OreConfigDefaults overworldOres, OreConfigDefaults netherOres, OreConfigDefaults endOres) {
        this.color = new Color(colorIn);
        this.rarity = rarity;

        this.oreConfigDefaults.put(Level.OVERWORLD, overworldOres);
        this.oreConfigDefaults.put(Level.NETHER, netherOres);
        this.oreConfigDefaults.put(Level.END, endOres);

        String name = this.getName();
        this.incorrectForToolTag = makeBlockTag(SilentGems.getId("incorrect_for_" + name + "_tools"));
        this.equivalentIncorrectForToolTag = equivalentHarvestTierTag;
        this.harvestTierLevelHint = harvestTierLevelHint;
        this.blockTag = makeBlockTag(commonId("storage_blocks/" + name));
        this.glowroseTag = makeBlockTag(SilentGems.getId("glowroses/" + name));
        this.oreTag = makeBlockTag(commonId("ores/" + name));
        this.modOresTag = makeBlockTag(SilentGems.getId("ores/" + name));

        this.blockItemTag = makeItemTag(commonId("storage_blocks/" + name));
        this.glowroseItemTag = makeItemTag(SilentGems.getId("glowroses/" + name));
        this.oreItemTag = makeItemTag(commonId("ores/" + name));
        this.modOresItemTag = makeItemTag(SilentGems.getId("ores/" + name));
        this.itemTag = makeItemTag(commonId("gems/" + name));
    }

    private static TagKey<Block> makeBlockTag(Identifier name) {
        return BlockTags.create(name);
    }

    private static TagKey<Item> makeItemTag(Identifier name) {
        return ItemTags.create(name);
    }

    private static Identifier commonId(String path) {
        return Identifier.fromNamespaceAndPath("c", path);
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

    public TagKey<Block> getIncorrectForToolTag() {
        return incorrectForToolTag;
    }

    // Used by data generators

    public void generateIncorrectForToolTag(Function<TagKey<Block>, TagAppender<Block, Block>> tagProvider) {
        var intrinsicTagAppender = tagProvider.apply(this.incorrectForToolTag);
        intrinsicTagAppender.addTag(this.equivalentIncorrectForToolTag);
    }

    public String getHarvestTierLevelHint() {
        return harvestTierLevelHint;
    }

    //region World generation

    public OreConfigDefaults getOreConfigDefaults(ResourceKey<Level> level) {
        return this.oreConfigDefaults.getOrDefault(level, this.oreConfigDefaults.get(Level.OVERWORLD));
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

    public GemBlock getTiles() {
        return tiles.get();
    }

    public GemBlock getSmallBricks() {
        return smallBricks.get();
    }

    public GemBlock getPolishedStone() {
        return polishedStone.get();
    }

    public GemBlock getSmoothStone() {
        return smoothStone.get();
    }

    public GemBlock getChiseledStone() {
        return chiseledStone.get();
    }

    public GemGlassBlock getGlass() {
        return glass.get();
    }

    public GemLampBlock getLamp(GemLampBlock.State state) {
        return lamps.get(state).get();
    }

    public DeferredBlock<GemTeleporterBlock> getTeleporter() {
        return teleporter;
    }

    public DeferredBlock<GemTeleporterBlock> getRedstoneTeleporter() {
        return redstoneTeleporter;
    }

    public GlowroseBlock getGlowrose() {
        return glowrose.get();
    }

    public FlowerPotBlock getPottedGlowrose() {
        return pottedGlowrose.get();
    }

    public Item getItem() {
        return item.get();
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
            gem.ore = GemsBlocks.register(gem.getName() + "_ore",
                    properties -> new GemOreBlock(gem, 2, "gem_ore", properties),
                    properties -> properties
                            .strength(3f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.deepslateOre = GemsBlocks.register("deepslate_" + gem.getName() + "_ore",
                    properties -> new GemOreBlock(gem, 2, "deepslate_gem_ore", properties),
                    p -> BlockBehaviour.Properties.ofFullCopy(gem.ore.get())
                            .strength(4.5f, 3f)
                            .sound(SoundType.DEEPSLATE),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.netherOre = GemsBlocks.register("nether_" + gem.getName() + "_ore",
                    properties -> new GemOreBlock(gem, 3, "gem_nether_ore", properties),
                    p -> BlockBehaviour.Properties.ofFullCopy(gem.ore.get())
                            .strength(4f)
                            .sound(SoundType.NETHER_ORE),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.endOre = GemsBlocks.register("end_" + gem.getName() + "_ore",
                    properties -> new GemOreBlock(gem, 4, "gem_end_ore", properties),
                    p -> BlockBehaviour.Properties.ofFullCopy(gem.ore.get())
                            .strength(6f),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.block = GemsBlocks.register(gem.getName() + "_block",
                    properties -> new GemBlock(gem, "gem_block", properties),
                    properties -> properties
                            .strength(4, 30)
                            .sound(SoundType.METAL),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.bricks = GemsBlocks.register(gem.getName() + "_bricks",
                    properties -> new GemBlock(gem, "gem_bricks", properties),
                    properties -> properties
                            .strength(2f, 8f),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.tiles = GemsBlocks.register(gem.getName() + "_tiles",
                    properties -> new GemBlock(gem, "gem_tiles", properties),
                    properties -> properties
                            .strength(2f, 8f),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.smallBricks = GemsBlocks.register(gem.getName() + "_small_bricks",
                    properties -> new GemBlock(gem, "gem_small_bricks", properties),
                    properties -> properties
                            .strength(2f, 8f),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.polishedStone = GemsBlocks.register("polished_" + gem.getName(),
                    properties -> new GemBlock(gem, "polished_gem_stone", properties),
                    properties -> properties
                            .strength(2f, 8f),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.smoothStone = GemsBlocks.register("smooth_" + gem.getName(),
                    properties -> new GemBlock(gem, "smooth_gem_stone", properties),
                    properties -> properties
                            .strength(2f, 8f),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.chiseledStone = GemsBlocks.register("chiseled_" + gem.getName(),
                    properties -> new GemBlock(gem, "chiseled_gem_stone", properties),
                    properties -> properties
                            .strength(2f, 8f),
                    Gems::defaultBlockItem
            );

        BlockBehaviour.StatePredicate isNotSolid = (state, world, pos) -> false;
        for (Gems gem : values())
            gem.glass = GemsBlocks.register(gem.getName() + "_glass",
                    properties -> new GemGlassBlock(gem, properties),
                    properties -> properties
                            .strength(1f, 5f)
                            .sound(SoundType.GLASS)
                            .noOcclusion()
                            .isValidSpawn((state, world, pos, entityType) -> false)
                            .isRedstoneConductor(isNotSolid)
                            .isSuffocating(isNotSolid)
                            .isViewBlocking(isNotSolid),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.lamps.put(GemLampBlock.State.OFF, registerLamp(gem, GemLampBlock.State.OFF));
        for (Gems gem : values())
            gem.lamps.put(GemLampBlock.State.ON, registerLamp(gem, GemLampBlock.State.ON));
        for (Gems gem : values())
            gem.lamps.put(GemLampBlock.State.INVERTED_ON, registerLamp(gem, GemLampBlock.State.INVERTED_ON));
        for (Gems gem : values())
            gem.lamps.put(GemLampBlock.State.INVERTED_OFF, registerLamp(gem, GemLampBlock.State.INVERTED_OFF));

        for (Gems gem : values())
            gem.glowrose = GemsBlocks.register(gem.getName() + "_glowrose",
                    properties -> new GlowroseBlock(gem, properties),
                    properties -> properties
                            .sound(SoundType.GRASS)
                            .strength(0)
                            .noCollision(),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values()) {
            gem.pottedGlowrose = GemsBlocks.registerNoItem("potted_" + gem.getName() + "_glowrose",
                    properties -> new PottedGlowroseBlock(gem, () -> gem.glowrose.get(), properties),
                    properties -> properties
                            .strength(0)
            );
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(SilentGems.getId(gem.getName() + "_glowrose"), gem.pottedGlowrose);
        }

        for (Gems gem : values())
            gem.teleporter = GemsBlocks.register(gem.getName() + "_teleporter",
                    properties -> new GemTeleporterBlock(gem, properties),
                    properties -> properties
                            .sound(SoundType.METAL)
                            .strength(5),
                    Gems::defaultBlockItem
            );

        for (Gems gem : values())
            gem.redstoneTeleporter = GemsBlocks.register(gem.getName() + "_redstone_teleporter",
                    properties -> new GemRedstoneTeleporterBlock(gem, properties),
                    properties -> properties
                            .sound(SoundType.METAL)
                            .strength(5),
                    Gems::defaultBlockItem
            );
    }

    public static void registerItems() {
        for (Gems gem : values())
            gem.item = registerItem(
                    gem.getName(),
                    properties -> new GemItem(gem, "item.silentgems.gem", properties)
            );
    }

    private static DeferredBlock<GemLampBlock> registerLamp(Gems gem, GemLampBlock.State state) {
        String name = gem.getName() + "_lamp" + (state.inverted() ? "_inverted" : "") + (state.lit() ? "_on" : "");
        Function<BlockBehaviour.Properties, GemLampBlock> blockFunction = properties -> new GemLampBlock(gem, state, properties);
        UnaryOperator<BlockBehaviour.Properties> propertiesFunction = properties -> properties
                .strength(0.3f, 15)
                .lightLevel(s -> state.lit() ? 15 : 0);
        if (state.hasItem()) {
            return GemsBlocks.register(name, blockFunction, propertiesFunction, Gems::defaultBlockItem);
        } else {
            return GemsBlocks.registerNoItem(name, blockFunction, propertiesFunction);
        }
    }

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> item) {
        return registerItem(name, item, UnaryOperator.identity());
    }

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> item, UnaryOperator<Item.Properties> properties) {
        return GemsItems.ITEMS.registerItem(name, item, properties);
    }

    private static Function<Item.Properties, BlockItem> defaultBlockItem(DeferredBlock<?> block) {
        return properties -> new GemBlockItem(block.get(), properties);
    }
}
